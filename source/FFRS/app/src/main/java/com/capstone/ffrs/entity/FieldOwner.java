package com.capstone.ffrs.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HuanPMSE61860 on 9/29/2017.
 */

public class FieldOwner implements Parcelable {

    private String imgURL, fieldName, address;
    private int id, price;

    public FieldOwner() {
    }

    public FieldOwner(Parcel source) {
        id = source.readInt();
        fieldName = source.readString();
        address = source.readString();
        price = source.readInt();
    }

    public FieldOwner(int id, String fieldName, String address, String imgURL) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fieldName);
        dest.writeString(address);
        dest.writeInt(price);
    }

    public static final Creator<FieldOwner> CREATOR = new Creator<FieldOwner>() {
        @Override
        public FieldOwner[] newArray(int size) {
            return new FieldOwner[size];
        }

        @Override
        public FieldOwner createFromParcel(Parcel source) {
            return new FieldOwner(source);
        }
    };

    @Override
    public String toString() {
        return fieldName;
    }
}