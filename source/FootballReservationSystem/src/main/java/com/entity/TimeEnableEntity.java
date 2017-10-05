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
@Table(name = "time_enable")
public class TimeEnableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date_in_week")
    private String dateInWeek;
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Basic(optional = false)
    @Column(name = "price")
    private float price;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "field_owner_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity fieldOwnerId;
    @JoinColumn(name = "field_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FieldTypeEntity fieldTypeId;

    public TimeEnableEntity() {
    }

    public TimeEnableEntity(Integer id) {
        this.id = id;
    }

    public TimeEnableEntity(Integer id, String dateInWeek, Date startTime, Date endTime, float price, boolean status) {
        this.id = id;
        this.dateInWeek = dateInWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateInWeek() {
        return dateInWeek;
    }

    public void setDateInWeek(String dateInWeek) {
        this.dateInWeek = dateInWeek;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public AccountEntity getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(AccountEntity fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public FieldTypeEntity getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(FieldTypeEntity fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
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
        if (!(object instanceof TimeEnableEntity)) {
            return false;
        }
        TimeEnableEntity other = (TimeEnableEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TimeEnableEntity[ id=" + id + " ]";
    }
    
}
