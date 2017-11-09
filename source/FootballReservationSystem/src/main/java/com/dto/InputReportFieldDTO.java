package com.dto;

public class InputReportFieldDTO {
    private Integer userId;

    private Integer fieldOwnerId;

    private String reason;

    public InputReportFieldDTO(Integer userId, Integer fieldOwnerId, String reason) {
        this.userId = userId;
        this.fieldOwnerId = fieldOwnerId;
        this.reason = reason;
    }

    public InputReportFieldDTO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(Integer fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
