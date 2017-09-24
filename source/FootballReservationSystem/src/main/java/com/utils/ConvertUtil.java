package com.utils;

import com.dto.InputFieldDTO;
import com.dto.InputFieldOwnerDTO;
import com.dto.InputFieldTypeDTO;
import com.dto.InputUserDTO;
import com.entity.FieldEntity;
import com.entity.FieldOwnerEntity;
import com.entity.FieldTypeEntity;
import com.entity.UserEntity;
import com.services.FieldOwnerServices;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public class ConvertUtil {
    @Autowired
    FieldOwnerServices fieldOwnerServices;

    public static UserEntity convertFromInputUserDTOToUserEntity(InputUserDTO inputUserDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(inputUserDTO.getUsername());
        userEntity.setPassword(inputUserDTO.getPassword());
        userEntity.setTeamName(inputUserDTO.getTeamName());
        userEntity.setPhone(inputUserDTO.getPhone());
        userEntity.setCreditCard(inputUserDTO.getCreditCard());
        userEntity.setBonusPoint(0);
        userEntity.setRatingScore(2000);
        userEntity.setStatus(true);
        return userEntity;
    }

    public static FieldOwnerEntity convertFromInputFieldOwnerDTOToFieldOwnerEntity(InputFieldOwnerDTO inputFieldOwnerDTO){
        FieldOwnerEntity fieldOwnerEntity = new FieldOwnerEntity();
        fieldOwnerEntity.setUsername(inputFieldOwnerDTO.getUsername());
        fieldOwnerEntity.setPassword(inputFieldOwnerDTO.getPassword());
        fieldOwnerEntity.setAddress(inputFieldOwnerDTO.getAddress());
        fieldOwnerEntity.setLongitude(inputFieldOwnerDTO.getLongitute());
        fieldOwnerEntity.setLatitude(inputFieldOwnerDTO.getLatitude());
        fieldOwnerEntity.setCreditCard(inputFieldOwnerDTO.getCreditCard());
        fieldOwnerEntity.setProfitsCommission(3);
        fieldOwnerEntity.setStatus(true);
        return fieldOwnerEntity;
    }

    public static FieldTypeEntity convertFromInputFieldTypeDTOToFieldTypeEntity(InputFieldTypeDTO inputFieldTypeDTO){
        FieldTypeEntity fieldTypeEntity = new FieldTypeEntity();
        fieldTypeEntity.setName(inputFieldTypeDTO.getName());
        fieldTypeEntity.setNumberPlayer(5);
        fieldTypeEntity.setDescription(inputFieldTypeDTO.getDescription());
        fieldTypeEntity.setStatus(true);
        return fieldTypeEntity;
    }

}
