package com.dto;

public class InputStandardPriceDTO {
    private Integer staffId;

    private float minPrice;

    private float maxPrice;

    private Integer standardPriceId;

    public InputStandardPriceDTO() {
    }

    public InputStandardPriceDTO(Integer staffId, float minPrice, float maxPrice, Integer standardPriceId) {
        this.staffId = staffId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.standardPriceId = standardPriceId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getStandardPriceId() {
        return standardPriceId;
    }

    public void setStandardPriceId(Integer standardPriceId) {
        this.standardPriceId = standardPriceId;
    }
}

