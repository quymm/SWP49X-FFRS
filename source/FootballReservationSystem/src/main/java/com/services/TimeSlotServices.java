package com.services;

import com.dto.InputFriendlyMatch;
import com.entity.*;
import com.repository.AccountRepository;
import com.repository.FieldRepository;
import com.repository.TimeEnableRepository;
import com.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    TimeEnableRepository timeEnableRepository;

    @Autowired
    FieldTypeServices fieldTypeServices;

    public List<TimeSlotEntity> findUpcomingReservationByDate(Date targetDate, int fieldOwnerId, int fieldTypeId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatus(accountEntity, fieldTypeEntity, targetDate, true, true);
    }

    public List<TimeSlotEntity> createTimeSlotForDate(Date date) {
        // kiem tra ngay do la thu may trong tuan
        SimpleDateFormat format = new SimpleDateFormat("EE");
        String dayInWeek = format.format(date);

        List<TimeSlotEntity> savedTimeSlotEntity = new ArrayList<>();

        return savedTimeSlotEntity;
    }

//    public TimeSlotEntity reserveFriendlyMatch(InputFriendlyMatch inputFriendlyMatch){
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
