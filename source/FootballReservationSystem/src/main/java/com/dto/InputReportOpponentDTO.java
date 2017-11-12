package com.dto;

public class InputReportOpponentDTO {
    private Integer accuserId;

    private Integer accusedId;

    private Integer tourMatchId;

    private String reason;

    public InputReportOpponentDTO() {
    }

    public InputReportOpponentDTO(Integer accuserId, Integer accusedId, Integer tourMatchId, String reason) {
        this.accuserId = accuserId;
        this.accusedId = accusedId;
        this.tourMatchId = tourMatchId;
        this.reason = reason;
    }

    public Integer getAccuserId() {
        return accuserId;
    }

    public void setAccuserId(Integer accuserId) {
        this.accuserId = accuserId;
    }

    public Integer getAccusedId() {
        return accusedId;
    }

    public void setAccusedId(Integer accusedId) {
        this.accusedId = accusedId;
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
