package com.services;

import com.dto.InputFieldOwnerDTO;
import com.entity.FieldOwnerEntity;
import com.repository.FieldOwnerRepository;
import com.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@Service
public class FieldOwnerServices {
    @Autowired
    FieldOwnerRepository fieldOwnerRepository;

    public FieldOwnerEntity createNewFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO){
        FieldOwnerEntity fieldOwnerEntity = ConvertUtil.convertFromInputFieldOwnerDTOToFieldOwnerEntity(inputFieldOwnerDTO);
        FieldOwnerEntity savedFieldOwnerEntity = fieldOwnerRepository.save(fieldOwnerEntity);
        return savedFieldOwnerEntity;
    }

    public FieldOwnerEntity getFieldOwnerEntityByFieldOwnerId(int fieldOwnerId){
        return fieldOwnerRepository.findById(fieldOwnerId);
    }
}
