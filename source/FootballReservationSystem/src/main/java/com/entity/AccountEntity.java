/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author MinhQuy
 */
@Entity
@Table(name = "account")
public class AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "lock_status")
    private boolean lockStatus;
    @Basic(optional = false)
    @Column(name = "request_lock")
    private boolean requestLock;
    @Basic(optional = false)
    @Column(name = "num_of_report")
    private int numOfReport;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;
    @Basic(optional = false)
    @Column(name = "modification_date")
    @UpdateTimestamp
    private Date modificationDate;
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProfileEntity profileId;
    @JoinColumn(name = "staff_request_id", referencedColumnName = "id")
    @ManyToOne
    private AccountEntity staffRequestId;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RoleEntity roleId;

    public AccountEntity() {
    }

    public AccountEntity(Integer id) {
        this.id = id;
    }

    public AccountEntity(Integer id, String username, String password, boolean lock, boolean status, Date creationDate, Date modificationDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lockStatus = lock;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

    public boolean getRequestLock() {
        return requestLock;
    }

    public void setRequestLock(boolean requestLock) {
        this.requestLock = requestLock;
    }

    public int getNumOfReport() {
        return numOfReport;
    }

    public void setNumOfReport(int numOfReport) {
        this.numOfReport = numOfReport;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public ProfileEntity getProfileId() {
        return profileId;
    }

    public void setProfileId(ProfileEntity profileId) {
        this.profileId = profileId;
    }

    public RoleEntity getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleEntity roleId) {
        this.roleId = roleId;
    }

    public AccountEntity getStaffRequestId() {
        return staffRequestId;
    }

    public void setStaffRequestId(AccountEntity staffRequestId) {
        this.staffRequestId = staffRequestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountEntity)) {
            return false;
        }
        AccountEntity other = (AccountEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.AccountEntity[ id=" + id + " ]";
    }

}
