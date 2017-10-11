package com.dto;

import com.entity.AccountEntity;

/**
 * @author MinhQuy
 */
public class FieldOwnerAndDistance {
    AccountEntity fieldOwner;

    double distance;

    public FieldOwnerAndDistance(AccountEntity fieldOwner, double distance) {
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
