package com.services;

import com.dto.InputTimeEnableDTO;
import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import com.repository.TimeEnableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TimeEnableEntity setUpTimeEnable(InputTimeEnableDTO inputTimeEnableDTO){
        TimeEnableEntity timeEnableEntity = convertFromInputTimeEnableDTOToEntity(inputTimeEnableDTO);
        return timeEnableRepository.save(timeEnableEntity);
    }

    public List<TimeEnableEntity> findTimeEnableByFieldOwnerIdAndFieldTypeId(int fieldOwnerId, int fieldTypeId){
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityById(fieldOwnerId);
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(fieldTypeId);
        return timeEnableRepository.findByFieldOwnerIdAndAndFieldTypeIdAndStatus(fieldOwnerEntity, fieldTypeEntity, true);
    }

    public TimeEnableEntity convertFromInputTimeEnableDTOToEntity(InputTimeEnableDTO inputTimeEnableDTO){
        TimeEnableEntity timeEnableEntity = new TimeEnableEntity();
        timeEnableEntity.setDateInWeek(inputTimeEnableDTO.getDayInWeek());
        timeEnableEntity.setFieldOwnerId(accountServices.findAccountEntityById(inputTimeEnableDTO.getFieldOwnerId()));
        timeEnableEntity.setFieldTypeId(fieldTypeServices.findFieldTypeEntityById(inputTimeEnableDTO.getFieldTypeId()));
        timeEnableEntity.setStartTime(inputTimeEnableDTO.getStartTime());
        timeEnableEntity.setEndTime(inputTimeEnableDTO.getEndTime());
        timeEnableEntity.setPrice(inputTimeEnableDTO.getPrice());
        timeEnableEntity.setStatus(true);
        return timeEnableEntity;
    }
}
