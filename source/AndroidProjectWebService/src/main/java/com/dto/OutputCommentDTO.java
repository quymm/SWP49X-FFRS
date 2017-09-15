package com.dto;

import java.util.Date;

/**
 * Created by Minh Quy on 3/7/2017.
 */
public class OutputCommentDTO {
    private int id;

    private String productCode;

    private String title;

    private String detail;

    private Date date;

    private int rate;

    public OutputCommentDTO() {
    }

    public OutputCommentDTO(int id, String productCode, String title, String detail, Date date, int rate) {
        this.id = id;
        this.productCode = productCode;
        this.title = title;
        this.detail = detail;
        this.date = date;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
