package com.services;

import com.config.Constant;
import com.dto.InputPromotionDTO;
import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.PromotionEntity;
import com.repository.PromotionRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PromotionServices {
    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    Constant constant;

    public PromotionEntity createNewPromotion(InputPromotionDTO inputPromotionDTO) {
        PromotionEntity promotionEntity = new PromotionEntity();
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(inputPromotionDTO.getFieldTypeId());
        AccountEntity fieldOwner = accountServices.findAccountEntityByIdAndRole(inputPromotionDTO.getFieldOwnerId(), constant.getFieldOwnerRole());
        promotionEntity.setFieldTypeId(fieldTypeEntity);
        promotionEntity.setFieldOwnerId(fieldOwner);
        promotionEntity.setDateFrom(DateTimeUtils.convertFromStringToDate(inputPromotionDTO.getDateFrom()));
        promotionEntity.setDateTo(DateTimeUtils.convertFromStringToDate(inputPromotionDTO.getDateTo()));
        promotionEntity.setStartTime(DateTimeUtils.convertFromStringToTime(inputPromotionDTO.getStartTime()));
        promotionEntity.setEndTime(DateTimeUtils.convertFromStringToTime(inputPromotionDTO.getEndTime()));
        promotionEntity.setSaleOff(inputPromotionDTO.getSaleOff());
        promotionEntity.setFreeServices(inputPromotionDTO.getFreeServices());
        promotionEntity.setStatus(true);
        return promotionRepository.save(promotionEntity);
    }

    public List<PromotionEntity> getListPromotionByFieldOwnerAndDate(int fieldOwnerId, String date) {
        Date targetDate = DateTimeUtils.convertFromStringToDate(date);
        AccountEntity fieldOwner = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        return promotionRepository.getPromotionByFieldOwnerAndTargetDate(fieldOwner, targetDate, true);
    }

    public List<PromotionEntity> getListPromotionNow() {
        Date targetDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.formatDate(new Date()));
        return promotionRepository.getPromotionByDate(targetDate, true);
    }

    public List<PromotionEntity> getListPromotionByFieldOwnerId(int fieldOwnerId) {
        AccountEntity fieldOwner = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        return promotionRepository.findByFieldOwnerIdAndStatus(fieldOwner, true);
    }


}
