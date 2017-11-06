/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author MinhQuy
 */
@Entity
@Table(name = "blacklist_opponent")
public class BlacklistOpponentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "opponent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity opponentId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity userId;

    public BlacklistOpponentEntity() {
    }

    public BlacklistOpponentEntity(Integer id) {
        this.id = id;
    }

    public BlacklistOpponentEntity(Integer id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public AccountEntity getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(AccountEntity opponentId) {
        this.opponentId = opponentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BlacklistOpponentEntity)) {
            return false;
        }
        BlacklistOpponentEntity other = (BlacklistOpponentEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.BlacklistOpponentEntity[ id=" + id + " ]";
    }
    
}