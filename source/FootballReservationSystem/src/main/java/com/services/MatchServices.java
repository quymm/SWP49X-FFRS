package com.services;

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

    public MatchingRequestEntity findMatchingRequestEntityById(int id) {
        MatchingRequestEntity matchingRequestEntity = matchingRequestRepository.findByIdAndStatus(id, true);
        if (matchingRequestEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Matching Request have id = %s", id));
        }
        return matchingRequestEntity;
    }

    public BillEntity reserveFriendlyMatch(int timeSlotId, int userId, int voucherId) {
        AccountEntity userEntity = accountServices.findAccountEntityById(userId, "user");
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        FriendlyMatchEntity friendlyMatchEntity = new FriendlyMatchEntity();
        friendlyMatchEntity.setTimeSlotId(timeSlotEntity);
        friendlyMatchEntity.setUserId(userEntity);
        friendlyMatchEntity.setStatus(true);

        FriendlyMatchEntity savedFriendlyMatchEntity = friendlyMatchRepository.save(friendlyMatchEntity);

        InputBillDTO inputBillDTO = new InputBillDTO();
        inputBillDTO.setFriendlyMatchId(savedFriendlyMatchEntity.getId());
        if (voucherId != 0) {
            inputBillDTO.setVoucherId(voucherId);
        }
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

    public MatchingRequestEntity createNewMatchingRequest(InputMatchingRequestDTO inputMatchingRequestDTO) {
        AccountEntity user = accountServices.findAccountEntityById(inputMatchingRequestDTO.getUserId(), "user");
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
        matchingRequestEntity.setLongitude(inputMatchingRequestDTO.getLongitude());
        matchingRequestEntity.setLatitude(inputMatchingRequestDTO.getLatitude());
        matchingRequestEntity.setStatus(true);
        return matchingRequestRepository.save(matchingRequestEntity);
    }

    public List<MatchingRequestEntity> suggestOpponent(InputMatchingRequestDTO inputMatchingRequestDTO, int deviationDistance) {
        AccountEntity user = accountServices.findAccountEntityById(inputMatchingRequestDTO.getUserId(), "user");
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputMatchingRequestDTO.getFieldTypeId());

        Date date = DateTimeUtils.convertFromStringToDate(inputMatchingRequestDTO.getDate());
        Date startTime = DateTimeUtils.convertFromStringToTime(inputMatchingRequestDTO.getStartTime());
        Date endTime = DateTimeUtils.convertFromStringToTime(inputMatchingRequestDTO.getEndTime());

        CordinationPoint cordinationPointA = new CordinationPoint(NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLongitude()),
                NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLatitude()));

        int ratingScore = user.getProfileId().getRatingScore();
        // tìm các request cùng loại sân, thời gian đá dao động trong khoảng trước và sau deviationTime phút, rating score dao động trong khoảng 100 điểm
        List<MatchingRequestEntity> similarMatchingRequestList = matchingRequestRepository.findSimilarMatchingRequest(fieldType, true, date, startTime, endTime);

        List<MatchingRequestEntity> returnMatchingRequest = new ArrayList<>();
        if (!similarMatchingRequestList.isEmpty()) {
            for (MatchingRequestEntity matchingRequest : similarMatchingRequestList) {
                CordinationPoint cordinationPointB = new CordinationPoint(NumberUtils.parseFromStringToDouble(matchingRequest.getLongitude()),
                        NumberUtils.parseFromStringToDouble(matchingRequest.getLatitude()));

                double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);

                boolean checkRatingScore = matchingRequest.getUserId().getProfileId().getRatingScore() > (ratingScore - 100)
                        && matchingRequest.getUserId().getProfileId().getRatingScore() < (ratingScore + 100);

                boolean checkBlackList = blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(user.getId(), matchingRequest.getUserId().getId()) == null
                        && blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(matchingRequest.getUserId().getId(), user.getId()) == null ? true : false;
                // khoảng cách là nhỏ hơn deviation
                if (matchingRequest.getUserId().getId() != inputMatchingRequestDTO.getUserId() && distance < deviationDistance && checkBlackList && checkRatingScore) {
                    returnMatchingRequest.add(matchingRequest);
                }
            }
        }
        return returnMatchingRequest;
    }

    public TimeSlotEntity chooseSuitableField(InputMatchingRequestDTO inputMatchingRequestDTO, int matchingRequestId, int deviationDistance) {
        MatchingRequestEntity opponentMatching = matchingRequestRepository.findByIdAndStatus(matchingRequestId, true);
        // tìm những sân chung trong sở thích của 2 người chơi
        List<AccountEntity> favoritesFieldList = favoritesFieldServices.findFavoritesFieldOf2User(inputMatchingRequestDTO.getUserId(), opponentMatching.getUserId().getId());

        // tạo dữ liệu đặt sân dựa trên dữ liệu gốc theo matchingRequestId (người confirm đã đồng ý về thời gian của người tạo request)
        InputReservationDTO inputReservationDTO = new InputReservationDTO();
        inputReservationDTO.setStartTime(inputMatchingRequestDTO.getStartTime());
        inputReservationDTO.setEndTime(inputMatchingRequestDTO.getEndTime());
        inputReservationDTO.setDate(DateTimeUtils.formatDate(opponentMatching.getDate()));
        inputReservationDTO.setFieldTypeId(opponentMatching.getFieldTypeId().getId());

        if(!favoritesFieldList.isEmpty()){
            for (AccountEntity favoritesField : favoritesFieldList){
                inputReservationDTO.setFieldOwnerId(favoritesField.getId());
                TimeSlotEntity timeSlotEntity = timeSlotServices.reserveTimeSlot(inputReservationDTO);
                if (timeSlotEntity != null) {
                    // trả nửa phí tiền sân đối với tour match
                    timeSlotEntity.setPrice(timeSlotEntity.getPrice() / 2);
                    return timeSlotEntity;
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
            TimeSlotEntity timeSlotEntity = timeSlotServices.reserveTimeSlot(inputReservationDTO);
            if (timeSlotEntity != null) {
                // trả nửa phí tiền sân đối với tour match
                timeSlotEntity.setPrice(timeSlotEntity.getPrice() / 2);
                return timeSlotEntity;
            }
        }
        // nếu vẫn ko có sân phù hợp thì trả về null, hệ thống sẽ báo ko tìm được sân phù hợp
        return null;
    }

    private List<FieldOwnerAndDistance> getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(CordinationPoint cordinationPointA, int deviationDistance) {
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();
        List<AccountEntity> allfieldOwnerList = accountServices.findAccountByRole("owner");

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

    public BillEntity reserveTourMatch(int timeSlotId, int matchingRequestId, int opponentId, int voucherId) {
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        MatchingRequestEntity matchingRequestEntity = findMatchingRequestEntityById(matchingRequestId);
        AccountEntity user = matchingRequestEntity.getUserId();
        AccountEntity opponent = accountServices.findAccountEntityById(opponentId, "user");

        TourMatchEntity tourMatchEntity = new TourMatchEntity();
        tourMatchEntity.setTimeSlotId(timeSlotEntity);
        tourMatchEntity.setUserId(user);
        tourMatchEntity.setOpponentId(opponent);
        tourMatchEntity.setCompleteStatus(false);
        tourMatchEntity.setStatus(true);

        TourMatchEntity savedTourMatchEntity = tourMatchRepository.save(tourMatchEntity);

        // xóa matching request
        matchingRequestEntity.setStatus(false);
        matchingRequestRepository.save(matchingRequestEntity);

        InputBillDTO inputBillDTO = new InputBillDTO();
        if (voucherId != 0) {
            inputBillDTO.setVoucherId(voucherId);
        }
        inputBillDTO.setTourMatchId(savedTourMatchEntity.getId());
        return billServices.createBill(inputBillDTO);
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

    public List<MatchingRequestEntity> findMatchingRequestByUserId(int userId){
        AccountEntity user = accountServices.findAccountEntityById(userId, "user");
        List<MatchingRequestEntity> matchingRequestEntityList = matchingRequestRepository.findByUserIdAndStatus(user, true);
        return matchingRequestEntityList;
    }


}
