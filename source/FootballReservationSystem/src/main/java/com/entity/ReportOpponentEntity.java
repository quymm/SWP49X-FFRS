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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "report_opponent")
@XmlRootElement
public class ReportOpponentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "reason")
    private String reason;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "opponent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity opponentId;
    @JoinColumn(name = "tour_match_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TourMatchEntity tourMatchId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity userId;

    public ReportOpponentEntity() {
    }

    public ReportOpponentEntity(Integer id) {
        this.id = id;
    }

    public ReportOpponentEntity(Integer id, String reason, boolean status) {
        this.id = id;
        this.reason = reason;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AccountEntity getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(AccountEntity opponentId) {
        this.opponentId = opponentId;
    }

    public TourMatchEntity getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(TourMatchEntity tourMatchId) {
        this.tourMatchId = tourMatchId;
    }

    public AccountEntity getUserId() {
        return userId;
    }

    public void setUserId(AccountEntity userId) {
        this.userId = userId;
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
        if (!(object instanceof ReportOpponentEntity)) {
            return false;
        }
        ReportOpponentEntity other = (ReportOpponentEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.ReportOpponentEntity[ id=" + id + " ]";
    }
    
}
