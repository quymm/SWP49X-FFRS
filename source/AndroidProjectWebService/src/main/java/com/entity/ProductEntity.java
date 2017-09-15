package com.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Minh Quy on 3/6/2017.
 */
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "product_code", unique = true)
    private String productCode;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "old_price")
    private Double oldPrice;

    @Column(name = "new_price")
    private Double newPrice;

    @Column(name = "number_of_lover")
    private int numberOfLover;

    @Column(name = "image_url")
    private String imageUrl;

    public ProductEntity(String productCode, String categoryCode, String name, String detail, Double oldPrice, Double newPrice, int numberOfLover, String imageUrl) {
        this.productCode = productCode;
        this.categoryCode = categoryCode;
        this.name = name;
        this.detail = detail;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.numberOfLover = numberOfLover;
        this.imageUrl = imageUrl;
    }

    public ProductEntity() {
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

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public int getNumberOfLover() {
        return numberOfLover;
    }

    public void setNumberOfLover(int numberOfLover) {
        this.numberOfLover = numberOfLover;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
