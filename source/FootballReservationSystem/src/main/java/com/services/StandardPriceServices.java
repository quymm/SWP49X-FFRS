package com.services;

import com.config.Constant;
import com.dto.InputStandardPriceDTO;
import com.dto.RequestReservateDTO;
import com.entity.AccountEntity;
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
    AccountServices accountServices;

    @Autowired
    Constant constant;

    public StandardPriceEntity createNewStandardPrice(InputStandardPriceDTO inputStandardPriceDTO){
        AccountEntity staff = accountServices.findAccountEntityByIdAndRole(inputStandardPriceDTO.getStaffId(), constant.getStaffRole());
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputStandardPriceDTO.getFieldTypeId());

        Date dateFrom = DateTimeUtils.convertFromStringToDate(inputStandardPriceDTO.getDateFrom());
        Date dateTo = DateTimeUtils.convertFromStringToDate(inputStandardPriceDTO.getDateTo());

        StandardPriceEntity standardPriceEntity = new StandardPriceEntity();
        standardPriceEntity.setStaffId(staff);
        standardPriceEntity.setFieldTypeId(fieldType);
        standardPriceEntity.setDateFrom(dateFrom);
        standardPriceEntity.setDateTo(dateTo);
        standardPriceEntity.setMaxPrice(inputStandardPriceDTO.getMaxPrice());
        standardPriceEntity.setMinPrice(inputStandardPriceDTO.getMinPrice());
        standardPriceEntity.setRushHour(inputStandardPriceDTO.isRushHour());
        standardPriceEntity.setStatus(true);
        return standardPriceRepository.save(standardPriceEntity);
    }

    public StandardPriceEntity getStandartPriceWithDateFieldTypeAndRushHour(String targetDateStr, int fieldTypeId, boolean rushHour) {
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        Date targetDate = DateTimeUtils.convertFromStringToDate(targetDateStr);
        return standardPriceRepository.getStandartPriceWithTargetDateFieldTypeAndRushHour(targetDate, true, rushHour, fieldTypeEntity);
    }

    public float getMaxPriceWithRequestReservationDTO(RequestReservateDTO requestReservateDTO) {

        Date endTime = DateTimeUtils.convertFromStringToTime(requestReservateDTO.getEndTime());

        float maxPriceRush = getStandartPriceWithDateFieldTypeAndRushHour(requestReservateDTO.getDate(), requestReservateDTO.getFieldTypeId(), true).getMaxPrice();
        float maxPriceNormal = getStandartPriceWithDateFieldTypeAndRushHour(requestReservateDTO.getDate(), requestReservateDTO.getFieldTypeId(), false).getMaxPrice();

        float price = 0;
        Date rushHour = DateTimeUtils.convertFromStringToTime(constant.getRushHour());
        // endTime nằm sau giờ cao điểm
        if (endTime.after(rushHour)) {
            Date start = new Date(endTime.getTime() - requestReservateDTO.getDuration() * 60000);
            if (start.before(rushHour)) {
                // giờ chơi sẽ kéo dài từ giờ thường vào giờ cao điểm
                float priceInRush = ((endTime.getTime() - rushHour.getTime()) / (60000 * 60)) * maxPriceRush;
                float priceInNormal = ((rushHour.getTime() - start.getTime()) / (60000 * 60)) * maxPriceNormal;
                price = priceInNormal + priceInRush;
            } else {
                // giờ chơi nằm hoàn toàn vào giờ cao điểm
                price = (requestReservateDTO.getDuration() / 60) * maxPriceRush;
            }
        } else {
            // giờ chơi nằm vào giờ thường
            price = (requestReservateDTO.getDuration() / 60) * maxPriceNormal;
        }
        return price;
    }
}
