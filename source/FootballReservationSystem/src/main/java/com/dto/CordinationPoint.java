package com.dto;

/**
 * @author MinhQuy
 */
public class CordinationPoint {
    private double longitute;

    private double latitute;

    public CordinationPoint(double longitute, double latitute) {
        this.longitute = longitute;
        this.latitute = latitute;
    }

    public CordinationPoint() {
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }
}
