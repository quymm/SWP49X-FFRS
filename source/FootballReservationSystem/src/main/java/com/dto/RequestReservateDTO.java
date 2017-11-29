package com.dto;

public class RequestReservateDTO {
    private Integer fieldTypeId;

    private String startTime;

    private String endTime;

    private int duration;

    public RequestReservateDTO(Integer fieldTypeId, String startTime, String endTime, int duration) {
        this.fieldTypeId = fieldTypeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public RequestReservateDTO() {
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
