package com.dto;

public class InputVoucherRecordDTO {

    private Integer userId;

    private Integer voucherId;

    public InputVoucherRecordDTO() {
    }

    public InputVoucherRecordDTO(Integer userId, Integer voucherId) {
        this.userId = userId;
        this.voucherId = voucherId;
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
