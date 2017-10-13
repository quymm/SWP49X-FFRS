package com.dto;

public class InputVoucherRecordDTO {

    private boolean status;

    private Integer userId;

    private Integer voucherId;

    public InputVoucherRecordDTO() {
    }

    public InputVoucherRecordDTO(boolean status, Integer userId, Integer voucherId) {
        this.status = status;
        this.userId = userId;
        this.voucherId = voucherId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }
}
