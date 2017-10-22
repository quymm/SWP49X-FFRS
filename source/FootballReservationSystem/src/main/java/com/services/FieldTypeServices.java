package com.services;

import com.dto.InputFieldTypeDTO;
import com.entity.FieldTypeEntity;
import com.repository.FieldTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@Service
public class FieldTypeServices {
    @Autowired
    FieldTypeRepository fieldTypeRepository;

    public FieldTypeEntity createNewFieldType(InputFieldTypeDTO inputFieldTypeDTO){
        FieldTypeEntity fieldTypeEntity = convertFromInputFieldTypeDTOToFieldTypeEntity(inputFieldTypeDTO);
        return fieldTypeRepository.save(fieldTypeEntity);
    }

    public FieldTypeEntity findById(int fieldTypeId){
        FieldTypeEntity fieldTypeEntity = fieldTypeRepository.findByIdAndStatus(fieldTypeId, true);
        if (fieldTypeEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Field Type have id = %s", fieldTypeId));
        }
        return fieldTypeEntity;
    }

    public FieldTypeEntity deleteFieldType(int fieldTypeId){
        FieldTypeEntity fieldTypeEntity = findById(fieldTypeId);
        fieldTypeEntity.setStatus(false);
        return fieldTypeRepository.save(fieldTypeEntity);
    }

    public FieldTypeEntity convertFromInputFieldTypeDTOToFieldTypeEntity(InputFieldTypeDTO inputFieldTypeDTO){
        FieldTypeEntity fieldTypeEntity = new FieldTypeEntity();
        fieldTypeEntity.setName(inputFieldTypeDTO.getName());
        fieldTypeEntity.setNumberPlayer(Integer.parseInt(inputFieldTypeDTO.getNumberPlayer()));
        fieldTypeEntity.setStatus(true);
        return fieldTypeEntity;
    }

    public List<FieldTypeEntity> findAllFieldType(){
        return fieldTypeRepository.findAllByStatus(true);
    }
}
