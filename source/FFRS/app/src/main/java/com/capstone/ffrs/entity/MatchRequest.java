package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/11/2017.
 */

public class MatchRequest {
    private int id, ratingScore;
    private String date, startTime, endTime;
    private String teamName;

    public MatchRequest() {

    }

    public MatchRequest(int id, int ratingScore, String date, String startTime, String endTime, String teamName) {
        this.id = id;
        this.ratingScore = ratingScore;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
