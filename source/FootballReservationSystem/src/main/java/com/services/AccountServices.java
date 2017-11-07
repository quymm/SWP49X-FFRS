package com.services;

import com.config.Constant;
import com.dto.CordinationPoint;
import com.dto.FieldOwnerAndDistance;
import com.dto.InputFieldOwnerDTO;
import com.dto.InputUserDTO;
import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.entity.RoleEntity;
import com.repository.AccountRepository;
import com.repository.ProfileRepository;
import com.utils.MapUtils;
import com.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    @Autowired
    MatchServices matchServices;

    @Autowired
    Constant constant;

    public AccountEntity createNewFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO) {
        RoleEntity roleEntity = roleServices.findByRoleName(constant.getFieldOwnerRole());
        if (accountRepository.findByUsernameAndStatusAndRoleId(inputFieldOwnerDTO.getUsername(), true, roleEntity) != null) {
            throw new DuplicateKeyException(String.format("Username: %s is already exists!", inputFieldOwnerDTO.getUsername()));
        }

        ProfileEntity profileEntity = createProfileEntityForFieldOwner(inputFieldOwnerDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);


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
        profileEntity.setBalance(profileEntity.getBalance());
        profileEntity.setLongitude(inputFieldOwnerDTO.getLongitute());
        profileEntity.setLatitude(inputFieldOwnerDTO.getLatitude());
        profileEntity.setPhone(inputFieldOwnerDTO.getPhone());
        accountEntity.setProfileId(profileRepository.save(profileEntity));

        return accountRepository.save(accountEntity);
    }

    public AccountEntity createNewUser(InputUserDTO inputUserDTO) {
        RoleEntity roleEntity = roleServices.findByRoleName(constant.getUserRole());
        if (accountRepository.findByUsernameAndStatusAndRoleId(inputUserDTO.getUsername(), true, roleEntity) != null) {
            throw new DuplicateKeyException(String.format("Username: %s is already exists!", inputUserDTO.getUsername()));
        }
        ProfileEntity profileEntity = createProfileEntityForUser(inputUserDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);


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

    public AccountEntity checkLogin(String username, String password, String role) {
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        AccountEntity accountEntity = accountRepository.findByUsernamePasswordAndRoleEntity(username, password, roleEntity, true);
        if (accountEntity == null) {
            throw new EntityNotFoundException(String.format("Not found account have username: %s and password: %s", username, password));
        }
        return accountEntity;
    }

    public ProfileEntity createProfileEntityForFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setAddress(inputFieldOwnerDTO.getAddress());
        profileEntity.setAvatarUrl(inputFieldOwnerDTO.getAvatarUrl());
        profileEntity.setBalance(Float.valueOf(0));
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
        profileEntity.setBalance(Float.valueOf(0));
        profileEntity.setAvatarUrl(inputUserDTO.getAvatarUrl());
        profileEntity.setBonusPoint(0);
        profileEntity.setRatingScore(2000);
        profileEntity.setStatus(true);
        return profileEntity;
    }

    public List<AccountEntity> findMax10FieldOwnerNearByPosition(String longitudeStr, String latitudeStr) {
        double longitude = NumberUtils.parseFromStringToDouble(longitudeStr);
        double latitude = NumberUtils.parseFromStringToDouble(latitudeStr);
        CordinationPoint cordinationPointA = new CordinationPoint(longitude, latitude);

        List<AccountEntity> fieldOwnerList = findAccountByRole(constant.getFieldOwnerRole());
        List<FieldOwnerAndDistance> fieldOwnerAndDistanceList = new ArrayList<>();
        List<AccountEntity> returnFieldOwnerList = new ArrayList<>();

        if (!fieldOwnerList.isEmpty() && fieldOwnerList.size() > 1) {
            for (AccountEntity fieldOwner : fieldOwnerList) {
                CordinationPoint cordinationPointB = new CordinationPoint(NumberUtils.parseFromStringToDouble(fieldOwner.getProfileId().getLongitude()),
                        NumberUtils.parseFromStringToDouble(fieldOwner.getProfileId().getLatitude()));
                double distance = MapUtils.calculateDistanceBetweenTwoPoint(cordinationPointA, cordinationPointB);
                FieldOwnerAndDistance fieldOwnerAndDistance = new FieldOwnerAndDistance(fieldOwner, distance);
                fieldOwnerAndDistanceList.add(fieldOwnerAndDistance);
            }

            // sắp xếp theo thứ tự khoảng cách tăng dần
            matchServices.arrangeFieldOwnerByDistance(fieldOwnerAndDistanceList);
            for (int i = 0; i < (fieldOwnerAndDistanceList.size() < 10 ? fieldOwnerAndDistanceList.size() : 10); i++) {
                AccountEntity returnFieldOwner = fieldOwnerAndDistanceList.get(i).getFieldOwner();
                returnFieldOwnerList.add(returnFieldOwner);
            }
            return returnFieldOwnerList;
        }
        return fieldOwnerList;
    }

    public List<AccountEntity> findByNameLikeAndRole(String name, String role) {
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        List<ProfileEntity> profileEntityList = profileRepository.searchByName("%" + name + "%", true);

        List<AccountEntity> returnAccountEntityList = new ArrayList<>();

        for (ProfileEntity profileEntity : profileEntityList) {
            AccountEntity accountEntity = accountRepository.findByProfileIdAndRoleIdAndStatus(profileEntity, roleEntity, true);
            if (accountEntity != null) {
                returnAccountEntityList.add(accountEntity);
            }
        }
        return returnAccountEntityList;
    }

    public AccountEntity changeBalance(int accountId, float balanceNumber, String role) {
        AccountEntity accountEntity = findAccountEntityById(accountId, role);
        ProfileEntity profileEntity = accountEntity.getProfileId();
        if (balanceNumber > 0) {
            profileEntity.setBalance(profileEntity.getBalance() + balanceNumber);
        }
        if (balanceNumber < 0) {
            if (balanceNumber > profileEntity.getBalance()) {
                throw new IllegalArgumentException(String.format("Account balance: %s. Not enough to withdraw!", profileEntity.getBalance()));
            } else {
                profileEntity.setBalance(profileEntity.getBalance() - balanceNumber);
            }
        }
        accountEntity.setProfileId(profileRepository.save(profileEntity));
        return accountEntity;
    }
}
