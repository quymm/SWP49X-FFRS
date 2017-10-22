package com.dto;

import java.util.Date;

public class InputBillDTO {
    private String dateCharge;
    private Integer userId;
    private Integer friendlyMatchId;
    private Integer voucherId;
    private Integer tourMatchId;

    public InputBillDTO() {
    }

    public InputBillDTO(String dateCharge, Integer userId, Integer friendlyMatchId, Integer voucherId, Integer tourMatchId) {
        this.dateCharge = dateCharge;
        this.userId = userId;
        this.friendlyMatchId = friendlyMatchId;
        this.voucherId = voucherId;
        this.tourMatchId = tourMatchId;
    }

    public String getDateCharge() {
        return dateCharge;
    }

    public void setDateCharge(String dateCharge) {
        this.dateCharge = dateCharge;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendlyMatchId() {
        return friendlyMatchId;
    }

    public void setFriendlyMatchId(Integer friendlyMatchId) {
        this.friendlyMatchId = friendlyMatchId;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(Integer tourMatchId) {
        this.tourMatchId = tourMatchId;
    }
}
