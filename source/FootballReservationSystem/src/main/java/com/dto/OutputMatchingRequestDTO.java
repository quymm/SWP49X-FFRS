package com.dto;

public class OutputMatchingRequestDTO {
    private Integer matchingRequestId;

    private Integer userId;

    public OutputMatchingRequestDTO(Integer matchingRequestId, Integer userId) {
        this.matchingRequestId = matchingRequestId;
        this.userId = userId;
    }

    public OutputMatchingRequestDTO() {
    }

    public Integer getMatchingRequestId() {
        return matchingRequestId;
    }

    public void setMatchingRequestId(Integer matchingRequestId) {
        this.matchingRequestId = matchingRequestId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
