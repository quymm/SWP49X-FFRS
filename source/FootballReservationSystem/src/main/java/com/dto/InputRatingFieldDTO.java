package com.dto;

public class InputRatingFieldDTO {
    private Integer userId;

    private Integer fieldOwnerId;

    private Integer ratingScore;

    private String comment;

    public InputRatingFieldDTO(Integer userId, Integer fieldOwnerId, Integer ratingScore, String comment) {
        this.userId = userId;
        this.fieldOwnerId = fieldOwnerId;
        this.ratingScore = ratingScore;
        this.comment = comment;
    }

    public InputRatingFieldDTO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(Integer fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public Integer getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(Integer ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
