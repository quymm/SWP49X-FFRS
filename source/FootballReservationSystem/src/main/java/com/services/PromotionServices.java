package com.services;

import com.dto.InputPromotionDTO;
import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.PromotionEntity;
import com.repository.PromotionRepository;
import com.utils.DateTimeUtils;
import org.hibernate.type.TimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.Date;

@Service
public class PromotionServices {
    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    PromotionRepository promotionRepository;

//    findByFieldOwnerIdAndFieldTypeIdAndStatus
    public List<PromotionEntity> findByFieldOwnerIdAndFieldTypeId(int fieldOwnerId, int fieldTypeId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return promotionRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(accountEntity, fieldTypeEntity, true);
    }

    public PromotionEntity createPromotion(InputPromotionDTO inputPromotionDTO){
        PromotionEntity promotionEntity = new PromotionEntity();
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityById(inputPromotionDTO.getFieldOwnerId(), "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(inputPromotionDTO.getFieldTypeId());
        Date startTime = DateTimeUtils.convertFromStringToTime(inputPromotionDTO.getStartTime());
        Date endTime = DateTimeUtils.convertFromStringToTime(inputPromotionDTO.getEndTime());
        Date dateFrom = DateTimeUtils.convertFromStringToDate(inputPromotionDTO.getDateFrom());
        Date dateTo = DateTimeUtils.convertFromStringToDate(inputPromotionDTO.getDateTo());
        promotionEntity.setFieldOwnerId(fieldOwnerEntity);
        promotionEntity.setFieldTypeId(fieldTypeEntity);
        promotionEntity.setDateFrom(dateFrom);
        promotionEntity.setDateTo(dateTo);
        promotionEntity.setStartTime(startTime);
        promotionEntity.setEndTime(endTime);
        promotionEntity.setSaleOff(inputPromotionDTO.getSaleOff());
        promotionEntity.setFreeServices(inputPromotionDTO.getFreeServices());
        promotionEntity.setStatus(true);
        return promotionRepository.save(promotionEntity);
    }

    public PromotionEntity disablePromotion(int fieldOwnerId, int fieldTypeId, String sDateFrom, String sdateTo){
        AccountEntity fieldOwnerEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        Date dateFrom = DateTimeUtils.convertFromStringToDate(sDateFrom);
        Date dateTo = DateTimeUtils.convertFromStringToDate(sdateTo);
        PromotionEntity promotionEntity = promotionRepository.findByFieldOwnerIdAndFieldTypeIdAndDateFromAndDateToAndStatus(fieldOwnerEntity, fieldTypeEntity, dateFrom, dateTo, true);
        promotionEntity.setStatus(false);
        return promotionRepository.save(promotionEntity);
    }
}
