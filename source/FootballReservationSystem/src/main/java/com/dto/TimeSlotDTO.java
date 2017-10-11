package com.dto;

import java.util.Date;

/**
 * @author MinhQuy
 */
public class TimeSlotDTO {
    private Date startTime;

    private Date endTime;

    private Float price;

    public TimeSlotDTO() {
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
