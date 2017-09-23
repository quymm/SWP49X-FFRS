/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
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

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "tour_match_temp")
public class TourMatchTempEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "confirm_status")
    private boolean confirmStatus;
    @JoinColumn(name = "matching_request_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MatchingRequestEntity matchingRequestId;
    @JoinColumn(name = "time_slot_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TimeSlotEntity timeSlotId;
    @JoinColumn(name = "opponent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserEntity opponentId;

    public TourMatchTempEntity() {
    }

    public TourMatchTempEntity(Integer id) {
        this.id = id;
    }

    public TourMatchTempEntity(Integer id, boolean confirmStatus) {
        this.id = id;
        this.confirmStatus = confirmStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(boolean confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public MatchingRequestEntity getMatchingRequestId() {
        return matchingRequestId;
    }

    public void setMatchingRequestId(MatchingRequestEntity matchingRequestId) {
        this.matchingRequestId = matchingRequestId;
    }

    public TimeSlotEntity getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(TimeSlotEntity timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public UserEntity getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(UserEntity opponentId) {
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
        if (!(object instanceof TourMatchTempEntity)) {
            return false;
        }
        TourMatchTempEntity other = (TourMatchTempEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TourMatchTempEntity[ id=" + id + " ]";
    }
    
}
