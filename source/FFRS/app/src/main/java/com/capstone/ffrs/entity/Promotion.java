package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 12/13/2017.
 */

public class Promotion {

    private int saleOff;
    private String freeServices, startDate, endDate, startTime, endTime;

    private FieldOwner field;

    public Promotion() {
    }

    public int getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(int saleOff) {
        this.saleOff = saleOff;
    }

    public String getFreeServices() {
        return freeServices;
    }

    public void setFreeServices(String freeServices) {
        this.freeServices = freeServices;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public FieldOwner getField() {
        return field;
    }

    public void setField(FieldOwner field) {
        this.field = field;
    }
}
