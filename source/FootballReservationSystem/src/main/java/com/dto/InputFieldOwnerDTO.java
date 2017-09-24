package com.dto;

/**
 * Created by MinhQuy on 9/24/2017.
 */
public class InputFieldOwnerDTO {
    private String username;

    private String password;

    private String address;

    private String longitute;

    private String latitude;

    private String creditCard;

    private String profitCommission;

    public InputFieldOwnerDTO() {
    }

    public InputFieldOwnerDTO(String username, String password, String address, String longitute, String latitude, String creditCard, String profitCommission) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.longitute = longitute;
        this.latitude = latitude;
        this.creditCard = creditCard;
        this.profitCommission = profitCommission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getProfitCommission() {
        return profitCommission;
    }

    public void setProfitCommission(String profitCommission) {
        this.profitCommission = profitCommission;
    }
}
