package com.services;

import com.dto.InputUserDTO;
import com.entity.UserEntity;
import com.repository.UserRepository;
import com.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MinhQuy on 9/23/2017.
 */
@Service
public class UserServices {
    @Autowired
    UserRepository userRepository;

    public UserEntity createNewUser(InputUserDTO inputUserDTO) {
        UserEntity userEntity = ConvertUtil.convertFromInputUserDTOToUserEntity(inputUserDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity;
    }

    public UserEntity getUserEntityByUserName(String username){
        UserEntity userEntity = userRepository.findUserEntityByUsername(username);
        return userEntity;
    }
}
