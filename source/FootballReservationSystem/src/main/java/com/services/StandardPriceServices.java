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
import java.util.List;

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

    public StandardPriceEntity updateStandardPrice(InputStandardPriceDTO inputStandardPriceDTO) {
        AccountEntity staff = accountServices.findAccountEntityByIdAndRole(inputStandardPriceDTO.getStaffId(), constant.getStaffRole());
        StandardPriceEntity standardPriceEntity = standardPriceRepository.findById(inputStandardPriceDTO.getStandardPriceId());
        standardPriceEntity.setStaffId(staff);
        standardPriceEntity.setMaxPrice(inputStandardPriceDTO.getMaxPrice());
        standardPriceEntity.setMinPrice(inputStandardPriceDTO.getMinPrice());
        standardPriceEntity.setStatus(true);
        return standardPriceRepository.save(standardPriceEntity);
    }

    public List<StandardPriceEntity> getStandardPriceWithRushHour(boolean rushHour) {
        return standardPriceRepository.findByRushHourAndStatus(rushHour, true);
    }

    public StandardPriceEntity getStandardPriceWithDateAndFieldTypeAndRushHour(int fieldTypeId, boolean rushHour){
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return standardPriceRepository.findByFieldTypeIdAndRushHourAndStatus(fieldTypeEntity, rushHour, true);
    }

    public List<StandardPriceEntity> getAllStandardPrice() {
        return standardPriceRepository.getAllByStatus(true);
    }

    public float getMaxPriceWithRequestReservationDTO(RequestReservateDTO requestReservateDTO) {

        Date endTime = DateTimeUtils.convertFromStringToTime(requestReservateDTO.getEndTime());

        float maxPriceRush = getStandardPriceWithDateAndFieldTypeAndRushHour(requestReservateDTO.getFieldTypeId(), true).getMaxPrice();
        float maxPriceNormal = getStandardPriceWithDateAndFieldTypeAndRushHour(requestReservateDTO.getFieldTypeId(), false).getMaxPrice();

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
