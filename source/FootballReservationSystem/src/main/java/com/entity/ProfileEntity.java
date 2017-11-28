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
import javax.persistence.*;

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "profile")
public class ProfileEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Basic(optional = false)
    @Column(name = "balance")
    private float balance;
    @Basic(optional = false)
    @Column(name = "account_payable")
    private float accountPayable;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "rating_score")
    private Integer ratingScore;
    @Column(name = "bonus_point")
    private Integer bonusPoint;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "percent_profit")
    private Float percentProfit;
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

    public ProfileEntity() {
    }

    public ProfileEntity(Integer id) {
        this.id = id;
    }

    public ProfileEntity(Integer id, String name, String phone, boolean status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getAccountPayable() {
        return accountPayable;
    }

    public void setAccountPayable(float accountPayable) {
        this.accountPayable = accountPayable;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(Integer ratingScore) {
        this.ratingScore = ratingScore;
    }

    public Integer getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(Integer bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public Float getPercentProfit() {
        return percentProfit;
    }

    public void setPercentProfit(Float percentProfit) {
        this.percentProfit = percentProfit;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfileEntity)) {
            return false;
        }
        ProfileEntity other = (ProfileEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.ProfileEntity[ id=" + id + " ]";
    }
    
}
