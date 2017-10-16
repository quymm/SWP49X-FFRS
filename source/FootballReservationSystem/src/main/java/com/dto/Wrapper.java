package com.dto;

/**
 * @author MinhQuy
 */
public class Wrapper {
    private Object body;

    private Integer status;

    private String message;

    public Wrapper(Object body, Integer status, String message) {
        this.body = body;
        this.status = status;
        this.message = message;
    }

    public Wrapper() {
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
