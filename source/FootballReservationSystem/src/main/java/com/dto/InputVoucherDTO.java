package com.dto;

public class InputVoucherDTO {

    private float voucherValue;
    private int bonusPointTarget;

    public InputVoucherDTO() {
    }

    public InputVoucherDTO(float voucherValue, int bonusPointTarget) {
        this.voucherValue = voucherValue;
        this.bonusPointTarget = bonusPointTarget;
    }

    public float getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(float voucherValue) {
        this.voucherValue = voucherValue;
    }

    public int getBonusPointTarget() {
        return bonusPointTarget;
    }

    public void setBonusPointTarget(int bonusPointTarget) {
        this.bonusPointTarget = bonusPointTarget;
    }
}
