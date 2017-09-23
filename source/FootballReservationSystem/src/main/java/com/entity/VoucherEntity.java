/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    @Column(name = "percent_sale_off")
    private float percentSaleOff;
    @Basic(optional = false)
    @Column(name = "bonus_point_target")
    private int bonusPointTarget;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;

    public VoucherEntity() {
    }

    public VoucherEntity(Integer id) {
        this.id = id;
    }

    public VoucherEntity(Integer id, float percentSaleOff, int bonusPointTarget, boolean status) {
        this.id = id;
        this.percentSaleOff = percentSaleOff;
        this.bonusPointTarget = bonusPointTarget;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getPercentSaleOff() {
        return percentSaleOff;
    }

    public void setPercentSaleOff(float percentSaleOff) {
        this.percentSaleOff = percentSaleOff;
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