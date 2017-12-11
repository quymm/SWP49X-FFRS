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
@Table(name = "tour_match")
public class TourMatchEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "complete_status")
    private boolean completeStatus;
    @Basic(optional = false)
    @Column(name = "winner_id")
    private int winnerId;
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
    @JoinColumn(name = "time_slot_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TimeSlotEntity timeSlotId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity userId;
    @JoinColumn(name = "opponent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity opponentId;

    public TourMatchEntity() {
    }

    public TourMatchEntity(Integer id) {
        this.id = id;
    }

    public TourMatchEntity(Integer id, boolean completeStatus, int winnerId, boolean status) {
        this.id = id;
        this.completeStatus = completeStatus;
        this.winnerId = winnerId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(boolean completeStatus) {
        this.completeStatus = completeStatus;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
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

    public TimeSlotEntity getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(TimeSlotEntity timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public AccountEntity getUserId() {
        return userId;
    }

    public void setUserId(AccountEntity userId) {
        this.userId = userId;
    }

    public AccountEntity getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(AccountEntity opponentId) {
        this.opponentId = opponentId;
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
        if (!(object instanceof TourMatchEntity)) {
            return false;
        }
        TourMatchEntity other = (TourMatchEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TourMatchEntity[ id=" + id + " ]";
    }
    
}
