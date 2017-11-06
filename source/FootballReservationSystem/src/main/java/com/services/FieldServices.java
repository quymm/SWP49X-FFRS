package com.services;

import com.config.Constant;
import com.dto.InputFieldDTO;
import com.entity.AccountEntity;
import com.entity.FieldEntity;
import com.entity.FieldTypeEntity;
import com.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@Service
public class FieldServices {
    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    Constant constant;

    public FieldEntity createNewField(InputFieldDTO inputFieldDTO) {
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(inputFieldDTO.getFieldTypeId());
        AccountEntity accountEntity = accountServices.findAccountEntityById(inputFieldDTO.getFieldOwnerId(), constant.getFieldOwnerRole());
        FieldEntity fieldEntity = fieldRepository.findByFieldOwnerIdAndFieldTypeIdAndNameAndStatus(accountEntity, fieldTypeEntity, inputFieldDTO.getFieldName(), false);
        if (fieldEntity != null) {
            fieldEntity.setStatus(true);
        } else {
            fieldEntity = new FieldEntity();
            fieldEntity.setName(inputFieldDTO.getFieldName());
            fieldEntity.setFieldOwnerId(accountEntity);
            fieldEntity.setFieldTypeId(fieldTypeEntity);
            fieldEntity.setStatus(true);
        }
        FieldEntity savedFieldEntity = fieldRepository.save(fieldEntity);
        timeSlotServices.addTimeSlotWhenCreateNewField(savedFieldEntity.getFieldOwnerId(), savedFieldEntity.getFieldTypeId());

        return fieldRepository.save(fieldEntity);
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerId(int fieldOwnerId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole());
        return fieldRepository.findByFieldOwnerIdAndStatus(accountEntity, true);
    }

    public FieldEntity findFieldEntityByFieldNameAndFieldOwnerId(String fieldName, int fieldOwnerId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole());
        return fieldRepository.findByFieldOwnerIdAndNameAndStatus(accountEntity, fieldName, true);
    }

    public FieldEntity findFieldEntityById(int fieldId) {
        return fieldRepository.findByIdAndStatus(fieldId, true);
    }

    public FieldEntity deleteFieldEntity(int fieldId) {
        FieldEntity fieldEntity = findFieldEntityById(fieldId);
        fieldEntity.setStatus(false);
        return fieldRepository.save(fieldEntity);
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType){
        return fieldRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(fieldOwner, fieldType, true);
    }

    public Integer countNumberOfFieldByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType){
        return fieldRepository.countByFieldOwnerIdAndAndFieldTypeIdAndStatus(fieldOwner, fieldType, true);
    }

}
