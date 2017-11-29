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
import java.util.Date;
import java.util.List;

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

    public List<TimeEnableEntity> setUpTimeEnable(List<InputTimeEnableDTO> inputTimeEnableDTOList) {
        int fieldOwnerId = inputTimeEnableDTOList.get(0).getFieldOwnerId();
        int fieldTypeId = inputTimeEnableDTOList.get(0).getFieldTypeId();
        String dateInWeek = inputTimeEnableDTOList.get(0).getDayInWeek();
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        Date currDate = new Date();
        String sCurrDate = DateTimeUtils.formatDate(currDate);
        int numDayFromNow = -1;

        for (int i = 6; i >= 0; i--) {
            Date targetDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(sCurrDate, i));
            if (!DateTimeUtils.returnDayInWeek(targetDate).equals(dateInWeek)) {
                continue;
            }
            List<TimeSlotEntity> freeTimeSlotEntityList = timeSlotServices.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndOptimal(fieldOwnerEntity, fieldTypeEntity, targetDate, false, null);
            List<TimeSlotEntity> timeSlotEntityList = timeSlotServices.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndOptimal(fieldOwnerEntity, fieldTypeEntity, targetDate, true, null);
            if (!timeSlotEntityList.isEmpty()) {
                numDayFromNow = i;
                break;
            } else {
                // xóa time slot free của ngày hôm đó nếu đã tạo rồi
                if (!freeTimeSlotEntityList.isEmpty())
                    timeSlotRepository.delete(freeTimeSlotEntityList);
            }
        }
        // targetdate là ngày cuối cùng time enable cũ còn hoạt động
        Date targetDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(sCurrDate, numDayFromNow));
        List<TimeEnableEntity> oldTimeEnableEntityList = findByFieldOwnerAndTypeAndDateInWeekAndOptimalAndTargetDateOrderByStartTime(fieldOwnerEntity, fieldTypeEntity, dateInWeek, targetDate, null);
        for (TimeEnableEntity timeEnableEntity : oldTimeEnableEntityList) {
            timeEnableEntity.setDateTo(targetDate);
        }
        // update old time enable entity
        timeEnableRepository.save(oldTimeEnableEntityList);

        // ngày time enable mới có hiệu lực
        Date newDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(sCurrDate, numDayFromNow + 1));

        // kiểm tra có thể ngày hôm đó user đã thực hiện update 1 lần thì xóa dữ liệu đó đi
        List<TimeEnableEntity> sameDayTimeEnableEntityList = findByFieldOwnerAndTypeAndDateInWeekAndOptimalAndTargetDateOrderByStartTime(fieldOwnerEntity, fieldTypeEntity, dateInWeek, newDate, null);
        timeEnableRepository.delete(sameDayTimeEnableEntityList);

        List<TimeEnableEntity> newTimeEnableEntityList = new ArrayList<>();

        for (InputTimeEnableDTO inputTimeEnableDTO : inputTimeEnableDTOList) {
            // time enable entity mới có ngày hiệu lực là sau ngày cuối cùng time enable cũ còn hoạt động 1 ngày
            TimeEnableEntity timeEnableEntity = convertFromInputTimeEnableDTOToEntity(inputTimeEnableDTO, newDate);
            newTimeEnableEntityList.add(timeEnableEntity);
        }

        return timeEnableRepository.save(newTimeEnableEntityList);
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

    public TimeEnableEntity convertFromInputTimeEnableDTOToEntity(InputTimeEnableDTO inputTimeEnableDTO, Date targetDate) {
        TimeEnableEntity timeEnableEntity = new TimeEnableEntity();
        timeEnableEntity.setDateInWeek(inputTimeEnableDTO.getDayInWeek());
        timeEnableEntity.setFieldOwnerId(accountServices.findAccountEntityByIdAndRole(inputTimeEnableDTO.getFieldOwnerId(), constant.getFieldOwnerRole()));
        timeEnableEntity.setFieldTypeId(fieldTypeServices.findById(inputTimeEnableDTO.getFieldTypeId()));
        timeEnableEntity.setStartTime(DateTimeUtils.convertFromStringToTime(inputTimeEnableDTO.getStartTime()));
        timeEnableEntity.setEndTime(DateTimeUtils.convertFromStringToTime(inputTimeEnableDTO.getEndTime()));
        timeEnableEntity.setPrice(Float.parseFloat(inputTimeEnableDTO.getPrice()));
        timeEnableEntity.setOptimal(inputTimeEnableDTO.isOptimal());
        timeEnableEntity.setDateFrom(targetDate);
        timeEnableEntity.setStatus(true);
        return timeEnableEntity;
    }


    public List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndOptimalAndTargetDateOrderByStartTime(AccountEntity fieldOwner, FieldTypeEntity fieldTypeEntity, String dateInWeek, Date targetDate, Boolean optimal) {
        List<TimeEnableEntity> returnTimeEnableEntity;
        if (optimal != null) {
            List<TimeEnableEntity> timeEnableEntityList = timeEnableRepository.findByFieldOwnerAndTypeAndDateInWeekAndOptimalAndTargetDateOrderByStartTime(fieldOwner, fieldTypeEntity, dateInWeek, targetDate, optimal, true);
            returnTimeEnableEntity = returnTimeEnableEntity(timeEnableEntityList, targetDate);
        } else {
            List<TimeEnableEntity> timeEnableEntityList = timeEnableRepository.findByFieldOwnerAndTypeAndDateInWeekAndTargetDateOrderByStartTime(fieldOwner, fieldTypeEntity, dateInWeek, targetDate, true);
            returnTimeEnableEntity = returnTimeEnableEntity(timeEnableEntityList, targetDate);
        }
        return returnTimeEnableEntity;
    }

    public List<TimeEnableEntity> returnTimeEnableEntity(List<TimeEnableEntity> inputTimeEnableEntity, Date targetDate) {
        List<TimeEnableEntity> returnTimeEnableEntityList = new ArrayList<>();
        if (!inputTimeEnableEntity.isEmpty()) {
            for (TimeEnableEntity timeEnableEntity : inputTimeEnableEntity) {
                if (timeEnableEntity.getDateTo() == null) {
                    returnTimeEnableEntityList.add(timeEnableEntity);
                } else {
                    if (!timeEnableEntity.getDateTo().before(targetDate)) {
                        returnTimeEnableEntityList.add(timeEnableEntity);
                    }
                }
            }
        }
        return returnTimeEnableEntityList;
    }

}
