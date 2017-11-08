package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.*"})
@PropertySource("classpath:config.properties")
public class Constant {
    @Value("${field.owner.role}")
    private String fieldOwnerRole;

    @Value("${user.role}")
    private String userRole;

    @Value("${admin.role}")
    private String adminRole;

    @Value("${staff.role}")
    private String staffRole;

    @Value("${max.price}")
    private Float maxPrice;

    @Value("${min.price}")
    private Float minPrice;

    @Value("${deviation.ranking}")
    private Integer deviationRanking;

    @Value("${rating.score}")
    private Integer ratingScore;

    @Value("${bonus.point}")
    private Integer bonusPoint;

    @Value("${percent.profit}")
    private Float percentProfit;

    public String getFieldOwnerRole() {
        return fieldOwnerRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public Integer getDeviationRanking() {
        return deviationRanking;
    }

    public Integer getRatingScore() {
        return ratingScore;
    }

    public Integer getBonusPoint() {
        return bonusPoint;
    }

    public Float getPercentProfit() {
        return percentProfit;
    }
}
