package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/2/2017.
 */

public class FieldTime {
    private String fromTime, toTime;
    private int price;

    public FieldTime(String fromTime, String toTime, int price) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.price = price;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
