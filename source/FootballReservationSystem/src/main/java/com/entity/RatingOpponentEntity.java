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
@Table(name = "rating_opponent")
public class RatingOpponentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "rating_score")
    private int ratingScore;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "tour_match_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TourMatchEntity tourMatchId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserEntity userId;
    @JoinColumn(name = "opponent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserEntity opponentId;

    public RatingOpponentEntity() {
    }

    public RatingOpponentEntity(Integer id) {
        this.id = id;
    }

    public RatingOpponentEntity(Integer id, int ratingScore, boolean status) {
        this.id = id;
        this.ratingScore = ratingScore;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TourMatchEntity getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(TourMatchEntity tourMatchId) {
        this.tourMatchId = tourMatchId;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
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
        if (!(object instanceof RatingOpponentEntity)) {
            return false;
        }
        RatingOpponentEntity other = (RatingOpponentEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RatingOpponentEntity[ id=" + id + " ]";
    }
    
}
