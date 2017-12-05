package com.dto;

/**
 * Created by truonghuuthanh on 10/15/17.
 */
public class Wapper {
    public Object body;
    public int status;
    public String message;

    public Wapper(Object body, int status, String message) {
        this.body = body;
        this.status = status;
        this.message = message;
    }

    public Wapper() {
    }
}
