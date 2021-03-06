package com.services;

import com.dto.InputVoucherDTO;
import com.entity.VoucherEntity;
import com.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServices {
    @Autowired
    VoucherRepository voucherRepository;

    public VoucherEntity createNewVoucher(InputVoucherDTO inputVoucherDTO){
        VoucherEntity voucherEntity = new VoucherEntity();
        voucherEntity.setBonusPointTarget(inputVoucherDTO.getBonusPointTarget());
        voucherEntity.setVoucherValue(inputVoucherDTO.getVoucherValue());
        voucherEntity.setImageUrl(inputVoucherDTO.getImageUrl());
        voucherEntity.setStatus(true);
        return  voucherRepository.save(voucherEntity);
    }

    public VoucherEntity updateVoucher(InputVoucherDTO inputVoucherDTO, int voucherId){
        VoucherEntity voucherEntity = voucherRepository.findByIdAndStatus(voucherId, true);
        voucherEntity.setBonusPointTarget(inputVoucherDTO.getBonusPointTarget());
        voucherEntity.setVoucherValue(inputVoucherDTO.getVoucherValue());
        voucherEntity.setImageUrl(inputVoucherDTO.getImageUrl());
        return  voucherRepository.save(voucherEntity);
    }

    public VoucherEntity disableVoucher(int voucherId){
        VoucherEntity voucherEntity = voucherRepository.findByIdAndStatus(voucherId, true);
        voucherEntity.setStatus(false);
        return  voucherRepository.save(voucherEntity);
    }
    public VoucherEntity findVoucherEntityById(int voucherId) {
        return voucherRepository.findByIdAndStatus(voucherId, true);
    }

    public List<VoucherEntity> findAll(){
        return voucherRepository.findAllByStatus(true);
    }
}
