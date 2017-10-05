package com.services;

import com.dto.InputFieldOwnerDTO;
import com.dto.InputUserDTO;
import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.entity.RoleEntity;
import com.repository.AccountRepository;
import com.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class AccountServices {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleServices roleServices;

    public AccountEntity createNewFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO) {
        ProfileEntity profileEntity = createProfileEntityForFieldOwner(inputFieldOwnerDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);

        RoleEntity roleEntity = roleServices.findByRoleName("owner");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(inputFieldOwnerDTO.getUsername());
        accountEntity.setPassword(inputFieldOwnerDTO.getPassword());
        accountEntity.setProfileId(savedProfileEntity);
        accountEntity.setRoleId(roleEntity);
        accountEntity.setStatus(true);
        return accountRepository.save(accountEntity);
    }

    public AccountEntity updateProfileFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO, int fieldOwnerId) {
        AccountEntity accountEntity = accountRepository.findByIdAndStatus(fieldOwnerId, true);
        accountEntity.setPassword(inputFieldOwnerDTO.getPassword());

        ProfileEntity profileEntity = accountEntity.getProfileId();
        profileEntity.setName(inputFieldOwnerDTO.getName());
        profileEntity.setAddress(inputFieldOwnerDTO.getAddress());
        profileEntity.setAvatarUrl(inputFieldOwnerDTO.getAvatarUrl());
        profileEntity.setCreaditCard(inputFieldOwnerDTO.getCreditCard());
        profileEntity.setLongitude(inputFieldOwnerDTO.getLongitute());
        profileEntity.setLatitude(inputFieldOwnerDTO.getLatitude());
        profileEntity.setPhone(inputFieldOwnerDTO.getPhone());
        accountEntity.setProfileId(profileRepository.save(profileEntity));

        return accountRepository.save(accountEntity);
    }

    public AccountEntity createNewUser(InputUserDTO inputUserDTO) {
        ProfileEntity profileEntity = createProfileEntityForUser(inputUserDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);

        RoleEntity roleEntity = roleServices.findByRoleName("user");

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setUsername(inputUserDTO.getUsername());
        accountEntity.setPassword(inputUserDTO.getPassword());
        accountEntity.setProfileId(savedProfileEntity);
        accountEntity.setRoleId(roleEntity);
        accountEntity.setStatus(true);
        return accountRepository.save(accountEntity);
    }

    public List<AccountEntity> findAccountByRole(String role) {
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        return accountRepository.findAllByRoleAndStatus(roleEntity, true);
    }

    public AccountEntity findAccountEntityById(int id, String role) {
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        return accountRepository.findByIdAndRole(id, roleEntity, true);
    }

    public AccountEntity checkLogin(String username, String password, String role){
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        return accountRepository.findByUsernamePasswordAndRoleEntity(username, password, roleEntity, true);
    }

    public ProfileEntity createProfileEntityForFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO) {
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

    public ProfileEntity createProfileEntityForUser(InputUserDTO inputUserDTO) {
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
