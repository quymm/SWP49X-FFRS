package com.dto;

public class InputReportOpponentDTO {
    private Integer userId;

    private Integer opponentId;

    private Integer tourMatchId;

    private String reason;

    public InputReportOpponentDTO(Integer userId, Integer opponentId, Integer tourMatchId, String reason) {
        this.userId = userId;
        this.opponentId = opponentId;
        this.tourMatchId = tourMatchId;
        this.reason = reason;
    }

    public InputReportOpponentDTO() {
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

    public Integer getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(Integer tourMatchId) {
        this.tourMatchId = tourMatchId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
