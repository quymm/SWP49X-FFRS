package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 11/4/2017.
 */

public class PendingRequest {
    private int matchingRequestId;
    private String date, startTime, endTime;
    private int duration;

    public PendingRequest() {
    }

    public int getMatchingRequestId() {
        return matchingRequestId;
    }

    public void setMatchingRequestId(int matchingRequestId) {
        this.matchingRequestId = matchingRequestId;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
