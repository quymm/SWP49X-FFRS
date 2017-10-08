package com.dto;

import java.util.Date;

public class InputReservationDTO {
    private int fieldOwnerId;

    private int fieldTypeId;

    private int userId;

    private String date;

    private String startTime;

    private int duration;

    public InputReservationDTO() {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
