package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 11/23/2017.
 */

public class TeamMemberInfo {

    private String name, phone, address;

    public TeamMemberInfo() {
    }

    public TeamMemberInfo(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
