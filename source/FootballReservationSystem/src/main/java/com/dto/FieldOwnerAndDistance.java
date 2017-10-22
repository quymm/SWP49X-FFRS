package com.dto;

import com.entity.AccountEntity;

/**
 * @author MinhQuy
 */
public class FieldOwnerAndDistance {
    AccountEntity fieldOwner;

    Integer distance;

    public FieldOwnerAndDistance(AccountEntity fieldOwner, Integer distance) {
        this.fieldOwner = fieldOwner;
        this.distance = distance;
    }

    public FieldOwnerAndDistance() {
    }

    public AccountEntity getFieldOwner() {
        return fieldOwner;
    }

    public void setFieldOwner(AccountEntity fieldOwner) {
        this.fieldOwner = fieldOwner;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }


}
