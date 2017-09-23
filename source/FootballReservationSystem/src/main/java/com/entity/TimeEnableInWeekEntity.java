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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "time_enable_in_week")
public class TimeEnableInWeekEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "week_date")
    private String weekDate;
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
    private FieldOwnerEntity fieldOwnerId;
    @JoinColumn(name = "field_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FieldTypeEntity fieldTypeId;

    public TimeEnableInWeekEntity() {
    }

    public TimeEnableInWeekEntity(Integer id) {
        this.id = id;
    }

    public TimeEnableInWeekEntity(Integer id, String weekDate, Date startTime, Date endTime, float price, boolean status) {
        this.id = id;
        this.weekDate = weekDate;
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

    public String getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
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

    public FieldOwnerEntity getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(FieldOwnerEntity fieldOwnerId) {
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
        if (!(object instanceof TimeEnableInWeekEntity)) {
            return false;
        }
        TimeEnableInWeekEntity other = (TimeEnableInWeekEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TimeEnableInWeekEntity[ id=" + id + " ]";
    }
    
}
