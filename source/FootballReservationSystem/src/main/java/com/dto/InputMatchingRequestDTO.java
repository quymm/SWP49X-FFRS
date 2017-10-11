package com.dto;

import java.util.Date;

public class InputMatchingRequestDTO {
    private Integer userId;

    private Integer fieldTypeId;

    private String longitude;

    private String latitude;

    private String date;

    private String startTime;

    private String endTime;

    public InputMatchingRequestDTO() {
    }

    public InputMatchingRequestDTO(Integer userId, Integer fieldTypeId, String longitude, String latitude, String date, String startTime, String endTime) {
        this.userId = userId;
        this.fieldTypeId = fieldTypeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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
