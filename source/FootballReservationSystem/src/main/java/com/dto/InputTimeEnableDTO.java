package com.dto;

import java.util.Date;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public class InputTimeEnableDTO {
    private Integer fieldTypeId;

    private Integer fieldOwnerId;

    private String dayInWeek;

    private String startTime;

    private String endTime;

    private String price;

    private boolean optimal;

    public InputTimeEnableDTO() {
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

    public String getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(String dayInWeek) {
        this.dayInWeek = dayInWeek;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isOptimal() {
        return optimal;
    }

    public void setOptimal(boolean optimal) {
        this.optimal = optimal;
    }
}
