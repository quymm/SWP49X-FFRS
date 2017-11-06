package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/29/2017.
 */

public class FieldOwnerFriendlyNotification {

    private int isRead, isShowed;
    private String username, time, playTime;

    public FieldOwnerFriendlyNotification() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }
}
