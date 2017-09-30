package com.dto;

import java.util.Date;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public class InputTimeEnableDTO {
    private Integer fieldTypeId;

    private Integer fieldOwnerId;

    private String dayInWeek;

    private Date startTime;

    private Date endTime;

    private Float price;

    public InputTimeEnableDTO(Integer fieldTypeId, Integer fieldOwnerId, String dayInWeek, Date startTime, Date endTime, Float price) {
        this.fieldTypeId = fieldTypeId;
        this.fieldOwnerId = fieldOwnerId;
        this.dayInWeek = dayInWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
