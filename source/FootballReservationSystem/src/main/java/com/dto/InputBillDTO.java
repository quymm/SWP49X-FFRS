package com.dto;

import com.entity.FriendlyMatchEntity;
import com.entity.TourMatchEntity;
import com.entity.VoucherEntity;

import java.util.Date;

public class InputBillDTO {
    private Integer friendlyMatchId;
    private Integer tourMatchId;
    private boolean opponentPayment;

    public InputBillDTO() {
    }

    public InputBillDTO(Integer friendlyMatchId, Integer tourMatchId) {
        this.friendlyMatchId = friendlyMatchId;
        this.tourMatchId = tourMatchId;
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

    public boolean isOpponentPayment() {
        return opponentPayment;
    }

    public void setOpponentPayment(boolean opponentPayment) {
        this.opponentPayment = opponentPayment;
    }
}
