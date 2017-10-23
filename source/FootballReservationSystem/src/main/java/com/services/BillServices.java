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
    VoucherServices voucherServices;

    @Autowired
    MatchServices matchServices;


    public BillEntity createBill(InputBillDTO inputBillDTO) {
        BillEntity billEntity = new BillEntity();
        billEntity.setDateCharge(new Date());

        Float price = new Float(0.0);
        if (inputBillDTO.getFriendlyMatchId() != null && inputBillDTO.getFriendlyMatchId() != 0) {
            FriendlyMatchEntity friendlyMatchEntity = matchServices.findFriendlyMatchEntityById(inputBillDTO.getFriendlyMatchId());
            billEntity.setFriendlyMatchId(friendlyMatchEntity);
            billEntity.setUserId(friendlyMatchEntity.getUserId());
            TimeSlotEntity timeSlotEntity = friendlyMatchEntity.getTimeSlotId();
            price = timeSlotEntity.getPrice();
        } else {
            TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputBillDTO.getTourMatchId());
            billEntity.setTourMatchId(tourMatchEntity);
            TimeSlotEntity timeSlotEntity = tourMatchEntity.getTimeSlotId();
            price = timeSlotEntity.getPrice() / 2;
            if (!inputBillDTO.isOpponentPayment()) {
                billEntity.setUserId(tourMatchEntity.getUserId());
            } else {
                billEntity.setUserId(tourMatchEntity.getOpponentId());
            }
        }
        if (inputBillDTO.getVoucherId() != null && inputBillDTO.getVoucherId() != 0) {
            VoucherEntity voucherEntity = voucherServices.findVoucherEntityById(inputBillDTO.getVoucherId());
            if (voucherEntity != null) {
                billEntity.setVoucherId(voucherEntity);

                //tinh gia cho Bill neu co voucher
                Float voucherValue = voucherEntity.getVoucherValue();
                price = price - voucherValue;
            }

        }
        billEntity.setPrice(price);

        billEntity.setStatus(true);
        return billRepository.save(billEntity);
    }


}
