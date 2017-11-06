package com.services;

import com.config.Constant;
import com.dto.InputVoucherRecordDTO;
import com.entity.AccountEntity;
import com.entity.VoucherEntity;
import com.entity.VoucherRecordEntity;
import com.repository.VoucherRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoucherRecordServices {
    @Autowired
    VoucherServices voucherServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    VoucherRecordRepository voucherRecordRepository;

    @Autowired
    Constant constant;

    public VoucherRecordEntity createNewVoucherRecord(InputVoucherRecordDTO inputVoucherRecordDTO){
        VoucherEntity voucherEntity = voucherServices.findVoucherEntityById(inputVoucherRecordDTO.getVoucherId());
        AccountEntity accountEntity = accountServices.findAccountEntityById(inputVoucherRecordDTO.getUserId(), constant.getUserRole());

        VoucherRecordEntity voucherRecordEntity = voucherRecordRepository.findByUserIdAndVoucherIdAndStatus(accountEntity, voucherEntity, false);
        if (voucherRecordEntity != null) {
            voucherRecordEntity.setStatus(true);
        } else {
            voucherRecordEntity = new VoucherRecordEntity();
            voucherRecordEntity.setUserId(accountEntity);
            voucherRecordEntity.setVoucherId(voucherEntity);
            voucherRecordEntity.setStatus(true);
        }
        return voucherRecordRepository.save(voucherRecordEntity);
    }

    public VoucherRecordEntity findVoucherRecordEntityById(int voucherRecordId) {
        return voucherRecordRepository.findByIdAndStatus(voucherRecordId, true);
    }

    public List<VoucherRecordEntity> findByUserId(int userId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, constant.getUserRole());
        return voucherRecordRepository.findByUserIdAndStatus(accountEntity, true);
    }
}
