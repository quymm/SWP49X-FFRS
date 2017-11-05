package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/29/2017.
 */

public class FieldOwnerTourNotification {
    private int isRead, isShowed;
    private String time;

    public FieldOwnerTourNotification() {
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsShowed() {
        return isShowed;
    }

    public void setIsShowed(int isShowed) {
        this.isShowed = isShowed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
