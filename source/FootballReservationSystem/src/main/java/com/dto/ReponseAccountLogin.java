package com.dto;

import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.entity.RoleEntity;

import java.util.Date;

/**
 * Created by truonghuuthanh on 11/22/17.
 */
public class ReponseAccountLogin {
    int id;
    String username;
    int numOfReport;
    boolean lockStatus;
    boolean requestLock;
    boolean status;
    AccountEntity staffRequestId;
    Date creationDate;
    Date modificationDate;
    RoleEntity roleId;
    ProfileEntity profileId;
    String tokenHeader;
    String tokenValue;

    public ReponseAccountLogin() {
    }

    public ReponseAccountLogin(AccountEntity accountEntity, String tokenHeader, String tokenValue  ) {
        this.id = accountEntity.getId();
        this.username = accountEntity.getUsername();
        this.numOfReport = accountEntity.getNumOfReport();
        this.lockStatus = accountEntity.getLockStatus();
        this.requestLock = accountEntity.getRequestLock();
        this.status = accountEntity.getStatus();
        this.staffRequestId = accountEntity.getStaffRequestId();
        this.creationDate = accountEntity.getCreationDate();
        this.modificationDate = accountEntity.getModificationDate();
        this.roleId = accountEntity.getRoleId();
        this.profileId = accountEntity.getProfileId();
        this.tokenHeader = tokenHeader;
        this.tokenValue = tokenValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumOfReport() {
        return numOfReport;
    }

    public void setNumOfReport(int numOfReport) {
        this.numOfReport = numOfReport;
    }

    public boolean isLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

    public boolean isRequestLock() {
        return requestLock;
    }

    public void setRequestLock(boolean requestLock) {
        this.requestLock = requestLock;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AccountEntity getStaffRequestId() {
        return staffRequestId;
    }

    public void setStaffRequestId(AccountEntity staffRequestId) {
        this.staffRequestId = staffRequestId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public RoleEntity getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleEntity roleId) {
        this.roleId = roleId;
    }

    public ProfileEntity getProfileId() {
        return profileId;
    }

    public void setProfileId(ProfileEntity profileId) {
        this.profileId = profileId;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}


