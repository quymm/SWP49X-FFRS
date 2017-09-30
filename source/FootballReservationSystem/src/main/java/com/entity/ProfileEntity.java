/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "profile")
@XmlRootElement
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
    private String longitude;
    @Column(name = "latitude")
    private String latitude;
    @Basic(optional = false)
    @Column(name = "creadit_card")
    private String creaditCard;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "rating_score")
    private Integer ratingScore;
    @Column(name = "bonus_point")
    private Integer bonusPoint;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;

    public ProfileEntity() {
    }

    public ProfileEntity(Integer id) {
        this.id = id;
    }

    public ProfileEntity(Integer id, String name, String phone, String creaditCard, boolean status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.creaditCard = creaditCard;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCreaditCard() {
        return creaditCard;
    }

    public void setCreaditCard(String creaditCard) {
        this.creaditCard = creaditCard;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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