package com.dto;

/**
 * Created by MinhQuy on 4/13/2017.
 */
public class ErrorDTO {
    private String message;

    private Integer statusCode;

    public ErrorDTO(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public ErrorDTO() {
    }
}
