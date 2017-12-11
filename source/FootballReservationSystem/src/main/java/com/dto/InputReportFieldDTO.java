package com.dto;

public class InputReportFieldDTO {
    private Integer accuserId;

    private Integer accusedId;

    private String reason;

    public InputReportFieldDTO() {
    }

    public InputReportFieldDTO(Integer accuserId, Integer accusedId, String reason) {
        this.accuserId = accuserId;
        this.accusedId = accusedId;
        this.reason = reason;
    }

    public Integer getAccuserId() {
        return accuserId;
    }

    public void setAccuserId(Integer accuserId) {
        this.accuserId = accuserId;
    }

    public Integer getAccusedId() {
        return accusedId;
    }

    public void setAccusedId(Integer accusedId) {
        this.accusedId = accusedId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
