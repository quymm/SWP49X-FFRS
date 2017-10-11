package com.dto;

public class InputRatingOpponentDTO {
    private boolean win;

    private int ratingScore;

    private boolean status;

    private Integer tourMatchId;

    private Integer userId;

    private Integer opponentId;

    public InputRatingOpponentDTO() {
    }

    public InputRatingOpponentDTO(boolean win, int ratingScore, boolean status, Integer tourMatchId, Integer userId, Integer opponentId) {
        this.win = win;
        this.ratingScore = ratingScore;
        this.status = status;
        this.tourMatchId = tourMatchId;
        this.userId = userId;
        this.opponentId = opponentId;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(Integer tourMatchId) {
        this.tourMatchId = tourMatchId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(Integer opponentId) {
        this.opponentId = opponentId;
    }
}
