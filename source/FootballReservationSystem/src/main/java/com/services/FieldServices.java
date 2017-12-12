package com.services;

import com.config.Constant;
import com.dto.InputFieldDTO;
import com.entity.AccountEntity;
import com.entity.FieldEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeSlotEntity;
import com.repository.FieldRepository;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@Service
public class FieldServices {
    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    TimeEnableServices timeEnableServices;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    Constant constant;

    public FieldEntity createNewField(InputFieldDTO inputFieldDTO) {
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(inputFieldDTO.getFieldTypeId());
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(inputFieldDTO.getFieldOwnerId(), constant.getFieldOwnerRole());
        FieldEntity fieldEntity = fieldRepository.findByFieldOwnerIdAndFieldTypeIdAndNameAndStatus(accountEntity, fieldTypeEntity, inputFieldDTO.getFieldName(), false);
        if (fieldEntity != null) {
            fieldEntity.setDateFrom(new Date());
            fieldEntity.setDateTo(null);
            fieldEntity.setStatus(true);
        } else {
            fieldEntity = new FieldEntity();
            fieldEntity.setName(inputFieldDTO.getFieldName());
            fieldEntity.setFieldOwnerId(accountEntity);
            fieldEntity.setFieldTypeId(fieldTypeEntity);
            fieldEntity.setDateFrom(new Date());
            fieldEntity.setDateTo(null);
            fieldEntity.setStatus(true);
        }
        FieldEntity savedFieldEntity = fieldRepository.save(fieldEntity);
        timeSlotServices.addTimeSlotWhenCreateNewField(savedFieldEntity.getFieldOwnerId(), savedFieldEntity.getFieldTypeId());

        return fieldRepository.save(fieldEntity);
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerId(int fieldOwnerId) {
        Date currDay = DateTimeUtils.convertFromStringToDate(DateTimeUtils.formatDate(new Date()));
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        List<FieldEntity> fieldEntityList = fieldRepository.findByFieldOwnerId(accountEntity, true);
        return fieldEntityList;
    }

    public FieldEntity findFieldEntityById(int fieldId) {
        return fieldRepository.findByIdAndStatus(fieldId, true);
    }

    //disable field từ dateFrom đến dateTo
    public boolean disableField(int fieldId, String dateFrom, String dateTo) {
        FieldEntity fieldEntity = findFieldEntityById(fieldId);
        AccountEntity fieldOwner = fieldEntity.getFieldOwnerId();
        FieldTypeEntity fieldType = fieldEntity.getFieldTypeId();
        String dateNowStr = DateTimeUtils.formatDate(new Date());
        Date dateNow = DateTimeUtils.convertFromStringToDate(dateNowStr);

        // nếu chủ sân chưa có dữ liệu về time enable thì có thể xóa sân thỏa mái
        if (timeEnableServices.findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwner.getId(), fieldType.getId()).isEmpty()) {
            fieldRepository.delete(fieldEntity);
            return true;
        }
        int numDateFromNow = 0;
        // kiểm tra trong 6 ngày tới, ngày nào có người đặt rồi thì báo ngày đó là ngày ko thể disable
        // trả về i, tức là ngày đó cách hiện tại bao lâu
        for (int i = 6; i >= 0; i--) {
            Date targetDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(dateNowStr, i));
            // ngày nào có người đặt sân
            List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndReserveStatusAndDateAndStatus(fieldOwner, true, targetDate, true);
            if (!timeSlotEntityList.isEmpty()) {
                numDateFromNow = i;
                break;
            }
        }
        Date deletedDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(dateNowStr, numDateFromNow));
        Date expectedDeleteDate = DateTimeUtils.convertFromStringToDate(dateFrom);
        if (expectedDeleteDate.before(deletedDate)) {
            throw new IllegalArgumentException(String.format("Have reservation request in %s", DateTimeUtils.getDateAfter(dateNowStr, numDateFromNow)));
        }
        // set ngày đó làm ngày cuối cùng mà sân phải hoạt động để đáp ứng cho những người đã đặt sân
        fieldEntity.setDateTo(DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(dateFrom, -1)));
        // tạo mới record về thời gian sẽ hoạt động lại
        FieldEntity newFieldEntity = new FieldEntity();
        newFieldEntity.setFieldOwnerId(fieldOwner);
        newFieldEntity.setFieldTypeId(fieldType);
        newFieldEntity.setName(fieldEntity.getName());
        newFieldEntity.setDateTo(null);
        newFieldEntity.setDateFrom(DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(dateTo, 1)));
        fieldRepository.save(newFieldEntity);
        return true;
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType) {
        Date currDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.formatDate(new Date()));
        List<FieldEntity> fieldEntityList = fieldRepository.getListFieldWithFieldOwnerTypeAndDate(fieldOwner, fieldType, currDate, true);
        return getListFieldAvailableFromListField(fieldEntityList, currDate);
    }

    public List<FieldEntity> getListFieldAvailableFromListField(List<FieldEntity> inputFieldEntityList, Date targetDate) {
        List<FieldEntity> returnFieldEntityList = new ArrayList<>();
        if (!inputFieldEntityList.isEmpty()) {
            for (FieldEntity fieldEntity : inputFieldEntityList) {
                if (fieldEntity.getDateTo() == null) {
                    returnFieldEntityList.add(fieldEntity);
                } else {
                    if (!fieldEntity.getDateTo().before(targetDate)) {
                        returnFieldEntityList.add(fieldEntity);
                    }
                }
            }
        }
        return returnFieldEntityList;
    }

    public Integer countNumberOfFieldByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType, Date targetDate) {
        List<FieldEntity> fieldEntityList = fieldRepository.getListFieldWithFieldOwnerTypeAndDate(fieldOwner, fieldType, targetDate, true);
        return getListFieldAvailableFromListField(fieldEntityList, targetDate).size();
    }

}
