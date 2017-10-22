package com.capstone.ffrs.entity;

import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/17/2017.
 */

public class TourMatch {
    List<Integer> userIdList;

    public TourMatch() {
    }

    public TourMatch(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }
}
