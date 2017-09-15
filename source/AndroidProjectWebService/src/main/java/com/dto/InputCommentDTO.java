package com.dto;

import java.util.Date;

/**
 * Created by Minh Quy on 3/6/2017.
 */
public class InputCommentDTO {

    private String productCode;

    private String title;

    private String detail;

    private String date;

    private String rate;

    public InputCommentDTO() {
    }

    public InputCommentDTO(String productCode, String title, String detail, String date, String rate) {
        this.productCode = productCode;
        this.title = title;
        this.detail = detail;
        this.date = date;
        this.rate = rate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
