package com.services;

import com.config.Constant;
import com.dto.*;
import com.entity.AccountEntity;
import com.entity.DepositHistoryEntity;
import com.entity.ProfileEntity;
import com.entity.RoleEntity;
import com.repository.AccountRepository;
import com.repository.DepositHistoryRepository;
import com.repository.ProfileRepository;
import com.utils.DateTimeUtils;
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
    DepositHistoryRepository depositHistoryRepository;

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
        accountEntity.setLockStatus(false);
        accountEntity.setRequestLock(false);
        accountEntity.setNumOfReport(0);
        return accountRepository.save(accountEntity);
    }

    public AccountEntity updateProfileFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO, int fieldOwnerId) {
        AccountEntity accountEntity = accountRepository.findByIdAndStatus(fieldOwnerId, true);
        accountEntity.setPassword(inputFieldOwnerDTO.getPassword());

        ProfileEntity profileEntity = accountEntity.getProfileId();
        profileEntity.setName(inputFieldOwnerDTO.getName());
        profileEntity.setAddress(inputFieldOwnerDTO.getAddress());
        profileEntity.setAvatarUrl(inputFieldOwnerDTO.getAvatarUrl());
        profileEntity.setLongitude(NumberUtils.parseFromStringToDouble(inputFieldOwnerDTO.getLongitute()));
        profileEntity.setLatitude(NumberUtils.parseFromStringToDouble(inputFieldOwnerDTO.getLatitude()));
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
        accountEntity.setLockStatus(false);
        accountEntity.setRequestLock(false);
        accountEntity.setStatus(true);
        accountEntity.setNumOfReport(0);
        return accountRepository.save(accountEntity);
    }

    public List<AccountEntity> findAccountByRole(String role) {
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        return accountRepository.findAllByRoleAndStatus(roleEntity, true);
    }

    public AccountEntity findAccountEntityByIdAndRole(int id, String role) {
        RoleEntity roleEntity = roleServices.findByRoleName(role);
        return accountRepository.findByIdAndRole(id, roleEntity, true);
    }

    public AccountEntity findAccountEntityById(int id) {
        return accountRepository.findByIdAndStatus(id, true);
    }

    public AccountEntity checkLogin(String username, String password) {
        AccountEntity accountEntity = accountRepository.findByUsernameAndPasswordAndStatus(username, password, true);
        if (accountEntity == null) {
            throw new EntityNotFoundException(String.format("Not found account have username: %s and password: %s", username, password));
        }
        if (accountEntity.getLockStatus()) {
            throw new IllegalArgumentException(String.format("Account have username: %s is locked!", accountEntity.getUsername()));
        }
        return accountEntity;
    }

    public AccountEntity lockAccountById(int id) {
        AccountEntity accountEntity = findAccountEntityById(id);
        if (accountEntity.getRequestLock()) {
            accountEntity.setLockStatus(true);
        }
        return accountRepository.save(accountEntity);
    }

    public AccountEntity unLockAccountById(int id) {
        AccountEntity accountEntity = findAccountEntityById(id);
        if (accountEntity.getLockStatus()) {
            accountEntity.setLockStatus(false);
            accountEntity.setRequestLock(false);
        }
        return accountRepository.save(accountEntity);
    }

    public AccountEntity requestLockAccountById(int userId, int staffId) {
        AccountEntity staffEntity = findAccountEntityByIdAndRole(staffId, constant.getStaffRole());
        AccountEntity userEntity = findAccountEntityByIdAndRole(userId, constant.getUserRole());
        userEntity.setRequestLock(true);
        userEntity.setStaffRequestId(staffEntity);
        return accountRepository.save(userEntity);
    }

    public ProfileEntity createProfileEntityForFieldOwner(InputFieldOwnerDTO inputFieldOwnerDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setAddress(inputFieldOwnerDTO.getAddress());
        profileEntity.setAvatarUrl(inputFieldOwnerDTO.getAvatarUrl());
        profileEntity.setBalance(Float.valueOf(0));
        profileEntity.setAccountPayable(Float.valueOf(0));
        profileEntity.setLatitude(NumberUtils.parseFromStringToDouble(inputFieldOwnerDTO.getLatitude()));
        profileEntity.setLongitude(NumberUtils.parseFromStringToDouble(inputFieldOwnerDTO.getLongitute()));
        profileEntity.setName(inputFieldOwnerDTO.getName());
        profileEntity.setPhone(inputFieldOwnerDTO.getPhone());
        profileEntity.setPercentProfit((float) 0.05);
        profileEntity.setStatus(true);
        return profileEntity;
    }

    public List<AccountEntity> findAllAccountWithLockStatus(boolean lockStatus, boolean requestLock) {
        return accountRepository.findAccountEntitiesByLockStatusAndRequestLockAndStatus(lockStatus, requestLock, true);
    }

    public ProfileEntity createProfileEntityForUser(InputUserDTO inputUserDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(inputUserDTO.getTeamName());
        profileEntity.setPhone(inputUserDTO.getPhone());
        profileEntity.setBalance(Float.valueOf(0));
        profileEntity.setAccountPayable(Float.valueOf(0));
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
                CordinationPoint cordinationPointB = new CordinationPoint(fieldOwner.getProfileId().getLongitude(),
                        fieldOwner.getProfileId().getLatitude());
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

    public List<AccountEntity> getFieldOwnerWithDistance(String latitudeStr, String longitudeStr, int distance) {
        double latitude = NumberUtils.parseFromStringToDouble(latitudeStr);
        double longitude = NumberUtils.parseFromStringToDouble(longitudeStr);
        double latDown = latitude - 0.009 * distance;
        double latUp = latitude + 0.009 * distance;
        double longUp = longitude + 0.0091 * distance;
        double longDown = longitude - 0.0091 * distance;
        RoleEntity roleEntity = roleServices.findByRoleName(constant.getFieldOwnerRole());
        List<ProfileEntity> profileEntityList = profileRepository.getByLocationWithLongLatAndDistance(latUp, latDown, longUp, longDown, true);

        List<AccountEntity> returnFieldOwnerList = new ArrayList<>();
        if (!profileEntityList.isEmpty()) {
            for (ProfileEntity profileEntity : profileEntityList) {
                AccountEntity accountEntity = accountRepository.findByProfileIdAndRoleIdAndStatus(profileEntity, roleEntity, true);
                if (accountEntity != null) {
                    returnFieldOwnerList.add(accountEntity);
                }
            }
        }
        return returnFieldOwnerList;
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
        AccountEntity accountEntity = findAccountEntityByIdAndRole(accountId, role);
        ProfileEntity profileEntity = accountEntity.getProfileId();
        if (balanceNumber > 0) {
            profileEntity.setBalance(profileEntity.getBalance() + balanceNumber);
        }
        if (balanceNumber < 0) {
            if (balanceNumber > profileEntity.getBalance()) {
                throw new IllegalArgumentException(String.format("Account balance: %s. Not enough to withdraw!", profileEntity.getBalance()));
            } else {
                profileEntity.setBalance(profileEntity.getBalance() + balanceNumber);
            }
        }
        accountEntity.setProfileId(profileRepository.save(profileEntity));
        return accountEntity;
    }

    public AccountEntity changeAccountPayable(int accountId, float amount) {
        AccountEntity accountEntity = findAccountEntityById(accountId);
        ProfileEntity profileEntity = accountEntity.getProfileId();
        if (amount < 0) {
            profileEntity.setAccountPayable(profileEntity.getAccountPayable() + amount);
        }
        if (amount > 0) {
            if (profileEntity.getBalance() < amount) {
                throw new IllegalArgumentException(String.format("Account balance: %s not enough to debit!", profileEntity.getBalance()));
            } else {
                profileEntity.setAccountPayable(profileEntity.getAccountPayable() + amount);
            }
        }
        accountEntity.setProfileId(profileRepository.save(profileEntity));
        return accountEntity;
    }


    public DepositHistoryEntity depositMoney(InputDepositHistoryDTO inputDepositHistoryDTO) {
        AccountEntity account = findAccountEntityByIdAndRole(inputDepositHistoryDTO.getAccountId(), inputDepositHistoryDTO.getRole());
        DepositHistoryEntity depositHistoryEntity = new DepositHistoryEntity();
        depositHistoryEntity.setUserId(account);
        depositHistoryEntity.setBalance(inputDepositHistoryDTO.getBalance());
        depositHistoryEntity.setInformation(inputDepositHistoryDTO.getInformation());
        depositHistoryEntity.setStatus(true);
        DepositHistoryEntity savedDepositHistoryEntity = depositHistoryRepository.save(depositHistoryEntity);
        changeBalance(inputDepositHistoryDTO.getAccountId(), inputDepositHistoryDTO.getBalance(), inputDepositHistoryDTO.getRole());
        return savedDepositHistoryEntity;
    }

    public List<DepositHistoryEntity> findDepositHistoryByAccountId(int accountId, String role) {
        AccountEntity account = findAccountEntityByIdAndRole(accountId, role);
        return depositHistoryRepository.findByUserIdAndStatus(account, true);
    }

    public AccountEntity createNewStaff(InputStaffDTO inputStaffDTO) {
        RoleEntity roleEntity = roleServices.findByRoleName(constant.getStaffRole());
        if (accountRepository.findByUsernameAndStatusAndRoleId(inputStaffDTO.getUsername(), true, roleEntity) != null) {
            throw new DuplicateKeyException(String.format("Username: %s is already exists!", inputStaffDTO.getUsername()));
        }
        ProfileEntity profileEntity = createProfileForStaff(inputStaffDTO);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(inputStaffDTO.getUsername());
        accountEntity.setPassword(inputStaffDTO.getPassword());
        accountEntity.setProfileId(savedProfileEntity);
        accountEntity.setRoleId(roleEntity);
        accountEntity.setLockStatus(false);
        accountEntity.setRequestLock(false);
        accountEntity.setStatus(true);
        return accountRepository.save(accountEntity);
    }

    public ProfileEntity createProfileForStaff(InputStaffDTO inputStaffDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(inputStaffDTO.getName());
        profileEntity.setAddress(inputStaffDTO.getAddress());
        profileEntity.setPhone(inputStaffDTO.getPhone());
        profileEntity.setAvatarUrl(inputStaffDTO.getAvatarUrl());
        profileEntity.setStatus(true);
        return profileEntity;
    }


}
