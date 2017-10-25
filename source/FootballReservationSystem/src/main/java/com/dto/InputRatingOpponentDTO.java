package com.dto;

public class InputRatingOpponentDTO {

    private Integer userId;

    private Integer tourMatchId;

    private Integer ratingScore;

    private boolean win;

    public InputRatingOpponentDTO() {
    }

    public InputRatingOpponentDTO(Integer userId, Integer tourMatchId, Integer ratingScore, boolean win) {
        this.userId = userId;
        this.tourMatchId = tourMatchId;
        this.ratingScore = ratingScore;
        this.win = win;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(Integer tourMatchId) {
        this.tourMatchId = tourMatchId;
    }

    public Integer getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(Integer ratingScore) {
        this.ratingScore = ratingScore;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
