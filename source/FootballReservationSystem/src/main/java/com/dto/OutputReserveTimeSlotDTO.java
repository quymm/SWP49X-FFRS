package com.dto;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;

public class OutputReserveTimeSlotDTO {
    private AccountEntity fieldOwnerId;

    private FieldTypeEntity fieldTypeId;

    private String date;

    private String startTime;

    private String endTime;

    private Float price;

    public OutputReserveTimeSlotDTO(AccountEntity fieldOwnerId, FieldTypeEntity fieldTypeId, String date, String startTime, String endTime, Float price) {
        this.fieldOwnerId = fieldOwnerId;
        this.fieldTypeId = fieldTypeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public OutputReserveTimeSlotDTO() {
    }

    public AccountEntity getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(AccountEntity fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public FieldTypeEntity getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(FieldTypeEntity fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
