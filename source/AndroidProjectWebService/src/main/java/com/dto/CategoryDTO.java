package com.dto;

/**
 * Created by Minh Quy on 3/6/2017.
 */
public class CategoryDTO {
    private Integer id;
    private String categoryCode;
    private String name;
    private String detail;
    private String squareImageUrl;
    private String recImageUrl;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryCode, String name, String detail, String squareImageUrl, String recImageUrl) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.detail = detail;
        this.squareImageUrl = squareImageUrl;
        this.recImageUrl = recImageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSquareImageUrl() {
        return squareImageUrl;
    }

    public void setSquareImageUrl(String squareImageUrl) {
        this.squareImageUrl = squareImageUrl;
    }

    public String getRecImageUrl() {
        return recImageUrl;
    }

    public void setRecImageUrl(String recImageUrl) {
        this.recImageUrl = recImageUrl;
    }
}
