package com.dto;

import java.util.Date;

public class InputPromotionDTO {
    private Integer fieldTypeId;

    private Integer fieldOwnerId;

    private String dateFrom;

    private String dateTo;

    private String startTime;

    private String endTime;

    private Float saleOff;

    private String freeServices;

    public InputPromotionDTO(Integer fieldTypeId, Integer fieldOwnerId, String dateFrom, String dateTo, String startTime, String endTime, Float saleOff, String freeServices) {
        this.fieldTypeId = fieldTypeId;
        this.fieldOwnerId = fieldOwnerId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.saleOff = saleOff;
        this.freeServices = freeServices;
    }

    public InputPromotionDTO() {
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public Integer getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(Integer fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
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

    public Float getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(Float saleOff) {
        this.saleOff = saleOff;
    }

    public String getFreeServices() {
        return freeServices;
    }

    public void setFreeServices(String freeServices) {
        this.freeServices = freeServices;
    }
}
