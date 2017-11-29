package com.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "standard_price")
public class StandardPriceEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "rush_hour")
    private boolean rushHour;
    @Basic(optional = false)
    @Column(name = "max_price")
    private Float maxPrice;
    @Basic(optional = false)
    @Column(name = "min_price")
    private Float minPrice;
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
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountEntity staffId;
    @JoinColumn(name = "field_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FieldTypeEntity fieldTypeId;

    public StandardPriceEntity(boolean rushHour, Float maxPrice, Float minPrice, Date creationDate, Date modificationDate, AccountEntity staffId) {
        this.rushHour = rushHour;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.staffId = staffId;
    }

    public StandardPriceEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isRushHour() {
        return rushHour;
    }

    public void setRushHour(boolean rushHour) {
        this.rushHour = rushHour;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
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

    public AccountEntity getStaffId() {
        return staffId;
    }

    public void setStaffId(AccountEntity staffId) {
        this.staffId = staffId;
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
        if (!(object instanceof StandardPriceEntity)) {
            return false;
        }
        StandardPriceEntity other = (StandardPriceEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.StandardPriceEntity[ id=" + id + " ]";
    }
}
