package com.dto;

public class InputReserveTimeSlotDTO {
    private Integer fieldOwnerId;

    private Integer fieldTypeId;

    private String date;

    private String startTime;

    private String endTime;

    public InputReserveTimeSlotDTO(Integer fieldOwnerId, Integer fieldTypeId, String date, String startTime, String endTime) {
        this.fieldOwnerId = fieldOwnerId;
        this.fieldTypeId = fieldTypeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public InputReserveTimeSlotDTO() {
    }

    public Integer getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(Integer fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
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
