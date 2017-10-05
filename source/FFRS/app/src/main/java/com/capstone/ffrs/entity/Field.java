package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 9/29/2017.
 */

public class Field {

    private String imgURL, fieldName, address;

    public Field(String fieldName, String address, String imgURL) {
        this.imgURL = imgURL;
        this.fieldName = fieldName;
        this.address = address;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}