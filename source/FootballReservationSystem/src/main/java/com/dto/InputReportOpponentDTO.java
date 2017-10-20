package com.dto;

import com.entity.AccountEntity;
import com.entity.TourMatchEntity;

public class InputReportOpponentDTO {
    private String reason;
    private boolean status;
    private Integer tourMatchId;
    private Integer userId;
    private Integer opponentId;

    public InputReportOpponentDTO() {
    }

    public InputReportOpponentDTO(String reason, Integer tourMatchId, Integer userId, Integer opponentId) {
        this.reason = reason;
        this.tourMatchId = tourMatchId;
        this.userId = userId;
        this.opponentId = opponentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
