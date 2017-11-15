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
@Table(name = "rating_opponent")
public class RatingOpponentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "result")
    private Integer result;
    @Basic(optional = false)
    @Column(name = "rating_level")
    private Integer ratingLevel;
    @Column(name = "goals_difference")
    private Integer goalsDifference;
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
    @JoinColumn(name = "tour_match_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TourMatchEntity tourMatchId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity userId;

    public RatingOpponentEntity() {
    }

    public RatingOpponentEntity(Integer id) {
        this.id = id;
    }

    public RatingOpponentEntity(Integer result, Integer ratingLevel, Integer goalsDifference, TourMatchEntity tourMatchId, AccountEntity userId) {
        this.result = result;
        this.ratingLevel = ratingLevel;
        this.goalsDifference = goalsDifference;
        this.tourMatchId = tourMatchId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(Integer ratingLevel) {
        this.ratingLevel = ratingLevel;
    }

    public Integer getGoalsDifference() {
        return goalsDifference;
    }

    public void setGoalsDifference(Integer goalsDifference) {
        this.goalsDifference = goalsDifference;
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
