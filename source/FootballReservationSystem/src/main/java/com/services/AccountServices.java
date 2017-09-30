package com.services;

import com.dto.InputFieldOwnerDTO;
import com.dto.InputUserDTO;
import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.repository.AccountRepository;
import com.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class AccountServices {
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    AccountRepository accountRepository;

    public AccountEntity createNewFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO){
        ProfileEntity profileEntity = createProfileEntityForFieldOwner(inputFieldOwnerDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);
        AccountEntity accountEntity = new AccountEntity(inputFieldOwnerDTO.getUsername(), inputFieldOwnerDTO.getPassword(),
                "owner", true, savedProfileEntity);
        return accountRepository.save(accountEntity);
    }

    public AccountEntity createNewUser(InputUserDTO inputUserDTO){
        ProfileEntity profileEntity = createProfileEntityForUser(inputUserDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);
        AccountEntity accountEntity = new AccountEntity(inputUserDTO.getUsername(), inputUserDTO.getPassword(),
                "user", true, savedProfileEntity);
        return accountRepository.save(accountEntity);
    }

    public AccountEntity findAccountEntityById(int id){
        return accountRepository.findByIdAndStatus(id, true);
    }

    public ProfileEntity createProfileEntityForFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO){
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setAddress(inputFieldOwnerDTO.getAddress());
        profileEntity.setAvatarUrl(inputFieldOwnerDTO.getAvatarUrl());
        profileEntity.setCreaditCard(inputFieldOwnerDTO.getCreditCard());
        profileEntity.setLatitude(inputFieldOwnerDTO.getLatitude());
        profileEntity.setLongitude(inputFieldOwnerDTO.getLongitute());
        profileEntity.setName(inputFieldOwnerDTO.getName());
        profileEntity.setPhone(inputFieldOwnerDTO.getPhone());
        profileEntity.setStatus(true);
        return profileEntity;
    }

    public ProfileEntity createProfileEntityForUser(InputUserDTO inputUserDTO){
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(inputUserDTO.getTeamName());
        profileEntity.setPhone(inputUserDTO.getPhone());
        profileEntity.setCreaditCard(inputUserDTO.getCreditCard());
        profileEntity.setAvatarUrl(inputUserDTO.getAvatarUrl());
        profileEntity.setBonusPoint(0);
        profileEntity.setRatingScore(2000);
        profileEntity.setStatus(true);
        return profileEntity;
    }




}
