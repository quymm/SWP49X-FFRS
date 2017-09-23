package com.utils;

import com.dto.InputUserDTO;
import com.entity.UserEntity;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public class ConvertUtil {

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
}
