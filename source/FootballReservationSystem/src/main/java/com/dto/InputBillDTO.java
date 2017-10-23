package com.dto;

import com.entity.FriendlyMatchEntity;
import com.entity.TourMatchEntity;
import com.entity.VoucherEntity;

import java.util.Date;

public class InputBillDTO {
    private Integer friendlyMatchId;
    private Integer tourMatchId;
    private Integer voucherId;
    private boolean opponentPayment;

    public InputBillDTO() {
    }

    public InputBillDTO(Integer friendlyMatchId, Integer tourMatchId, Integer voucherId) {
        this.friendlyMatchId = friendlyMatchId;
        this.tourMatchId = tourMatchId;
        this.voucherId = voucherId;
    }

    public Integer getFriendlyMatchId() {
        return friendlyMatchId;
    }

    public void setFriendlyMatchId(Integer friendlyMatchId) {
        this.friendlyMatchId = friendlyMatchId;
    }

    public Integer getTourMatchId() {
        return tourMatchId;
    }

    public void setTourMatchId(Integer tourMatchId) {
        this.tourMatchId = tourMatchId;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public boolean isOpponentPayment() {
        return opponentPayment;
    }

    public void setOpponentPayment(boolean opponentPayment) {
        this.opponentPayment = opponentPayment;
    }
}
