package com.dto;

public class InputRatingOpponentDTO {

    private Integer userId;

    private Integer tourMatchId;

    private Integer result;

    private Integer ratingLevel;

    private Integer goalsDifference;

    public InputRatingOpponentDTO() {
    }

    public InputRatingOpponentDTO(Integer userId, Integer tourMatchId, Integer result, Integer ratingLevel, Integer goalsDifference) {
        this.userId = userId;
        this.tourMatchId = tourMatchId;
        this.result = result;
        this.ratingLevel = ratingLevel;
        this.goalsDifference = goalsDifference;
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

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(Integer ratingLevel) {
        this.ratingLevel = ratingLevel;
    }

    public Integer getGoalsDifference() {
        return goalsDifference;
    }

    public void setGoalsDifference(Integer goalsDifference) {
        this.goalsDifference = goalsDifference;
    }
}
