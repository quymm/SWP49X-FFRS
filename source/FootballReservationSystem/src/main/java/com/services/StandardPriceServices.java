package com.services;

import com.config.Constant;
import com.dto.RequestReservateDTO;
import com.entity.FieldTypeEntity;
import com.entity.StandardPriceEntity;
import com.repository.StandardPriceRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StandardPriceServices {
    @Autowired
    StandardPriceRepository standardPriceRepository;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    Constant constant;

    public StandardPriceEntity getStandartPriceWithDateFieldTypeAndRushHour(String targetDateStr, int fieldTypeId, boolean rushHour){
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        Date targetDate = DateTimeUtils.convertFromStringToDate(targetDateStr);
        return standardPriceRepository.getStandartPriceWithTargetDateFieldTypeAndRushHour(targetDate, rushHour, true, fieldTypeEntity);
    }

//    public float getMaxPriceWithRequestReservationDTO(RequestReservateDTO requestReservateDTO){
//        FieldTypeEntity fieldTypeEntity
//    }
}
