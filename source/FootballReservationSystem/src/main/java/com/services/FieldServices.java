package com.services;

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

    public FieldEntity createNewField(InputFieldDTO inputFieldDTO) {
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(inputFieldDTO.getFieldTypeId());
        AccountEntity accountEntity = accountServices.findAccountEntityById(inputFieldDTO.getFieldOwnerId());
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setName(inputFieldDTO.getFieldName());
        fieldEntity.setFieldOwnerId(accountEntity);
        fieldEntity.setFieldTypeId(fieldTypeEntity);
        fieldEntity.setStatus(true);
        return fieldRepository.save(fieldEntity);
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerId(int fieldOwnerId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId);
        return fieldRepository.findByFieldOwnerIdAndStatus(accountEntity, true);
    }

    public FieldEntity findFieldEntityByFieldNameAndFieldOwnerId(String fieldName, int fieldOwnerId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId);
        return fieldRepository.findByFieldOwnerIdAndNameAndStatus(accountEntity, fieldName, true);
    }

}
