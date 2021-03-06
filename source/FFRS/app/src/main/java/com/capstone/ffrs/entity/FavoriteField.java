package com.capstone.ffrs.entity;

/**
 * Created by HuanPMSE61860 on 9/29/2017.
 */

public class FavoriteField {

    private String imgURL, fieldName, address;
    private int id, fieldId;

    public FavoriteField(int id, String fieldName, String address, String imgURL) {
        this.id = id;
        this.imgURL = imgURL;
        this.fieldName = fieldName;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
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