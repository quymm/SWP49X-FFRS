package com.entity;

import javafx.beans.DefaultProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "report")
public class ReportEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "accuser_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity accuserId;
    @JoinColumn(name = "accused_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity accusedId;
    @JoinColumn(name = "tour_match_id", referencedColumnName = "id")
    @ManyToOne
    private TourMatchEntity tourMatchId;
    @Basic(optional = false)
    @Column(name = "reason")
    private String reason;
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

    public ReportEntity() {
    }

    public ReportEntity(AccountEntity accuserId, AccountEntity accusedId, TourMatchEntity tourMatchId, String reason, boolean status) {
        this.accuserId = accuserId;
        this.accusedId = accusedId;
        this.tourMatchId = tourMatchId;
        this.reason = reason;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountEntity getAccuserId() {
        return accuserId;
    }

    public void setAccuserId(AccountEntity accuserId) {
        this.accuserId = accuserId;
    }

    public AccountEntity getAccusedId() {
        return accusedId;
    }

    public void setAccusedId(AccountEntity accusedId) {
        this.accusedId = accusedId;
    }

    public TourMatchEntity getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(TourMatchEntity tourMatchId) {
        this.tourMatchId = tourMatchId;
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
}
