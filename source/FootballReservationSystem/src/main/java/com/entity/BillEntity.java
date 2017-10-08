/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "bill")
public class BillEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date_charge")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCharge;
    @Basic(optional = false)
    @Column(name = "price")
    private float price;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity userId;
    @JoinColumn(name = "friendly_match_id", referencedColumnName = "id")
    @ManyToOne
    private FriendlyMatchEntity friendlyMatchId;
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    @ManyToOne
    private VoucherEntity voucherId;
    @JoinColumn(name = "tour_match_id", referencedColumnName = "id")
    @ManyToOne
    private TourMatchEntity tourMatchId;

    public BillEntity() {
    }

    public BillEntity(Integer id) {
        this.id = id;
    }

    public BillEntity(Integer id, Date dateCharge, float price, boolean status) {
        this.id = id;
        this.dateCharge = dateCharge;
        this.price = price;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateCharge() {
        return dateCharge;
    }

    public void setDateCharge(Date dateCharge) {
        this.dateCharge = dateCharge;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AccountEntity getUserId() {
        return userId;
    }

    public void setUserId(AccountEntity userId) {
        this.userId = userId;
    }

    public FriendlyMatchEntity getFriendlyMatchId() {
        return friendlyMatchId;
    }

    public void setFriendlyMatchId(FriendlyMatchEntity friendlyMatchId) {
        this.friendlyMatchId = friendlyMatchId;
    }

    public VoucherEntity getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(VoucherEntity voucherId) {
        this.voucherId = voucherId;
    }

    public TourMatchEntity getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(TourMatchEntity tourMatchId) {
        this.tourMatchId = tourMatchId;
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
        if (!(object instanceof BillEntity)) {
            return false;
        }
        BillEntity other = (BillEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.BillEntity[ id=" + id + " ]";
    }
    
}
