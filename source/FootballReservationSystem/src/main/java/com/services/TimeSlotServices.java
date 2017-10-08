package com.services;

import com.dto.InputReservationDTO;
import com.entity.*;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class TimeSlotServices {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    FieldServices fieldServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    TimeEnableServices timeEnableServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    FriendlyMatchServices friendlyMatchServices;


    public List<TimeSlotEntity> findUpcomingReservationByDate(String dateString, int fieldOwnerId, int fieldTypeId) {
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatus(accountEntity, fieldTypeEntity, targetDate, true, true);
    }

    public List<TimeSlotEntity> createTimeSlotForDate(Date date) {
        // kiem tra ngay do la thu may trong tuan
        String dayInWeek = DateTimeUtils.returnDayInWeek(date);

        List<AccountEntity> fieldOwnerList = accountServices.findAccountByRole("owner");
        List<FieldTypeEntity> fieldTypeEntityList = fieldTypeServices.findAllFieldType();
        List<TimeSlotEntity> savedTimeSlotEntityList = new ArrayList<>();

        // for tất cả các loại sân
        for (FieldTypeEntity fieldTypeEntity : fieldTypeEntityList) {
            // for tất cả chủ sân
            for (AccountEntity fieldOwnerEntity : fieldOwnerList) {
                // đếm chủ sân có bao nhiêu sân loại đó
                int numberOfField = fieldServices.countNumberOfFieldByFieldOwnerAndFieldType(fieldOwnerEntity, fieldTypeEntity);
                // get list timeEnable theo chủ sân, loại sân và theo ngày
                List<TimeEnableEntity> timeEnableEntityList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDate(fieldOwnerEntity, fieldTypeEntity, dayInWeek);

                for (TimeEnableEntity timeEnableEntity : timeEnableEntityList) {
                    TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
                    timeSlotEntity.setDate(date);
                    timeSlotEntity.setFieldOwnerId(fieldOwnerEntity);
                    timeSlotEntity.setFieldTypeId(fieldTypeEntity);
                    timeSlotEntity.setStartTime(timeEnableEntity.getStartTime());
                    timeSlotEntity.setEndTime(timeEnableEntity.getEndTime());
                    timeSlotEntity.setPrice(timeEnableEntity.getPrice());
                    timeSlotEntity.setReserveStatus(false);
                    timeSlotEntity.setStatus(true);
                    // chủ sân có bao nhiêu sân thì tạo ra bấy nhiêu timeSlot tại 1 thời điểm
                    for (int i = 0; i < numberOfField; i++) {
                        savedTimeSlotEntityList.add(timeSlotRepository.save(timeSlotEntity));
                    }
                }
            }
        }
        return savedTimeSlotEntityList;
    }

    public FriendlyMatchEntity reserveFriendlyMatch(InputReservationDTO inputReservationDTO) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(inputReservationDTO.getFieldOwnerId(), "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputReservationDTO.getFieldTypeId());
        Date targetDate = DateTimeUtils.convertFromStringToDate(inputReservationDTO.getDate());
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwner, fieldType, targetDate, true) == 0) {
            createTimeSlotForDate(targetDate);
        }
        Date startTime = DateTimeUtils.convertFromStringToTime(inputReservationDTO.getStartTime());

        Date endTime = new Date(startTime.getTime() + inputReservationDTO.getDuration()*60000);
//        endTime.setTime(startTime.getTime() + inputReservationDTO.getDuration() * 60000);
        // get tất cả thời gian rảnh theo chủ sân, loại sân và ngày
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatus(fieldOwner, fieldType, targetDate, false, true);
        for (TimeSlotEntity timeSlotEntity : timeSlotEntityList) {
            boolean checkStart = timeSlotEntity.getStartTime().before(startTime) || timeSlotEntity.getStartTime().equals(startTime);
            boolean checkEnd = timeSlotEntity.getEndTime().after(endTime) || timeSlotEntity.getEndTime().equals(endTime);
            if (checkStart && checkEnd) {
                if (timeSlotEntity.getStartTime().before(startTime) && timeSlotEntity.getEndTime().after(endTime)) {
                    // khoảng thời gian trước giờ đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            timeSlotEntity.getStartTime(), startTime, timeSlotEntity.getPrice(), false, true);
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, timeSlotEntity.getPrice(), true, true);
                    // khoảng thời gian sau giờ đặt
                    TimeSlotEntity timeSlotEntity3 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            endTime, timeSlotEntity.getEndTime(), timeSlotEntity.getPrice(), false, true);

                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity1);
                    timeSlotRepository.save(timeSlotEntity3);

                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity2), inputReservationDTO.getUserId());
                } else if (timeSlotEntity.getStartTime().before(startTime)) {
                    // khoảng thời gian trước giờ đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            timeSlotEntity.getStartTime(), startTime, timeSlotEntity.getPrice(), false, true);
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, timeSlotEntity.getPrice(), true, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity1);
                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity2), inputReservationDTO.getUserId());
                } else {
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, timeSlotEntity.getPrice(), true, true);
                    // khoảng thời gian sau giờ đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            endTime, timeSlotEntity.getEndTime(), timeSlotEntity.getPrice(), false, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity2);
                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity1), inputReservationDTO.getUserId());
                }
            }
        }
        return null;
    }

    public List<TimeSlotEntity> findFreeTimeByFieldOwnerTypeAndDate(int fieldOwnerId, int fieldTypeId, String dateString) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(fieldTypeId);
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwner, fieldType, targetDate, true) == 0) {
            return createTimeSlotForDate(targetDate);
        } else {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatus(fieldOwner,
                    fieldType, targetDate, false, true);
            return timeSlotEntityList;
        }
    }


//    public TimeSlotEntity reserveFriendlyMatch(InputReservationDTO inputFriendlyMatch){
//        AccountEntity fieldOwnerEntity = accountRepository.findByIdAndStatus(inputFriendlyMatch.getFieldOwnerId(), true);
//        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(inputFriendlyMatch.getFieldTypeId());
//        List<FieldEntity> fieldEntityList = fieldRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(fieldOwnerEntity, fieldTypeEntity, true);
//        // tìm list timeslot phù hợp
//        List<TimeSlotEntity> allTimeSlotEntity = new ArrayList<>();
//        for (FieldEntity fieldEntity : fieldEntityList) {
//            List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByDateAndFieldIdAndReserveStatusAndStatus(inputFriendlyMatch.getDate(),
//                    fieldEntity, false, true);
//            allTimeSlotEntity.addAll(timeSlotEntityList);
//        }
//        for (TimeSlotEntity timeSlotEntity : allTimeSlotEntity) {
//
//        }
//    }

}
