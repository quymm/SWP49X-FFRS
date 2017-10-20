package com.services;

import com.entity.AccountEntity;
import com.entity.BillEntity;
import com.entity.FriendlyMatchEntity;
import com.entity.TourMatchEntity;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BillServices {
    @Autowired
    AccountServices accountServices;

    @Autowired
    FriendlyMatchRepository friendlyMatchRepository;

    @Autowired
    TourMatchRepository tourMatchRepository;

    @Autowired
    VoucherServices voucherServices;

    @Autowired
    BillRepository billRepository;


    public BillEntity findByUserIdAndFriendlyMatchIdAndTourMatchId(int userId, int friendlyMatchId, int tourMatchId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
        FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByIdAndStatus(friendlyMatchId, true);
        TourMatchEntity tourMatchEntity = tourMatchRepository.findByIdAndStatus(tourMatchId, true);
        return billRepository.findByUserIdAndFriendlyMatchIdAndTourMatchIdAndStatus(accountEntity, friendlyMatchEntity, tourMatchEntity, true);
    }

//    public BillEntity createBill(int userId, int friendlyMatchId, int tourMatchId){
//        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
//        FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByIdAndStatus(friendlyMatchId, true);
//        TourMatchEntity tourMatchEntity = tourMatchRepository.findByIdAndStatus(tourMatchId, true);
//        return billRepository.findByUserIdAndFriendlyMatchIdAndTourMatchIdAndStatus(accountEntity, friendlyMatchEntity, tourMatchEntity, true);
//    }
}
