package com.services;

import com.dto.InputFieldTypeDTO;
import com.entity.FieldTypeEntity;
import com.repository.FieldTypeRepository;
import com.utils.ConvertUtil;
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
        FieldTypeEntity fieldTypeEntity = ConvertUtil.convertFromInputFieldTypeDTOToFieldTypeEntity(inputFieldTypeDTO);
        return fieldTypeRepository.save(fieldTypeEntity);
    }

    public FieldTypeEntity getFieldTypeEntityByFieldTypeId(int fieldTypeId){
        return fieldTypeRepository.findById(fieldTypeId);
    }
}
