package com.dto;

public class InputTeamMemberDTO {
    private Integer captainId;

    private String playerName;

    private String phone;

    private String longitude;

    private String latitude;

    private String address;

    public InputTeamMemberDTO(Integer captainId, String playerName, String phone) {
        this.captainId = captainId;
        this.playerName = playerName;
        this.phone = phone;
    }

    public InputTeamMemberDTO() {
    }

    public Integer getCaptainId() {
        return captainId;
    }

    public void setCaptainId(Integer captainId) {
        this.captainId = captainId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
