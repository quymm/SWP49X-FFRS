package com.dto;

import java.util.Date;

public class InputMatchingRequestDTO {
    private Integer userId;

    private Integer fieldTypeId;

    private String longitude;

    private String latitude;

    private Date startTime;

    private Integer duration;

    public InputMatchingRequestDTO(Integer userId, Integer fieldTypeId, String longitude, String latitude, Date startTime, Integer duration) {
        this.userId = userId;
        this.fieldTypeId = fieldTypeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.duration = duration;
    }

    public InputMatchingRequestDTO() {
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
