package com.dto;

public class InputStandardPriceDTO {
    private Integer staffId;

    private Integer fieldTypeId;

    private boolean rushHour;

    private float minPrice;

    private float maxPrice;

    public InputStandardPriceDTO(Integer staffId, Integer fieldTypeId, boolean rushHour, float minPrice, float maxPrice) {
        this.staffId = staffId;
        this.fieldTypeId = fieldTypeId;
        this.rushHour = rushHour;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public InputStandardPriceDTO() {
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public boolean isRushHour() {
        return rushHour;
    }

    public void setRushHour(boolean rushHour) {
        this.rushHour = rushHour;
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
}
