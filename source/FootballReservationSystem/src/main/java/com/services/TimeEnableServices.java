package com.services;

import com.config.Constant;
import com.dto.InputTimeEnableDTO;
import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import com.entity.TimeSlotEntity;
import com.repository.TimeEnableRepository;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class TimeEnableServices {
    @Autowired
    TimeEnableRepository timeEnableRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    Constant constant;

//    public List<TimeEnableEntity> setUpTimeEnable(List<InputTimeEnableDTO> inputTimeEnableDTOList) {
////        int fieldOwnerId = inputTimeEnableDTOList.get(0).getFieldOwnerId();
////        int fieldTypeId = inputTimeEnableDTOList.get(0).getFieldTypeId();
////        List<TimeEnableEntity> timeEnableOfFieldInDb = findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwnerId, fieldTypeId);
//        List<TimeEnableEntity> savedTimeEnableEntity = new ArrayList<>();
//        for (InputTimeEnableDTO inputTimeEnableDTO : inputTimeEnableDTOList) {
//            TimeEnableEntity timeEnableEntity = convertFromInputTimeEnableDTOToEntity(inputTimeEnableDTO);
//            savedTimeEnableEntity.add(timeEnableRepository.save(timeEnableEntity));
//        }
//        return savedTimeEnableEntity;
//    }

    public List<TimeEnableEntity> updateNewTimeEnable(List<InputTimeEnableDTO> inputTimeEnableDTOList) {
        int fieldOwnerId = inputTimeEnableDTOList.get(0).getFieldOwnerId();
        int fieldTypeId = inputTimeEnableDTOList.get(0).getFieldTypeId();
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
//        List<TimeEnableEntity> timeEnableOfFieldInDb = findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwnerId, fieldTypeId);
        /*findByFieldOwnerIdAndReserveStatusAndDateAndStatus*/
        // lay ngay hien tai
        Date currDate = new Date();
        String sCurrDate = DateTimeUtils.formatDate(currDate);

        // Muc tieu la 8 ngay sau
        int count = 8;
        String stargetDate = DateTimeUtils.getDateAfter(sCurrDate, count);
        Date targetDate = DateTimeUtils.convertFromStringToDate(stargetDate);
        boolean check = true;

        while ((check) && count > 0){
//            tim ngay targetdate xem co time slot nao dc dat chua
            List<TimeSlotEntity> listReservedTimeSlotEntity = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(
                    fieldOwnerEntity, fieldTypeEntity, targetDate, true, true);

            if (listReservedTimeSlotEntity.isEmpty()){
                List<TimeSlotEntity> listNotReserveTimeSlotEntity = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(
                        fieldOwnerEntity, fieldTypeEntity, targetDate, false, true);
                timeSlotServices.deleteTimeSlotInList(listNotReserveTimeSlotEntity);
                count --;
                stargetDate = DateTimeUtils.getDateAfter(sCurrDate, count);
                targetDate = DateTimeUtils.convertFromStringToDate(stargetDate);
            } else {
                check = false;
            }
        }


        List<TimeEnableEntity> savedTimeEnableEntity = new ArrayList<>();
        for (InputTimeEnableDTO inputTimeEnableDTO : inputTimeEnableDTOList) {
            TimeEnableEntity timeEnableEntity = convertFromInputTimeEnableDTOToEntity(inputTimeEnableDTO, targetDate);
            savedTimeEnableEntity.add(timeEnableRepository.save(timeEnableEntity));
        }
        return savedTimeEnableEntity;


        ////        int fieldOwnerId = inputTimeEnableDTOList.get(0).getFieldOwnerId();
////        int fieldTypeId = inputTimeEnableDTOList.get(0).getFieldTypeId();
////        List<TimeEnableEntity> timeEnableOfFieldInDb = findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwnerId, fieldTypeId);
//        List<TimeEnableEntity> savedTimeEnableEntity = new ArrayList<>();
//        for (InputTimeEnableDTO inputTimeEnableDTO : inputTimeEnableDTOList) {
//            TimeEnableEntity timeEnableEntity = convertFromInputTimeEnableDTOToEntity(inputTimeEnableDTO);
//            savedTimeEnableEntity.add(timeEnableRepository.save(timeEnableEntity));
//        }
//        return savedTimeEnableEntity;
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerIdAndFieldTypeId(int fieldOwnerId, int fieldTypeId) {
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return timeEnableRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(fieldOwnerEntity, fieldTypeEntity, true);
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerId(int fieldOwnerId) {
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        return timeEnableRepository.findByFieldOwnerIdAndStatus(fieldOwnerEntity, true);
    }

    public List<TimeEnableEntity> findTimeEnableByDateInWeek(String dateInWeek) {
        return timeEnableRepository.findByDateInWeekAndStatus(dateInWeek, true);
    }

    public TimeEnableEntity convertFromInputTimeEnableDTOToEntity(InputTimeEnableDTO inputTimeEnableDTO, Date effectDate) {
        TimeEnableEntity timeEnableEntity = new TimeEnableEntity();
        timeEnableEntity.setDateInWeek(inputTimeEnableDTO.getDayInWeek());
        timeEnableEntity.setFieldOwnerId(accountServices.findAccountEntityByIdAndRole(inputTimeEnableDTO.getFieldOwnerId(), constant.getFieldOwnerRole()));
        timeEnableEntity.setFieldTypeId(fieldTypeServices.findById(inputTimeEnableDTO.getFieldTypeId()));
        timeEnableEntity.setStartTime(DateTimeUtils.convertFromStringToTime(inputTimeEnableDTO.getStartTime()));
        timeEnableEntity.setEndTime(DateTimeUtils.convertFromStringToTime(inputTimeEnableDTO.getEndTime()));
        timeEnableEntity.setPrice(Float.parseFloat(inputTimeEnableDTO.getPrice()));
        timeEnableEntity.setOptimal(inputTimeEnableDTO.isOptimal());
        timeEnableEntity.setEffectiveDate(effectDate);
        timeEnableEntity.setStatus(true);
        return timeEnableEntity;
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerTypeAndDate(AccountEntity fieldOwner, FieldTypeEntity fieldTypeEntity, String dateInWeek, Boolean optimal) {
        if (optimal != null) {
            return timeEnableRepository.findByFieldOwnerAndTypeAndDateAndDateInWeekAndOptimalOrderByStartTime(fieldOwner, fieldTypeEntity, dateInWeek, optimal, true);
        } else {
            return timeEnableRepository.findByFieldOwnerAndTypeAndDateInWeekOrderByStartTime(fieldOwner, fieldTypeEntity, dateInWeek, true);
        }
    }
}
