package com.dto;

import com.entity.MatchingRequestEntity;

import java.util.List;

public class OutputMatchingRequestDTO {
    private Integer matchingRequestId;

    private List<MatchingRequestEntity> similarMatchingRequestList;

    public OutputMatchingRequestDTO(Integer matchingRequestId, List<MatchingRequestEntity> similarMatchingRequestList) {
        this.matchingRequestId = matchingRequestId;
        this.similarMatchingRequestList = similarMatchingRequestList;
    }

    public OutputMatchingRequestDTO() {
    }

    public Integer getMatchingRequestId() {
        return matchingRequestId;
    }

    public void setMatchingRequestId(Integer matchingRequestId) {
        this.matchingRequestId = matchingRequestId;
    }

    public List<MatchingRequestEntity> getSimilarMatchingRequestList() {
        return similarMatchingRequestList;
    }

    public void setSimilarMatchingRequestList(List<MatchingRequestEntity> similarMatchingRequestList) {
        this.similarMatchingRequestList = similarMatchingRequestList;
    }
}
