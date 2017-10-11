package com.dto;

import java.util.Date;

public class InputReservationDTO {
    private int fieldOwnerId;

    private int fieldTypeId;

    private String date;

    private String startTime;

    private String endTime;

    public InputReservationDTO() {
    }

    public InputReservationDTO(int fieldOwnerId, int fieldTypeId, String date, String startTime, String endTime) {
        this.fieldOwnerId = fieldOwnerId;
        this.fieldTypeId = fieldTypeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(int fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public int getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(int fieldTypeId) {
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
}
