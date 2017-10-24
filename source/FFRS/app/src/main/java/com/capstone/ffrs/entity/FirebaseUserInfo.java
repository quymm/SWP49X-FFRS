package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 10/24/2017.
 */

public class FirebaseUserInfo {
    private int status;
    private double latitude, longitude;

    public FirebaseUserInfo() {
    }

    public FirebaseUserInfo(int status, double latitude, double longitude) {
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
