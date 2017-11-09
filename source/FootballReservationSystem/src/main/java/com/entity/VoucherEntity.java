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
@Table(name = "voucher")
public class VoucherEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "voucher_value")
    private float voucherValue;
    @Basic(optional = false)
    @Column(name = "bonus_point_target")
    private int bonusPointTarget;
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

    public VoucherEntity() {
    }

    public VoucherEntity(Integer id) {
        this.id = id;
    }

    public VoucherEntity(Integer id, float voucherValue, int bonusPointTarget, boolean status) {
        this.id = id;
        this.voucherValue = voucherValue;
        this.bonusPointTarget = bonusPointTarget;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(float voucherValue) {
        this.voucherValue = voucherValue;
    }

    public int getBonusPointTarget() {
        return bonusPointTarget;
    }

    public void setBonusPointTarget(int bonusPointTarget) {
        this.bonusPointTarget = bonusPointTarget;
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
        if (!(object instanceof VoucherEntity)) {
            return false;
        }
        VoucherEntity other = (VoucherEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.VoucherEntity[ id=" + id + " ]";
    }
    
}
