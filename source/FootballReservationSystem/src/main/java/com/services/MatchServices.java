package com.services;

import com.config.Constant;
import com.dto.*;
import com.entity.*;
import com.repository.FriendlyMatchRepository;
import com.repository.MatchingRequestRepository;
import com.repository.TourMatchRepository;
import com.utils.DateTimeUtils;
import com.utils.MapUtils;
import com.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;


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
    Constant constant;

    public MatchingRequestEntity findMatchingRequestEntityById(int id) {
        MatchingRequestEntity matchingRequestEntity = matchingRequestRepository.findByIdAndStatus(id, true);
        if (matchingRequestEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Matching Request have id = %s", id));
        }
        return matchingRequestEntity;
    }

    public BillEntity reserveFriendlyMatch(InputReserveTimeSlotDTO inputReserveTimeSlotDTO, int userId) {
        AccountEntity userEntity = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        TimeSlotEntity timeSlotEntity = timeSlotServices.reserveTimeSlot(inputReserveTimeSlotDTO);
        if (timeSlotEntity == null) {
            return null;
        }
        if (!timeSlotEntity.getReserveStatus()) {
            throw new IllegalArgumentException("Time slot not yet reserve!");
        }
        if (userEntity.getProfileId().getBalance() < timeSlotEntity.getPrice()) {
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
        return billServices.createBill(inputBillDTO);
    }

    public FriendlyMatchEntity findFriendlyMatchByTimeSlot(int timeSlotId) {
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        return friendlyMatchRepository.findByTimeSlotIdAndStatus(timeSlotEntity, true);
    }

    public TourMatchEntity findTourMatchByTimeSlot(int timeSlotId) {
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        return tourMatchRepository.findByTimeSlotIdAndStatus(timeSlotEntity, true);
    }

//    public float getMaxPriceWithTimeAndDistance(RequestReservateDTO requestReservateDTO){
//        CordinationPoint cordinationPoint = new CordinationPoint(NumberUtils.parseFromStringToDouble(requestReservateDTO.getLongitude()),
//                NumberUtils.parseFromStringToDouble(requestReservateDTO.getLatitude()));
//
//        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(cordinationPoint, requestReservateDTO.getExpectedDistance());
//        Date rushHour = DateTimeUtils.convertFromStringToTime(constant.getRushHour());
//        Date beginTime = DateTimeUtils.convertFromStringToTime(requestReservateDTO.getBeginTime());
//        Date endTime = DateTimeUtils.convertFromStringToTime(requestReservateDTO.getEndTime());
//
//        // nếu end time ko nằm trước giờ cao điểm
//        if(endTime.after(rushHour)){
//            int fromRushHourToEnd = (int) ((endTime.getTime() - rushHour.getTime())/60000);
//            int fromBeginToRush = requestReservateDTO.getDuration() - fromRushHourToEnd;
//        }
//    }



    public MatchingRequestEntity createNewMatchingRequest(InputMatchingRequestDTO inputMatchingRequestDTO) {
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(inputMatchingRequestDTO.getUserId(), constant.getUserRole());
        float maxPrice = constant.getMaxPrice();
        if (user.getProfileId().getBalance() < maxPrice * inputMatchingRequestDTO.getDuration() / 60) {
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
        matchingRequestEntity.setLongitude(inputMatchingRequestDTO.getLongitude());
        matchingRequestEntity.setLatitude(inputMatchingRequestDTO.getLatitude());
        matchingRequestEntity.setStatus(true);
        return matchingRequestRepository.save(matchingRequestEntity);
    }

    public List<MatchingRequestEntity> suggestOpponent(InputMatchingRequestDTO inputMatchingRequestDTO, int deviationDistance) {
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
                CordinationPoint cordinationPointB = new CordinationPoint(NumberUtils.parseFromStringToDouble(matchingRequest.getLongitude()),
                        NumberUtils.parseFromStringToDouble(matchingRequest.getLatitude()));
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
                    int startBefore = (int) (startTime.getTime() - endTimeOfReq.getTime()) / 60000;
                    if(startBefore >= matchingRequest.getDuration()){
                        checkTime = true;
                    }
                } else {
                    checkTime = true;
                }


                double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);

                boolean checkRatingScore = matchingRequest.getUserId().getProfileId().getRatingScore() > (ratingScore - 100)
                        && matchingRequest.getUserId().getProfileId().getRatingScore() < (ratingScore + 100);

                boolean checkBlackList = blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(user.getId(), matchingRequest.getUserId().getId()) == null
                        && blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(matchingRequest.getUserId().getId(), user.getId()) == null ? true : false;
                // khoảng cách là nhỏ hơn deviation
                if (matchingRequest.getUserId().getId() != inputMatchingRequestDTO.getUserId() && distance < deviationDistance && checkBlackList && checkRatingScore && checkTime) {
                    returnMatchingRequest.add(matchingRequest);
                }
            }
        }
        return returnMatchingRequest;
    }

    public OutputReserveTimeSlotDTO chooseSuitableField(InputMatchingRequestDTO inputMatchingRequestDTO, int matchingRequestId, int deviationDistance) {
        MatchingRequestEntity opponentMatching = matchingRequestRepository.findByIdAndStatus(matchingRequestId, true);
        // tìm những sân chung trong sở thích của 2 người chơi
        List<AccountEntity> favoritesFieldList = favoritesFieldServices.findFavoritesFieldOf2User(inputMatchingRequestDTO.getUserId(), opponentMatching.getUserId().getId());

        // tạo dữ liệu đặt sân dựa trên dữ liệu gốc theo matchingRequestId (người confirm đã đồng ý về thời gian của người tạo request)
        InputReservationDTO inputReservationDTO = new InputReservationDTO();
        inputReservationDTO.setStartTime(inputMatchingRequestDTO.getStartTime());
        inputReservationDTO.setEndTime(inputMatchingRequestDTO.getEndTime());
        inputReservationDTO.setDate(DateTimeUtils.formatDate(opponentMatching.getDate()));
        inputReservationDTO.setFieldTypeId(opponentMatching.getFieldTypeId().getId());

        if (!favoritesFieldList.isEmpty()) {
            for (AccountEntity favoritesField : favoritesFieldList) {
                inputReservationDTO.setFieldOwnerId(favoritesField.getId());
                OutputReserveTimeSlotDTO outputReserveTimeSlotDTO = timeSlotServices.pickTimeSlot(inputReservationDTO);
                if (outputReserveTimeSlotDTO != null) {
                    // trả nửa phí tiền sân đối với tour match
                    outputReserveTimeSlotDTO.setPrice(outputReserveTimeSlotDTO.getPrice() / 2);
                    return outputReserveTimeSlotDTO;
                }
            }
        }

        // khi những sân chung nằm trong sở thích của 2 người ko đặt được thì tìm những sân trung bình về khoảng cách
        // tạo list những sân và khoảng cách đến sân đó sắp xếp theo thứ tự tăng dần
        CordinationPoint cordinationPointUser = new CordinationPoint(NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLongitude()),
                NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLatitude()));
        CordinationPoint cordinationPointOpponent = new CordinationPoint(NumberUtils.parseFromStringToDouble(opponentMatching.getLongitude()),
                NumberUtils.parseFromStringToDouble(opponentMatching.getLatitude()));
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceListFromUser = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(cordinationPointUser, deviationDistance);
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceListFromOpponent = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(cordinationPointOpponent, deviationDistance);
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();

        // tìm những sân chung trong danh sách
        for (int i = 0; i < fieldOwnerAndDistanceListFromUser.size(); i++) {
            for (int j = 0; j < fieldOwnerAndDistanceListFromOpponent.size(); j++) {
                if (fieldOwnerAndDistanceListFromUser.get(i).getFieldOwner().getId() == fieldOwnerAndDistanceListFromOpponent.get(j).getFieldOwner().getId()) {
                    fieldOwnerAndDistanceList.add(fieldOwnerAndDistanceListFromUser.get(i));
                }
            }
        }


        for (FieldOwnerAndDistance fieldOwnerAndDistance : fieldOwnerAndDistanceList) {
            inputReservationDTO.setFieldOwnerId(fieldOwnerAndDistance.getFieldOwner().getId());
            OutputReserveTimeSlotDTO outputReserveTimeSlotDTO = timeSlotServices.pickTimeSlot(inputReservationDTO);
            if (outputReserveTimeSlotDTO != null) {
                // trả nửa phí tiền sân đối với tour match
                outputReserveTimeSlotDTO.setPrice(outputReserveTimeSlotDTO.getPrice() / 2);
                return outputReserveTimeSlotDTO;
            }
        }
        // nếu vẫn ko có sân phù hợp thì trả về null, hệ thống sẽ báo ko tìm được sân phù hợp
        return null;
    }

    private List<FieldOwnerAndDistance> getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(CordinationPoint cordinationPointA, int deviationDistance) {
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();
        List<AccountEntity> allfieldOwnerList = accountServices.findAccountByRole(constant.getFieldOwnerRole());

        for (AccountEntity accountEntity : allfieldOwnerList) {
            CordinationPoint cordinationPointB = new CordinationPoint(NumberUtils.parseFromStringToDouble(accountEntity.getProfileId().getLongitude()),
                    NumberUtils.parseFromStringToDouble(accountEntity.getProfileId().getLatitude()));
            double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);
            if (distance <= deviationDistance) {
                FieldOwnerAndDistance fieldOwnerAndDistance = new FieldOwnerAndDistance(accountEntity, distance);
                fieldOwnerAndDistanceList.add(fieldOwnerAndDistance);
            }
        }
        return arrangeFieldOwnerByDistance(fieldOwnerAndDistanceList);
    }

    public BillEntity reserveTourMatch(InputReserveTimeSlotDTO inputReserveTimeSlotDTO, int matchingRequestId, int userId) {
        TimeSlotEntity savedTimeSlotEntity = timeSlotServices.reserveTimeSlot(inputReserveTimeSlotDTO);
        if (savedTimeSlotEntity == null) {
            return null;
        }
        MatchingRequestEntity matchingRequestEntity = findMatchingRequestEntityById(matchingRequestId);
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        AccountEntity opponent = matchingRequestEntity.getUserId();

        TourMatchEntity tourMatchEntity = new TourMatchEntity();
        tourMatchEntity.setTimeSlotId(savedTimeSlotEntity);
        tourMatchEntity.setUserId(user);
        tourMatchEntity.setOpponentId(opponent);
        tourMatchEntity.setCompleteStatus(false);
        tourMatchEntity.setStatus(true);

        TourMatchEntity savedTourMatchEntity = tourMatchRepository.save(tourMatchEntity);

        // xóa matching request
        matchingRequestEntity.setStatus(false);
        matchingRequestRepository.save(matchingRequestEntity);

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
        List<MatchingRequestEntity> matchingRequestEntityList = matchingRequestRepository.findByUserIdAndStatus(user, true);
        return matchingRequestEntityList;
    }


}
