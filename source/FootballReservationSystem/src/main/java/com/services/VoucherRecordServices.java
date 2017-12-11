package com.services;

import com.config.Constant;
import com.dto.InputDepositHistoryDTO;
import com.dto.InputVoucherRecordDTO;
import com.entity.AccountEntity;
import com.entity.ProfileEntity;
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

    public VoucherRecordEntity exchangeVoucher(InputVoucherRecordDTO inputVoucherRecordDTO) {
        VoucherEntity voucherEntity = voucherServices.findVoucherEntityById(inputVoucherRecordDTO.getVoucherId());
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(inputVoucherRecordDTO.getUserId(), constant.getUserRole());

        VoucherRecordEntity voucherRecordEntity = new VoucherRecordEntity();
        voucherRecordEntity.setUserId(accountEntity);
        voucherRecordEntity.setVoucherId(voucherEntity);
        voucherRecordEntity.setStatus(true);

        VoucherRecordEntity savedVoucherRecord = voucherRecordRepository.save(voucherRecordEntity);

        InputDepositHistoryDTO inputDepositHistoryDTO = new InputDepositHistoryDTO();
        inputDepositHistoryDTO.setAccountId(accountEntity.getId());
        inputDepositHistoryDTO.setBalance(savedVoucherRecord.getVoucherId().getVoucherValue());
        inputDepositHistoryDTO.setInformation("Exchange voucher");
        inputDepositHistoryDTO.setRole(constant.getUserRole());

        accountServices.depositMoney(inputDepositHistoryDTO);
        return voucherRecordRepository.save(voucherRecordEntity);
    }

    public VoucherRecordEntity findVoucherRecordEntityById(int voucherRecordId) {
        return voucherRecordRepository.findByIdAndStatus(voucherRecordId, true);
    }

    public List<VoucherRecordEntity> findByUserId(int userId) {
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        return voucherRecordRepository.findByUserIdAndStatus(accountEntity, true);
    }
}
