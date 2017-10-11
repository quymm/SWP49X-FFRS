package com.services;

import com.dto.InputVoucherDTO;
import com.entity.VoucherEntity;
import com.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherServices {
    @Autowired
    VoucherRepository voucherRepository;

    public VoucherEntity createNewVoucher(InputVoucherDTO inputVoucherDTO){
        VoucherEntity voucherEntity = new VoucherEntity();
        voucherEntity.setBonusPointTarget(inputVoucherDTO.getBonusPointTarget());
        voucherEntity.setVoucherValue(inputVoucherDTO.getVoucherValue());
        voucherEntity.setStatus(true);
        return  voucherRepository.save(voucherEntity);
    }

    public VoucherEntity updateVoucher(InputVoucherDTO inputVoucherDTO, int voucherId){
        VoucherEntity voucherEntity = voucherRepository.findByIdAndStatus(voucherId, true);
        voucherEntity.setBonusPointTarget(inputVoucherDTO.getBonusPointTarget());
        voucherEntity.setVoucherValue(inputVoucherDTO.getVoucherValue());
        return  voucherRepository.save(voucherEntity);
    }

    public VoucherEntity disableVoucher(int voucherId){
        VoucherEntity voucherEntity = voucherRepository.findByIdAndStatus(voucherId, true);
        voucherEntity.setStatus(false);
        return  voucherRepository.save(voucherEntity);
    }
}
