package com.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Minh Quy on 3/6/2017.
 */
@Entity
@Table(name = "category")
public class CategoryEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "category_code", unique = true)
    private String categoryCode;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "square_image_url")
    private String squareImageUrl;

    @Column(name = "rec_image_url")
    private String recImageUrl;

    public CategoryEntity() {
    }

    public CategoryEntity(String categoryCode, String name, String detail, String squareImageUrl, String recImageUrl) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.detail = detail;
        this.squareImageUrl = squareImageUrl;
        this.recImageUrl = recImageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
