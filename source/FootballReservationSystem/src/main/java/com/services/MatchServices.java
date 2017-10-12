package com.services;

import com.dto.CordinationPoint;
import com.dto.FieldOwnerAndDistance;
import com.dto.InputMatchingRequestDTO;
import com.dto.InputReservationDTO;
import com.entity.*;
import com.repository.FriendlyMatchRepository;
import com.repository.MatchingRequestRepository;
import com.repository.TourMatchRepository;
import com.utils.DateTimeUtils;
import com.utils.MapUtils;
import com.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public MatchingRequestEntity findMatchingRequestEntityById(int id) {
        return matchingRequestRepository.findByIdAndStatus(id, true);
    }

    public FriendlyMatchEntity reserveFriendlyMatch(int timeSlotId, int userId) {
        AccountEntity userEntity = accountServices.findAccountEntityById(userId, "user");
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        FriendlyMatchEntity friendlyMatchEntity = new FriendlyMatchEntity();
        friendlyMatchEntity.setTimeSlotId(timeSlotEntity);
        friendlyMatchEntity.setUserId(userEntity);
        friendlyMatchEntity.setStatus(true);
        return friendlyMatchRepository.save(friendlyMatchEntity);
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
        matchingRequestEntity.setLongitude(inputMatchingRequestDTO.getLongitude());
        matchingRequestEntity.setLatitude(inputMatchingRequestDTO.getLatitude());
        matchingRequestEntity.setStatus(true);
        return matchingRequestRepository.save(matchingRequestEntity);
    }

    public List<MatchingRequestEntity> suggestOpponent(int userId, int fieldTypeId, String longitute, String latitute, String dateStr, String startTimeStr) {
        AccountEntity user = accountServices.findAccountEntityById(userId, "user");
        FieldTypeEntity fieldType = fieldTypeServices.findById(fieldTypeId);

        Date date = DateTimeUtils.convertFromStringToDate(dateStr);
        Date time = DateTimeUtils.convertFromStringToTime(startTimeStr);
        Date startTime = new Date(time.getTime() - 30 * 60000);
        Date endTime = new Date(time.getTime() + 30 * 60000);

        int ratingScore = user.getProfileId().getRatingScore();
        // tìm các request cùng loại sân, thời gian dao động trong khoảng trước và sau 30 phút, rating score dao động trong khoảng 100 điểm
        List<MatchingRequestEntity> similarMatchingRequestList = matchingRequestRepository.findSimilarMatchingRequest(fieldType, true, date, startTime, endTime);
        CordinationPoint cordinationPointA = new CordinationPoint(NumberUtils.parseFromStringToDouble(longitute), NumberUtils.parseFromStringToDouble(latitute));
        List<MatchingRequestEntity> returnMatchingRequest = new ArrayList<>();
        if (!similarMatchingRequestList.isEmpty()) {

            for (MatchingRequestEntity matchingRequest : similarMatchingRequestList) {
                CordinationPoint cordinationPointB = new CordinationPoint(NumberUtils.parseFromStringToDouble(matchingRequest.getLongitude())
                        , NumberUtils.parseFromStringToDouble(matchingRequest.getLatitude()));
                double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);

                boolean checkRatingScore = matchingRequest.getUserId().getProfileId().getRatingScore() > (ratingScore - 100)
                        && matchingRequest.getUserId().getProfileId().getRatingScore() < (ratingScore + 100);
                // khoảng cách là dưới 5km
                if (matchingRequest.getUserId().getId() != userId && distance < 5 && checkRatingScore) {
                    returnMatchingRequest.add(matchingRequest);
                }
            }
        }
        return returnMatchingRequest;
    }

    public TimeSlotEntity chooseSuitableField(InputMatchingRequestDTO inputMatchingRequestDTO, int matchingRequestId) {
        MatchingRequestEntity matchingRequestEntity = findMatchingRequestEntityById(matchingRequestId);

        double longitute = (NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLongitude()) + NumberUtils.parseFromStringToDouble(matchingRequestEntity.getLongitude())) / 2;
        double latitute = (NumberUtils.parseFromStringToDouble(inputMatchingRequestDTO.getLatitude()) + NumberUtils.parseFromStringToDouble(matchingRequestEntity.getLatitude())) / 2;
        CordinationPoint midCordinationPoint = new CordinationPoint();
        midCordinationPoint.setLongitude(longitute);
        midCordinationPoint.setLatitude(latitute);

        InputReservationDTO inputReservationDTO = new InputReservationDTO();
        inputReservationDTO.setDate(inputMatchingRequestDTO.getDate());

        // thời gian bắt đầu và kết thúc sẽ được ưu tiên theo người gửi request
        inputReservationDTO.setStartTime(DateTimeUtils.formatTime(matchingRequestEntity.getStartTime()));
        inputReservationDTO.setEndTime(DateTimeUtils.formatTime(matchingRequestEntity.getEndTime()));
        inputReservationDTO.setFieldTypeId(inputMatchingRequestDTO.getFieldTypeId());

        List<AccountEntity> fieldOwnerList = accountServices.findAccountByRole("owner");

        List<FieldOwnerAndDistance> fieldOwnerListAndDistance = new ArrayList<>();

        // tạo 1 list chứa chủ sân và khoảng cách từ trung điểm đến vị trí của sân
        for (AccountEntity fieldowner : fieldOwnerList) {
            CordinationPoint cordinationPointField = new CordinationPoint(NumberUtils.parseFromStringToDouble(fieldowner.getProfileId().getLongitude()),
                    NumberUtils.parseFromStringToDouble(fieldowner.getProfileId().getLatitude()));
            double distance = MapUtils.calculateDistanceBetweenTwoPoint(midCordinationPoint, cordinationPointField);
            if (distance < 8) {
                FieldOwnerAndDistance fieldOwnerAndDistance = new FieldOwnerAndDistance(fieldowner, distance);
                fieldOwnerListAndDistance.add(fieldOwnerAndDistance);
            }
        }

        //  sắp xếp khoảng cách theo thứ tự tăng dần
        if (!fieldOwnerListAndDistance.isEmpty())
            if (fieldOwnerListAndDistance.size() > 1) {
                {
                    for (int i = 0; i < fieldOwnerListAndDistance.size(); i++) {
                        for (int j = fieldOwnerListAndDistance.size() - 1; j > 0; j--) {
                            if (fieldOwnerListAndDistance.get(j).getDistance() < fieldOwnerListAndDistance.get(j - 1).getDistance()) {
                                FieldOwnerAndDistance temp = fieldOwnerListAndDistance.get(j);
                                fieldOwnerListAndDistance.set(j, fieldOwnerListAndDistance.get(j - 1));
                                fieldOwnerListAndDistance.set(j - 1, temp);
                            }
                        }
                    }
                }
            }
        for (FieldOwnerAndDistance fieldOwnerAndDistance : fieldOwnerListAndDistance) {
            inputReservationDTO.setFieldOwnerId(fieldOwnerAndDistance.getFieldOwner().getId());
            TimeSlotEntity timeSlotEntity = timeSlotServices.reserveTimeSlot(inputReservationDTO);
            if (timeSlotEntity != null) {
                return timeSlotEntity;
            }
        }
        return null;
    }

    public TourMatchEntity reserveTourMatch(int timeSlotId, int userId, int opponentId) {
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        AccountEntity user = accountServices.findAccountEntityById(userId, "user");
        AccountEntity opponent = accountServices.findAccountEntityById(opponentId, "user");

        TourMatchEntity tourMatchEntity = new TourMatchEntity();
        tourMatchEntity.setTimeSlotId(timeSlotEntity);
        tourMatchEntity.setUserId(user);
        tourMatchEntity.setOpponentId(opponent);
        tourMatchEntity.setCompleteStatus(false);
        tourMatchEntity.setStatus(true);
        return tourMatchRepository.save(tourMatchEntity);
    }


}
