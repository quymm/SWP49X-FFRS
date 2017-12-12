package com.services;

import com.config.Constant;
import com.dto.InputBillDTO;
import com.entity.*;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

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

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    Constant constant;


    public BillEntity createBill(InputBillDTO inputBillDTO) {
        BillEntity billEntity = new BillEntity();
        billEntity.setDateCharge(new Date());

        Float price;
        if (inputBillDTO.getFriendlyMatchId() != null && inputBillDTO.getFriendlyMatchId() != 0) {
            FriendlyMatchEntity friendlyMatchEntity = matchServices.findFriendlyMatchEntityById(inputBillDTO.getFriendlyMatchId());
            billEntity.setFriendlyMatchId(friendlyMatchEntity);
            billEntity.setUserId(friendlyMatchEntity.getUserId());
            TimeSlotEntity timeSlotEntity = friendlyMatchEntity.getTimeSlotId();
            billEntity.setFieldOwnerId(timeSlotEntity.getFieldOwnerId());
            price = timeSlotEntity.getPrice();


        } else {
            TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputBillDTO.getTourMatchId());
            billEntity.setTourMatchId(tourMatchEntity);
            TimeSlotEntity timeSlotEntity = tourMatchEntity.getTimeSlotId();
            billEntity.setFieldOwnerId(timeSlotEntity.getFieldOwnerId());
            price = timeSlotEntity.getPrice() / 2;
            if (!inputBillDTO.isOpponentPayment()) {
                billEntity.setUserId(tourMatchEntity.getUserId());
            } else {
                billEntity.setUserId(tourMatchEntity.getOpponentId());
            }
        }

        billEntity.setPrice(price);

        // transfer fee from user to owner
        accountServices.changeBalance(billEntity.getUserId().getId(), billEntity.getPrice() * (-1), constant.getUserRole());
        accountServices.changeBalance(billEntity.getFieldOwnerId().getId(), billEntity.getPrice(), constant.getFieldOwnerRole());


        billEntity.setStatus(true);
        return billRepository.save(billEntity);
    }

    public BillEntity findById(int billId){
        BillEntity billEntity = billRepository.findByIdAndStatus(billId, true);
        if(billEntity == null){
            throw new EntityNotFoundException(String.format("Not found Bill Entity have id = %s", billId));
        }
        return billEntity;
    }

    public List<BillEntity> findByUserIdIn7Date(int userId){
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        Date now = new Date();
        Date targetDate = new Date(now.getTime() - 7*24*60*60*1000); //7 day
        List<BillEntity> billEntityList = billRepository.findBillWithUserIdAndDateCharge(user, true, targetDate);
        return billEntityList;
    }

    public List<BillEntity> findByFieldOwnerIdIn7Date(int fieldOwnerId){
        AccountEntity fieldOwner = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        Date now = new Date();
        Date targetDate = new Date(now.getTime() - 7*24*60*60*1000);
        List<BillEntity> billEntityList = billRepository.findBillWithFieldOwnerIdAndDateCharge(fieldOwner, targetDate, true);
        return billEntityList;
    }


}
