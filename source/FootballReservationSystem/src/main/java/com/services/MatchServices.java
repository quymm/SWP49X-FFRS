package com.services;

import com.config.Constant;
import com.dto.*;
import com.entity.*;
import com.repository.FriendlyMatchRepository;
import com.repository.MatchingRequestRepository;
import com.repository.ProfileRepository;
import com.repository.TourMatchRepository;
import com.utils.DateTimeUtils;
import com.utils.MapUtils;
import com.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class MatchServices {
    @Autowired
    FriendlyMatchRepository friendlyMatchRepository;

    @Autowired
    TourMatchRepository tourMatchRepository;

    @Autowired
    MatchingRequestRepository matchingRequestRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    FieldServices fieldServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    BlacklistOpponentServices blacklistOpponentServices;

    @Autowired
    FavoritesFieldServices favoritesFieldServices;

    @Autowired
    BillServices billServices;

    @Autowired
    StandardPriceServices standardPriceServices;

    @Autowired
    TeamMemberServices teamMemberServices;

    @Autowired
    SmsService smsService;
    @Autowired
    Constant constant;

    public MatchingRequestEntity findMatchingRequestEntityById(int id) {
        MatchingRequestEntity matchingRequestEntity = matchingRequestRepository.findByIdAndStatus(id, true);
        if (matchingRequestEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Matching Request have id = %s", id));
        }
        return matchingRequestEntity;
    }

    public BillEntity reserveFriendlyMatch(InputReserveTimeSlotDTO inputReserveTimeSlotDTO) {
        AccountEntity userEntity = accountServices.findAccountEntityByIdAndRole(inputReserveTimeSlotDTO.getUserId(), constant.getUserRole());
        TimeSlotEntity timeSlotEntity = timeSlotServices.reserveTimeSlot(inputReserveTimeSlotDTO);
        if (timeSlotEntity == null) {
            return null;
        }
        if (!timeSlotEntity.getReserveStatus()) {
            throw new IllegalArgumentException("Time slot not yet reserve!");
        }
        if ((userEntity.getProfileId().getBalance() - userEntity.getProfileId().getAccountPayable()) < timeSlotEntity.getPrice()) {
            timeSlotServices.cancelReservationTimeSlot(timeSlotEntity.getId());
            throw new IllegalArgumentException("Not enough money to reserve field");
        }
        FriendlyMatchEntity friendlyMatchEntity = new FriendlyMatchEntity();
        friendlyMatchEntity.setTimeSlotId(timeSlotEntity);
        friendlyMatchEntity.setUserId(userEntity);
        friendlyMatchEntity.setStatus(true);
        FriendlyMatchEntity savedFriendlyMatchEntity = friendlyMatchRepository.save(friendlyMatchEntity);

        InputBillDTO inputBillDTO = new InputBillDTO();
        inputBillDTO.setFriendlyMatchId(savedFriendlyMatchEntity.getId());
        BillEntity billEntity = billServices.createBill(inputBillDTO);
        List<TeamMemberEntity> teamMemberEntities = teamMemberServices.findTeamMemberListWithCaptainId(userEntity.getId());

        smsService.sendMessageToPlayerFriendLy(teamMemberEntities,friendlyMatchEntity);

        return billEntity;
    }

    public FriendlyMatchEntity findFriendlyMatchByTimeSlot(int timeSlotId) {
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        return friendlyMatchRepository.findByTimeSlotIdAndStatus(timeSlotEntity, true);
    }

    public TourMatchEntity findTourMatchByTimeSlot(int timeSlotId) {
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        return tourMatchRepository.findByTimeSlotIdAndStatus(timeSlotEntity, true);
    }

    public OutputMatchingRequestDTO createNewMatchingRequest(InputMatchingRequestDTO inputMatchingRequestDTO) {
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(inputMatchingRequestDTO.getUserId(), constant.getUserRole());

        RequestReservateDTO requestReservateDTO = new RequestReservateDTO();
        requestReservateDTO.setStartTime(inputMatchingRequestDTO.getStartTime());
        requestReservateDTO.setEndTime(inputMatchingRequestDTO.getEndTime());
        requestReservateDTO.setDuration(inputMatchingRequestDTO.getDuration());
        requestReservateDTO.setFieldTypeId(inputMatchingRequestDTO.getFieldTypeId());

        // lấy giá cao nhất của khung giờ yêu cầu
        float maxPrice = standardPriceServices.getMaxPriceWithRequestReservationDTO(requestReservateDTO);

        if ((user.getProfileId().getBalance() - user.getProfileId().getAccountPayable()) < maxPrice) {
            throw new IllegalArgumentException(String.format("User not have enough money to create request!"));
        }
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputMatchingRequestDTO.getFieldTypeId());
        Date date = DateTimeUtils.convertFromStringToDate(inputMatchingRequestDTO.getDate());
        Date startTime = DateTimeUtils.convertFromStringToTime(inputMatchingRequestDTO.getStartTime());
        Date endTime = DateTimeUtils.convertFromStringToTime(inputMatchingRequestDTO.getEndTime());

        MatchingRequestEntity matchingRequestEntity = new MatchingRequestEntity();
        matchingRequestEntity.setFieldTypeId(fieldType);
        matchingRequestEntity.setUserId(user);
        matchingRequestEntity.setDate(date);
        matchingRequestEntity.setStartTime(startTime);
        matchingRequestEntity.setEndTime(endTime);
        matchingRequestEntity.setDuration(inputMatchingRequestDTO.getDuration());
        matchingRequestEntity.setExpectedDistance(inputMatchingRequestDTO.getExpectedDistance());
        matchingRequestEntity.setLongitude(NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLongitude()));
        matchingRequestEntity.setLatitude(NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLatitude()));
        matchingRequestEntity.setAddress(inputMatchingRequestDTO.getAddress());
        matchingRequestEntity.setExpectedPrice(maxPrice / 2);
        matchingRequestEntity.setStatus(true);

        // ghi nợ cho người chơi
        user.getProfileId().setAccountPayable(user.getProfileId().getAccountPayable() + maxPrice / 2);
        profileRepository.save(user.getProfileId());
        List<MatchingRequestEntity> similarMatchingRequestList = suggestOpponent(inputMatchingRequestDTO);
        MatchingRequestEntity savedMatchingRequestEntity = matchingRequestRepository.save(matchingRequestEntity);

        OutputMatchingRequestDTO outputMatchingRequestDTO = new OutputMatchingRequestDTO();
        outputMatchingRequestDTO.setMatchingRequestId(savedMatchingRequestEntity.getId());
        outputMatchingRequestDTO.setSimilarMatchingRequestList(similarMatchingRequestList);

        return outputMatchingRequestDTO;
    }

    public List<MatchingRequestEntity> suggestOpponent(InputMatchingRequestDTO inputMatchingRequestDTO) {
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(inputMatchingRequestDTO.getUserId(), constant.getUserRole());
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputMatchingRequestDTO.getFieldTypeId());

        Date date = DateTimeUtils.convertFromStringToDate(inputMatchingRequestDTO.getDate());
        Date startTime = DateTimeUtils.convertFromStringToTime(inputMatchingRequestDTO.getStartTime());
        Date endTime = DateTimeUtils.convertFromStringToTime(inputMatchingRequestDTO.getEndTime());

        CordinationPoint cordinationPointA = new CordinationPoint(NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLongitude()),
                NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLatitude()));

        int ratingScore = user.getProfileId().getRatingScore();

        // tìm các request cùng loại sân, thời gian đá dao động trong khoảng trước và sau deviationTime phút, rating score dao động trong khoảng 100 điểm
        List<MatchingRequestEntity> similarMatchingRequestList = matchingRequestRepository.findSimilarMatchingRequest(fieldType, true, date, inputMatchingRequestDTO.getDuration(), startTime, endTime);

        List<MatchingRequestEntity> returnMatchingRequest = new ArrayList<>();
        if (!similarMatchingRequestList.isEmpty()) {
            for (MatchingRequestEntity matchingRequest : similarMatchingRequestList) {
                CordinationPoint cordinationPointB = new CordinationPoint(matchingRequest.getLongitude(),
                        matchingRequest.getLatitude());
                Date startTimeOfReq = DateTimeUtils.convertFromStringToTime(DateTimeUtils.formatTime(matchingRequest.getStartTime()));
                Date endTimeOfReq = DateTimeUtils.convertFromStringToTime(DateTimeUtils.formatTime(matchingRequest.getEndTime()));
                // nếu endtime của matching sau startTime của input 1 khoảng thời gian nhỏ hơn duration
                // hoặc starttime của matching trước endTime của input 1 khoảng thời gian nhỏ hơn duration
                // thì matching request đó ko thỏa mãn
                boolean checkTime = false;
                if (startTime.before(startTimeOfReq) && endTime.after(startTimeOfReq)) {
                    int endAfter = (int) (endTime.getTime() - startTimeOfReq.getTime()) / 60000;
                    if (endAfter >= matchingRequest.getDuration()) {
                        checkTime = true;
                    }
                } else if (startTime.before(endTimeOfReq) && endTime.after(endTimeOfReq)) {
                    int startBefore = (int) (endTimeOfReq.getTime() - startTime.getTime()) / 60000;
                    if (startBefore >= matchingRequest.getDuration()) {
                        checkTime = true;
                    }
                } else {
                    checkTime = true;
                }


                double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);

                boolean checkRatingScore = matchingRequest.getUserId().getProfileId().getRatingScore() > (ratingScore - 50)
                        && matchingRequest.getUserId().getProfileId().getRatingScore() < (ratingScore + 50);

                boolean checkBlackList = blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(user.getId(), matchingRequest.getUserId().getId()) == null
                        && blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(matchingRequest.getUserId().getId(), user.getId()) == null ? true : false;

                // khoảng cách là dựa vào người có yêu cầu khoảng cách ngắn hơn
                int expectedDistance = matchingRequest.getExpectedDistance() < inputMatchingRequestDTO.getExpectedDistance() ? matchingRequest.getExpectedDistance() : inputMatchingRequestDTO.getExpectedDistance();
                if (matchingRequest.getUserId().getId() != inputMatchingRequestDTO.getUserId() && distance <= expectedDistance && checkBlackList && checkRatingScore && checkTime) {
                    returnMatchingRequest.add(matchingRequest);
                }
            }
        }
        return returnMatchingRequest;
    }

    public List<OutputReserveTimeSlotDTO> chooseSuitableField(InputMatchingRequestDTO inputMatchingRequestDTO, int matchingRequestId) {
        MatchingRequestEntity opponentMatching = matchingRequestRepository.findByIdAndStatus(matchingRequestId, true);

        // tạo dữ liệu đặt sân dựa trên dữ liệu gốc theo matchingRequestId (người confirm đã đồng ý về thời gian của người tạo request)
        InputReservationDTO inputReservationDTO = new InputReservationDTO();
        inputReservationDTO.setStartTime(inputMatchingRequestDTO.getStartTime());
        inputReservationDTO.setEndTime(inputMatchingRequestDTO.getEndTime());
        inputReservationDTO.setDate(DateTimeUtils.formatDate(opponentMatching.getDate()));
        inputReservationDTO.setFieldTypeId(opponentMatching.getFieldTypeId().getId());


        // tìm những sân trung bình về khoảng cách và nếu là sân ưa thích thì ưu tiên
        // tạo list những sân và khoảng cách đến sân đó sắp xếp theo thứ tự tăng dần
        CordinationPoint cordinationPointUser = new CordinationPoint(NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLongitude()),
                NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLatitude()));
        CordinationPoint cordinationPointOpponent = new CordinationPoint(opponentMatching.getLongitude(), opponentMatching.getLatitude());
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceListFromUser = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(cordinationPointUser, inputMatchingRequestDTO.getExpectedDistance(), false);
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceListFromOpponent = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(cordinationPointOpponent, opponentMatching.getExpectedDistance(), false);
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();

        // tìm những sân chung trong danh sách
        for (int i = 0; i < fieldOwnerAndDistanceListFromUser.size(); i++) {
            for (int j = 0; j < fieldOwnerAndDistanceListFromOpponent.size(); j++) {
                if (fieldOwnerAndDistanceListFromUser.get(i).getFieldOwner().getId() == fieldOwnerAndDistanceListFromOpponent.get(j).getFieldOwner().getId()) {
                    fieldOwnerAndDistanceList.add(fieldOwnerAndDistanceListFromUser.get(i));
                }
            }
        }

        // tìm những sân ưa thích của 2 user
        List<AccountEntity> favoritesFieldList = favoritesFieldServices.findFavoritesFieldOf2User(inputMatchingRequestDTO.getUserId(), opponentMatching.getUserId().getId());

        // list chứa những sân ưa thích
        List<OutputReserveTimeSlotDTO> outputReserveTimeSlotDTOList = new ArrayList<>();
        for (FieldOwnerAndDistance fieldOwnerAndDistance : fieldOwnerAndDistanceList) {
            inputReservationDTO.setFieldOwnerId(fieldOwnerAndDistance.getFieldOwner().getId());
            OutputReserveTimeSlotDTO outputReserveTimeSlotDTO = timeSlotServices.pickTimeSlot(inputReservationDTO);
            if (outputReserveTimeSlotDTO != null) {
                // trả nửa phí tiền sân đối với tour match
                outputReserveTimeSlotDTO.setPrice(outputReserveTimeSlotDTO.getPrice() / 2);
                outputReserveTimeSlotDTOList.add(outputReserveTimeSlotDTO);
            }
        }

        // list chứa kết quả trả về, ưu tiên cho sân ưa thích, nếu sân ưa thích ko có thì trả về list các sân gần nhất
        List<OutputReserveTimeSlotDTO> returnOutputReserveTimeSlotDTO = new ArrayList<>();
        if (!outputReserveTimeSlotDTOList.isEmpty() && outputReserveTimeSlotDTOList.size() != 1 && !favoritesFieldList.isEmpty()) {
            for (OutputReserveTimeSlotDTO outputReserveTimeSlotDTO : outputReserveTimeSlotDTOList) {
                for (AccountEntity fieldOwner : favoritesFieldList) {
                    if (outputReserveTimeSlotDTO.getFieldOwnerId().getId() == fieldOwner.getId()) {
                        returnOutputReserveTimeSlotDTO.add(outputReserveTimeSlotDTO);
                        break;
                    }
                }
            }
        }

        if (returnOutputReserveTimeSlotDTO.isEmpty())
            returnOutputReserveTimeSlotDTO.addAll(outputReserveTimeSlotDTOList);


        // nếu vẫn ko có sân phù hợp thì trả về null, hệ thống sẽ báo ko tìm được sân phù hợp
        return returnOutputReserveTimeSlotDTO;
    }

    private List<FieldOwnerAndDistance> getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(CordinationPoint cordinationPointA, int deviationOrUserId, boolean favoritesField) {
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();
        List<AccountEntity> allfieldOwnerList = new ArrayList<>();
        if (favoritesField) {
            allfieldOwnerList = favoritesFieldServices.findFavoritesFieldListByUserId(deviationOrUserId);
        } else {
            allfieldOwnerList = accountServices.findAccountByRole(constant.getFieldOwnerRole());
        }

        for (AccountEntity accountEntity : allfieldOwnerList) {
            CordinationPoint cordinationPointB = new CordinationPoint(accountEntity.getProfileId().getLongitude(),
                    accountEntity.getProfileId().getLatitude());
            double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);
            if (deviationOrUserId > 0) {
                if (distance <= deviationOrUserId) {
                    FieldOwnerAndDistance fieldOwnerAndDistance = new FieldOwnerAndDistance(accountEntity, distance);
                    fieldOwnerAndDistanceList.add(fieldOwnerAndDistance);
                }
            } else {
                FieldOwnerAndDistance fieldOwnerAndDistance = new FieldOwnerAndDistance(accountEntity, distance);
                fieldOwnerAndDistanceList.add(fieldOwnerAndDistance);
            }
        }
        return arrangeFieldOwnerByDistance(fieldOwnerAndDistanceList);
    }

    public BillEntity reserveTourMatch(InputReserveTimeSlotDTO inputReserveTimeSlotDTO, int matchingRequestId) {
        TimeSlotEntity savedTimeSlotEntity = timeSlotServices.reserveTimeSlot(inputReserveTimeSlotDTO);
        if (savedTimeSlotEntity == null) {
            return null;
        }
        MatchingRequestEntity matchingRequestEntity = findMatchingRequestEntityById(matchingRequestId);
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(inputReserveTimeSlotDTO.getUserId(), constant.getUserRole());
        if ((user.getProfileId().getBalance() - user.getProfileId().getAccountPayable()) < (savedTimeSlotEntity.getPrice() / 2)) {
            throw new IllegalArgumentException("Not enough available balances to reserve field!");
        }
        AccountEntity opponent = matchingRequestEntity.getUserId();

        TourMatchEntity tourMatchEntity = new TourMatchEntity();
        tourMatchEntity.setTimeSlotId(savedTimeSlotEntity);
        tourMatchEntity.setUserId(user);
        tourMatchEntity.setOpponentId(opponent);
        tourMatchEntity.setCompleteStatus(false);
        tourMatchEntity.setStatus(true);

        TourMatchEntity savedTourMatchEntity = tourMatchRepository.save(tourMatchEntity);

        // xóa matching request và hoàn tiền đã chiếm của matching request cho người dùng
        opponent.getProfileId().setAccountPayable(opponent.getProfileId().getAccountPayable() - matchingRequestEntity.getExpectedPrice());
        profileRepository.save(opponent.getProfileId());
        matchingRequestEntity.setStatus(false);
        matchingRequestRepository.delete(matchingRequestEntity);

        // create bill of user
        InputBillDTO billOfUser = new InputBillDTO();
        billOfUser.setTourMatchId(savedTourMatchEntity.getId());
        billOfUser.setOpponentPayment(false);
        BillEntity savedBillOfUser = billServices.createBill(billOfUser);

        // create bill of opponent
        InputBillDTO billOfOpponent = new InputBillDTO();
        billOfOpponent.setOpponentPayment(true);
        billOfOpponent.setTourMatchId(savedTourMatchEntity.getId());
        billServices.createBill(billOfOpponent);

        List<TeamMemberEntity> teamMemberEntitiesA = teamMemberServices.findTeamMemberListWithCaptainId(user.getId());
        List<TeamMemberEntity> teamMemberEntitiesB = teamMemberServices.findTeamMemberListWithCaptainId(opponent.getId());
        SmsService.sendMessageToPlayer(teamMemberEntitiesA, teamMemberEntitiesB, tourMatchEntity);
        return savedBillOfUser;
    }

    public List<FieldOwnerAndDistance> arrangeFieldOwnerByDistance(List<FieldOwnerAndDistance> inputList) {
        if (!inputList.isEmpty()) {
            if (inputList.size() > 1) {
                {
                    for (int i = 0; i < inputList.size(); i++) {
                        for (int j = inputList.size() - 1; j > 0; j--) {
                            if (inputList.get(j).getDistance() < inputList.get(j - 1).getDistance()) {
                                FieldOwnerAndDistance temp = inputList.get(j);
                                inputList.set(j, inputList.get(j - 1));
                                inputList.set(j - 1, temp);
                            }
                        }
                    }
                }
                return inputList;
            }
            return inputList;
        }
        return inputList;
    }

    public TourMatchEntity findTourMatchEntityById(int tourMatchId) {
        TourMatchEntity tourMatchEntity = tourMatchRepository.findByIdAndStatus(tourMatchId, true);
        if (tourMatchEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Tour Match have id = %s", tourMatchId));
        }
        return tourMatchEntity;
    }

    public FriendlyMatchEntity findFriendlyMatchEntityById(int friendlyMatchId) {
        FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByIdAndStatus(friendlyMatchId, true);
        if (friendlyMatchEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Friendly Match have id = %s", friendlyMatchId));
        }
        return friendlyMatchEntity;
    }

    public List<MatchingRequestEntity> findMatchingRequestByUserId(int userId) {
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        List<MatchingRequestEntity> matchingRequestEntityList = matchingRequestRepository.findByUserId(user);
        return matchingRequestEntityList;
    }

    public boolean cancelMatchingRequest(int matchingRequestId) {
        MatchingRequestEntity matchingRequestEntity = matchingRequestRepository.findByIdAndStatus(matchingRequestId, true);
        matchingRequestEntity.setStatus(false);
        // hoàn tiền đã chiếm trong matching request cho người dùng
        ProfileEntity profileOfUser = matchingRequestEntity.getUserId().getProfileId();
        profileOfUser.setAccountPayable(profileOfUser.getAccountPayable() - matchingRequestEntity.getExpectedPrice());
        profileRepository.save(profileOfUser);
        matchingRequestRepository.save(matchingRequestEntity);
        return true;
    }

    public boolean deleteMatchingRequest(int matchingRequestId){
        MatchingRequestEntity matchingRequestEntity = matchingRequestRepository.findByIdAndStatus(matchingRequestId, true);
        // hoàn tiền đã chiếm trong matching request
        ProfileEntity profileOfUser = matchingRequestEntity.getUserId().getProfileId();
        profileOfUser.setAccountPayable(profileOfUser.getAccountPayable() - matchingRequestEntity.getExpectedPrice());
        profileRepository.save(profileOfUser);
        matchingRequestRepository.delete(matchingRequestEntity);
        return true;
    }
}
