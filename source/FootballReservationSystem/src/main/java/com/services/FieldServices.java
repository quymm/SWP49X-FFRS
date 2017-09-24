package com.services;

import com.dto.InputFieldDTO;
import com.entity.FieldEntity;
import com.entity.FieldOwnerEntity;
import com.entity.FieldTypeEntity;
import com.repository.FieldOwnerRepository;
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
    FieldOwnerServices fieldOwnerServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    public FieldEntity createNewField(InputFieldDTO inputFieldDTO) {
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.getFieldTypeEntityByFieldTypeId(inputFieldDTO.getFieldTypeId());
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerServices.getFieldOwnerEntityByFieldOwnerId(inputFieldDTO.getFieldOwnerId());
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setFieldOwnerId(fieldOwnerEntity);
        fieldEntity.setFieldTypeId(fieldTypeEntity);
        fieldEntity.setName(inputFieldDTO.getFieldName());
        fieldEntity.setStatus(true);
        return fieldRepository.save(fieldEntity);
    }

    public List<FieldEntity> getFieldEntityByFieldOwnerId(int fieldOwnerId){
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerServices.getFieldOwnerEntityByFieldOwnerId(fieldOwnerId);
        return fieldRepository.getFieldEntitiesByFieldOwnerId(fieldOwnerEntity);
    }



}
