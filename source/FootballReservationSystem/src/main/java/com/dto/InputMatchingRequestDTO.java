package com.dto;

import java.util.Date;

public class InputMatchingRequestDTO {
    private Integer userId;

    private Integer fieldTypeId;

    private String longitude;

    private String latitude;

    private String address;

    private String date;

    private String startTime;

    private String endTime;

    private Integer duration;

    private Integer expectedDistance;

    public InputMatchingRequestDTO() {
    }

    public InputMatchingRequestDTO(Integer userId, Integer fieldTypeId, String longitude, String latitude, String date, String startTime, String endTime, Integer duration) {
        this.userId = userId;
        this.fieldTypeId = fieldTypeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Integer getExpectedDistance() {
        return expectedDistance;
    }

    public void setExpectedDistance(Integer expectedDistance) {
        this.expectedDistance = expectedDistance;
    }
}
