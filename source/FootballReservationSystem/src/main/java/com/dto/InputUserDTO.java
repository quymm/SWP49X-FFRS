package com.dto;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public class InputUserDTO {
    private String username;

    private String password;

    private String teamName;

    private String phone;

    private String creditCard;

    private String avatarUrl;

    public InputUserDTO() {
    }

    public InputUserDTO(String username, String password, String teamName, String phone, String creditCard, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.teamName = teamName;
        this.phone = phone;
        this.creditCard = creditCard;
        this.avatarUrl = avatarUrl;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
