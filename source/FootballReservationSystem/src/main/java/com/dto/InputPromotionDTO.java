package com.dto;

import java.util.Date;

public class InputPromotionDTO {

    private String dateFrom;

    private String dateTo;

    private String startTime;

    private String endTime;

    private float saleOff;

    private String freeServices;

    private boolean status;

    private Integer fieldOwnerId;

    private Integer fieldTypeId;

    public InputPromotionDTO() {
    }

    public InputPromotionDTO(String dateFrom, String dateTo, String startTime, String endTime, float saleOff, String freeServices, Integer fieldOwnerId, Integer fieldTypeId) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.saleOff = saleOff;
        this.freeServices = freeServices;
        this.fieldOwnerId = fieldOwnerId;
        this.fieldTypeId = fieldTypeId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
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

    public float getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(float saleOff) {
        this.saleOff = saleOff;
    }

    public String getFreeServices() {
        return freeServices;
    }

    public void setFreeServices(String freeServices) {
        this.freeServices = freeServices;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
}
