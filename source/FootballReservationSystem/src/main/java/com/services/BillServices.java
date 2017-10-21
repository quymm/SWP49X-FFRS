package com.services;

import com.dto.InputBillDTO;
import com.entity.*;
import com.repository.BillRepository;
import com.repository.FriendlyMatchRepository;
import com.repository.TourMatchRepository;
import com.repository.VoucherRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BillServices {
    @Autowired
    BillRepository billRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FriendlyMatchRepository friendlyMatchRepository;

    @Autowired
    TourMatchRepository tourMatchRepository;

    @Autowired
    VoucherRepository voucherRepository;


    public BillEntity createBill(InputBillDTO inputBillDTO){
        BillEntity billEntity = new BillEntity();

        AccountEntity accountEntity = accountServices.findAccountEntityById(inputBillDTO.getUserId(), "user");
        billEntity.setUserId(accountEntity);

        Float price = new Float(0.0);
        if (inputBillDTO.getFriendlyMatchId()!= null){
            FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByIdAndStatus(inputBillDTO.getFriendlyMatchId(), true);
            billEntity.setFriendlyMatchId(friendlyMatchEntity);
            TimeSlotEntity timeSlotEntity = friendlyMatchEntity.getTimeSlotId();
            price = timeSlotEntity.getPrice();
        }
        if (inputBillDTO.getTourMatchId()!= null){
            TourMatchEntity tourMatchEntity = tourMatchRepository.findByIdAndStatus(inputBillDTO.getTourMatchId(), true);
            billEntity.setTourMatchId(tourMatchEntity);
            TimeSlotEntity timeSlotEntity = tourMatchEntity.getTimeSlotId();
            price = timeSlotEntity.getPrice();
        }
        VoucherEntity voucherEntity = voucherRepository.findByIdAndStatus(inputBillDTO.getVoucherId(), true);
        billEntity.setVoucherId(voucherEntity);
        Date dateCharge = DateTimeUtils.convertFromStringToDateTime(inputBillDTO.getDateCharge());
        billEntity.setDateCharge(dateCharge);

        //tinh gia cho Bill
        Float voucherValue = voucherEntity.getVoucherValue();
        price = price - voucherValue;
        billEntity.setPrice(price);

        billEntity.setStatus(true);

        return billRepository.save(billEntity);
    }
}
