package com.services;

import com.dto.InputFieldTypeDTO;
import com.entity.FieldTypeEntity;
import com.repository.FieldTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@Service
public class FieldTypeServices {
    @Autowired
    FieldTypeRepository fieldTypeRepository;

    public FieldTypeEntity createNewFieldTypeEntity(InputFieldTypeDTO inputFieldTypeDTO){
        FieldTypeEntity fieldTypeEntity = convertFromInputFieldTypeDTOToFieldTypeEntity(inputFieldTypeDTO);
        return fieldTypeRepository.save(fieldTypeEntity);
    }

    public FieldTypeEntity findFieldTypeEntityById(int fieldTypeId){
        return fieldTypeRepository.findByIdAndStatus(fieldTypeId, true);
    }

    public FieldTypeEntity convertFromInputFieldTypeDTOToFieldTypeEntity(InputFieldTypeDTO inputFieldTypeDTO){
        FieldTypeEntity fieldTypeEntity = new FieldTypeEntity();
        fieldTypeEntity.setName(inputFieldTypeDTO.getName());
        fieldTypeEntity.setNumberPlayer(Integer.parseInt(inputFieldTypeDTO.getNumberPlayer()));
        fieldTypeEntity.setStatus(true);
        return fieldTypeEntity;
    }
}
