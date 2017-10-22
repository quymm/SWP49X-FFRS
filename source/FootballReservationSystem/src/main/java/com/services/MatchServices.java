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

    public MatchingRequestEntity findMatchingRequestEntityById(int id) {
        MatchingRequestEntity matchingRequestEntity = matchingRequestRepository.findByIdAndStatus(id, true);
        if (matchingRequestEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Matching Request have id = %s", id));
        }
        return matchingRequestEntity;
    }

    public FriendlyMatchEntity reserveFriendlyMatch(int timeSlotId, int userId) {
        AccountEntity userEntity = accountServices.findAccountEntityById(userId, "user");
        TimeSlotEntity timeSlotEntity = timeSlotServices.findById(timeSlotId);
        if (timeSlotEntity.getReserveStatus()) {
            throw new IllegalArgumentException(String.format("Time Slot have id = %s already be reserved!", timeSlotId));
        }
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
        if (inputMatchingRequestDTO.getAddress() != null) {
            CordinationPoint cordinationPoint = MapUtils.getLongitudeAndLatitudeFromAddress(inputMatchingRequestDTO.getAddress());
            matchingRequestEntity.setLongitude(cordinationPoint.getLongitude() + "");
            matchingRequestEntity.setLatitude(cordinationPoint.getLatitude() + "");
        }
        matchingRequestEntity.setStatus(true);
        return matchingRequestRepository.save(matchingRequestEntity);
    }

    public List<MatchingRequestEntity> suggestOpponent(int userId, int fieldTypeId, String address, String dateStr, String startTimeStr, int deviationTime, int deviationDistance) {
        AccountEntity user = accountServices.findAccountEntityById(userId, "user");
        FieldTypeEntity fieldType = fieldTypeServices.findById(fieldTypeId);

        Date date = DateTimeUtils.convertFromStringToDate(dateStr);
        Date time = DateTimeUtils.convertFromStringToTime(startTimeStr);
        Date startTime = new Date(time.getTime() - deviationTime * 60000);
        Date endTime = new Date(time.getTime() + deviationTime * 60000);

        int ratingScore = user.getProfileId().getRatingScore();
        // tìm các request cùng loại sân, thời gian dao động trong khoảng trước và sau deviationTime phút, rating score dao động trong khoảng 100 điểm
        List<MatchingRequestEntity> similarMatchingRequestList = matchingRequestRepository.findSimilarMatchingRequest(fieldType, true, date, startTime, endTime);

        List<MatchingRequestEntity> returnMatchingRequest = new ArrayList<>();
        if (!similarMatchingRequestList.isEmpty()) {

            for (MatchingRequestEntity matchingRequest : similarMatchingRequestList) {
                int distance = MapUtils.calculateDistanceBetweenTwoPointWithAddress(address, matchingRequest.getAddress(), "driving");

                boolean checkRatingScore = matchingRequest.getUserId().getProfileId().getRatingScore() > (ratingScore - 100)
                        && matchingRequest.getUserId().getProfileId().getRatingScore() < (ratingScore + 100);
                // khoảng cách là nhỏ hơn deviation
                if (matchingRequest.getUserId().getId() != userId && distance < deviationDistance && checkRatingScore) {
                    returnMatchingRequest.add(matchingRequest);
                }
            }
        }
        return returnMatchingRequest;
    }

    public TimeSlotEntity chooseSuitableField(InputMatchingRequestDTO inputMatchingRequestDTO, int matchingRequestId, int deviationDistance) {
        MatchingRequestEntity opponentMatching = matchingRequestRepository.findByIdAndStatus(matchingRequestId, true);

        // tạo list những sân và khoảng cách đến sân đó sắp xếp theo thứ tự tăng dần
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceListFromUser = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(inputMatchingRequestDTO.getAddress(), deviationDistance);
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceListFromOpponent = getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(opponentMatching.getAddress(), deviationDistance);
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();

        // tìm những sân chung trong danh sách
        for (int i = 0; i <= fieldOwnerAndDistanceListFromUser.size(); i++) {
            for (int j = 0; j <= fieldOwnerAndDistanceListFromOpponent.size(); j++) {
                if (fieldOwnerAndDistanceListFromUser.get(i).getFieldOwner().getId() == fieldOwnerAndDistanceListFromOpponent.get(j).getFieldOwner().getId()) {
                    fieldOwnerAndDistanceList.add(fieldOwnerAndDistanceListFromUser.get(i));
                }
            }
        }

        // tạo dữ liệu đặt sân dựa trên dữ liệu gốc theo matchingRequestId (ưu tiên theo người đặt sân trước)
        InputReservationDTO inputReservationDTO = new InputReservationDTO();
        inputReservationDTO.setStartTime(DateTimeUtils.formatTime(opponentMatching.getStartTime()));
        inputReservationDTO.setEndTime(DateTimeUtils.formatTime(opponentMatching.getEndTime()));
        inputReservationDTO.setDate(DateTimeUtils.formatDate(opponentMatching.getDate()));
        inputReservationDTO.setFieldTypeId(opponentMatching.getFieldTypeId().getId());

        for (FieldOwnerAndDistance fieldOwnerAndDistance : fieldOwnerAndDistanceList) {
            inputReservationDTO.setFieldOwnerId(fieldOwnerAndDistance.getFieldOwner().getId());
            TimeSlotEntity timeSlotEntity = timeSlotServices.reserveTimeSlot(inputReservationDTO);
            if (timeSlotEntity != null) {
                return timeSlotEntity;
            }
        }
        return null;
    }

    private List<FieldOwnerAndDistance> getFieldOwnerAndDistanceListWithAddressAndDeviationDistance(String address, int deviationDistance) {
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();
        List<AccountEntity> allfieldOwnerList = accountServices.findAccountByRole("owner");

        for (AccountEntity accountEntity : allfieldOwnerList) {
            int distance = MapUtils.calculateDistanceBetweenTwoPointWithAddress(address, accountEntity.getProfileId().getAddress(), "driving");
            if (distance <= deviationDistance) {
                FieldOwnerAndDistance fieldOwnerAndDistance = new FieldOwnerAndDistance(accountEntity, distance);
                fieldOwnerAndDistanceList.add(fieldOwnerAndDistance);
            }
        }
        return arrangeFieldOwnerByDistance(fieldOwnerAndDistanceList);
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


}
