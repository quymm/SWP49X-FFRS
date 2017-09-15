package com.dto;

/**
 * Created by Minh Quy on 3/6/2017.
 */
public class ProductDTO {
    private Integer id;

    private String productCode;

    private String categoryCode;

    private String name;

    private String detail;

    private Double oldPrice;

    private Double newPrice;

    private Integer numberOfLover;

    private String imageUrl;

    public ProductDTO(String productCode, String categoryCode, String name, String detail, Double oldPrice, Double newPrice, Integer numberOfLover, String imageUrl) {
        this.productCode = productCode;
        this.categoryCode = categoryCode;
        this.name = name;
        this.detail = detail;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.numberOfLover = numberOfLover;
        this.imageUrl = imageUrl;
    }

    public ProductDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getNumberOfLover() {
        return numberOfLover;
    }

    public void setNumberOfLover(Integer numberOfLover) {
        this.numberOfLover = numberOfLover;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
