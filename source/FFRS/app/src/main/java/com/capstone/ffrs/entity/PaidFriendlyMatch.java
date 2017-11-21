package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/30/2017.
 */

public class PaidFriendlyMatch {
    private int id, userId, fieldId, fieldTypeId;
    private String date, startTime, endTime;
    private String fieldName;

    public PaidFriendlyMatch() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
