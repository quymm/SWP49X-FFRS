package com.services;

import com.dto.InputTimeEnableDTO;
import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import com.repository.TimeEnableRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<TimeEnableEntity> setUpTimeEnable(List<InputTimeEnableDTO> inputTimeEnableDTOList) {
        List<TimeEnableEntity> savedTimeEnableEntity = new ArrayList<>();
        for (InputTimeEnableDTO inputTimeEnableDTO : inputTimeEnableDTOList) {
            TimeEnableEntity timeEnableEntity = convertFromInputTimeEnableDTOToEntity(inputTimeEnableDTO);
            savedTimeEnableEntity.add(timeEnableRepository.save(timeEnableEntity));
        }
        return savedTimeEnableEntity;
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerIdAndFieldTypeId(int fieldOwnerId, int fieldTypeId) {
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return timeEnableRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(fieldOwnerEntity, fieldTypeEntity, true);
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerId(int fieldOwnerId) {
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        return timeEnableRepository.findByFieldOwnerIdAndStatus(fieldOwnerEntity, true);
    }

    public List<TimeEnableEntity> findTimeEnableByDateInWeek(String dateInWeek) {
        return timeEnableRepository.findByDateInWeekAndStatus(dateInWeek, true);
    }

    public TimeEnableEntity convertFromInputTimeEnableDTOToEntity(InputTimeEnableDTO inputTimeEnableDTO) {
        TimeEnableEntity timeEnableEntity = new TimeEnableEntity();
        timeEnableEntity.setDateInWeek(inputTimeEnableDTO.getDayInWeek());
        timeEnableEntity.setFieldOwnerId(accountServices.findAccountEntityById(inputTimeEnableDTO.getFieldOwnerId(), "owner"));
        timeEnableEntity.setFieldTypeId(fieldTypeServices.findById(inputTimeEnableDTO.getFieldTypeId()));
        timeEnableEntity.setStartTime(DateTimeUtils.convertFromStringToTime(inputTimeEnableDTO.getStartTime()));
        timeEnableEntity.setEndTime(DateTimeUtils.convertFromStringToTime(inputTimeEnableDTO.getEndTime()));
        timeEnableEntity.setPrice(Float.parseFloat(inputTimeEnableDTO.getPrice()));
        timeEnableEntity.setStatus(true);
        return timeEnableEntity;
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerTypeAndDate(AccountEntity fieldOwner, FieldTypeEntity fieldTypeEntity, String dateInWeek) {
        return timeEnableRepository.findByFieldOwnerAndTypeAndDateAndDateInWeekOrderByStartTime(fieldOwner, fieldTypeEntity, dateInWeek, true);
    }
}
