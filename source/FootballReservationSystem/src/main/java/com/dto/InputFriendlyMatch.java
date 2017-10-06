package com.dto;

import java.util.Date;

public class InputFriendlyMatch {
    private int fieldOwnerId;

    private int fieldTypeId;

    private int userId;

    private Date date;

    private Date startTime;

    private int duration;

    public InputFriendlyMatch(int fieldOwnerId, int fieldTypeId, int userId, Date date, Date startTime, int duration) {
        this.fieldOwnerId = fieldOwnerId;
        this.fieldTypeId = fieldTypeId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    public InputFriendlyMatch() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
