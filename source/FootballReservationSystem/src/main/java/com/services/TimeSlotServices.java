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
    FieldRepository fieldRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TimeEnableRepository timeEnableRepository;

    @Autowired
    FieldTypeServices fieldTypeServices;

    public List<TimeSlotEntity> findTimeSlotByDateFieldIdAndReservateStatus(Date targetDate, int fieldId, boolean reserveStatus) {
        FieldEntity fieldEntity = fieldRepository.findByIdAndStatus(fieldId, true);
        return timeSlotRepository.findByDateAndFieldIdAndReserveStatusAndStatus(targetDate, fieldEntity, reserveStatus, true);
    }

    public List<TimeSlotEntity> createTimeSlotForDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EE");
        String dayInWeek = format.format(date);
        List<FieldEntity> fieldEntityList = fieldRepository.findAllByStatus(true);
        List<TimeSlotEntity> savedTimeSlotEntity = new ArrayList<>();
        if (fieldEntityList != null && fieldEntityList.size() != 0) {
            for (FieldEntity fieldEntity : fieldEntityList) {
                AccountEntity accountEntity = fieldEntity.getFieldOwnerId();
                FieldTypeEntity fieldTypeEntity = fieldEntity.getFieldTypeId();
                List<TimeEnableEntity> timeEnableEntityList = timeEnableRepository.findByFieldOwnerIdAndAndFieldTypeIdAndStatus(accountEntity, fieldTypeEntity, true);
                for (TimeEnableEntity timeEnableEntity : timeEnableEntityList) {
                    if (timeEnableEntity.getDateInWeek().equalsIgnoreCase(dayInWeek)) {
                        TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
                        timeSlotEntity.setDate(date);
                        timeSlotEntity.setStartTime(timeEnableEntity.getStartTime());
                        timeSlotEntity.setEndTime(timeEnableEntity.getEndTime());
                        timeSlotEntity.setFieldId(fieldEntity);
                        timeSlotEntity.setPrice(timeEnableEntity.getPrice());
                        timeSlotEntity.setReserveStatus(false);
                        timeSlotEntity.setStatus(true);
                        savedTimeSlotEntity.add(timeSlotRepository.save(timeSlotEntity));
                    }
                }
            }
        }
        return savedTimeSlotEntity;
    }

//    public TimeSlotEntity reserveFriendlyMatch(InputFriendlyMatch inputFriendlyMatch){
//        AccountEntity fieldOwnerEntity = accountRepository.findByIdAndStatus(inputFriendlyMatch.getFieldOwnerId(), true);
//        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(inputFriendlyMatch.getFieldTypeId());
//        List<FieldEntity> fieldEntityList = fieldRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(fieldOwnerEntity, fieldTypeEntity, true);
//    }

}
