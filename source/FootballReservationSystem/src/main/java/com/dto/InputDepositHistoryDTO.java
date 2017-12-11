package com.dto;

public class InputDepositHistoryDTO {
    private Integer accountId;

    private Float balance;

    private String information;

    private String role;

    public InputDepositHistoryDTO(Integer accountId, Float balance, String information, String role) {
        this.accountId = accountId;
        this.balance = balance;
        this.information = information;
        this.role = role;
    }

    public InputDepositHistoryDTO() {
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
