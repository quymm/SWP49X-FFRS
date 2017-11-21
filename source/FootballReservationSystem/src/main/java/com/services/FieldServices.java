package com.services;

import com.config.Constant;
import com.dto.InputFieldDTO;
import com.entity.*;
import com.repository.FieldRepository;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            fieldEntity.setStatus(true);
        } else {
            fieldEntity = new FieldEntity();
            fieldEntity.setName(inputFieldDTO.getFieldName());
            fieldEntity.setFieldOwnerId(accountEntity);
            fieldEntity.setFieldTypeId(fieldTypeEntity);
            fieldEntity.setStatus(true);
        }
        FieldEntity savedFieldEntity = fieldRepository.save(fieldEntity);
        timeSlotServices.addTimeSlotWhenCreateNewField(savedFieldEntity.getFieldOwnerId(), savedFieldEntity.getFieldTypeId());

        return fieldRepository.save(fieldEntity);
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerId(int fieldOwnerId) {
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        return fieldRepository.findByFieldOwnerIdAndStatus(accountEntity, true);
    }

    public FieldEntity findFieldEntityByFieldNameAndFieldOwnerId(String fieldName, int fieldOwnerId) {
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        return fieldRepository.findByFieldOwnerIdAndNameAndStatus(accountEntity, fieldName, true);
    }

    public FieldEntity findFieldEntityById(int fieldId) {
        return fieldRepository.findByIdAndStatus(fieldId, true);
    }

    // trả về ngày sân sẽ bắt đầu được xóa
    public String deleteFieldEntity(int fieldId) {
        FieldEntity fieldEntity = findFieldEntityById(fieldId);
        AccountEntity fieldOwner = fieldEntity.getFieldOwnerId();
        FieldTypeEntity fieldType = fieldEntity.getFieldTypeId();
        String dateNow = DateTimeUtils.formatDate(new Date());

        // nếu chủ sân chưa có dữ liệu về time enable thì có thể xóa sân thỏa mái
        if (timeEnableServices.findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwner.getId(), fieldType.getId()).isEmpty()) {
            fieldRepository.delete(fieldEntity);
            return dateNow;
        }
        int numDateFromNow = 0;
        // kiểm tra trong 6 ngày tới, ngày nào thời gian rảnh bằng đúng time enable 1 ngày thì ngày đó là ngày xóa được
        // trả về i, tức là ngày đó cách hiện tại bao lâu
        for (int i = 6; i >= 0; i--) {
            boolean checkEqual = true;
            // bắt đầu từ ngày hiện tại với i = 0;
            String targetDate = DateTimeUtils.getDateAfter(dateNow, i);
            // kiểm tra hôm đó là thứ mấy trong tuần
            String dateInWeek = DateTimeUtils.returnDayInWeek(DateTimeUtils.convertFromStringToDate(targetDate));
            // get list thời gian hoạt động mà chủ sân đưa ra ứng cho ngày hôm đó
            List<TimeEnableEntity> timeEnableEntityList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDate(fieldOwner, fieldType, dateInWeek, null);
            // thời gian rảnh của chủ sân tại ngày hôm đó
            List<TimeSlotEntity> freeTimeSlotList = timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwner.getId(), fieldType.getId(), targetDate);

            // nếu thời gian rảnh đúng bằng thời gian hoạt động của 1 ngày
            for (int j = 0; j < (timeEnableEntityList.size() < freeTimeSlotList.size() ? timeEnableEntityList.size() : freeTimeSlotList.size()); j++) {
                if (!timeEnableEntityList.get(j).getStartTime().equals(freeTimeSlotList.get(j).getStartTime())
                        || !timeEnableEntityList.get(j).getEndTime().equals(freeTimeSlotList.get(j).getEndTime())) {
                    checkEqual = false;
                    break;
                }
            }
            if (!checkEqual) {
                numDateFromNow = i;
                break;
            }
        }
        // set ngày đó làm ngày cuối cùng mà sân phải hoạt động để đáp ứng cho những người đã đặt sân
        fieldEntity.setExpirationDate(DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(dateNow, numDateFromNow)));

        // xóa những time slot trống từ ngày đó trở về sau nếu đã đổ rồi
        for (int i = numDateFromNow + 1; i < 7; i++) {
            String deleteDate = DateTimeUtils.getDateAfter(dateNow, i);
            List<TimeSlotEntity> freeTimeSlotList = timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwner.getId(), fieldType.getId(), deleteDate);
            if (!freeTimeSlotList.isEmpty()) {
                for (TimeSlotEntity timeSlotEntity : freeTimeSlotList) {
                    timeSlotRepository.delete(timeSlotEntity);
                }
            }
        }
        return DateTimeUtils.formatDate(fieldRepository.save(fieldEntity).getExpirationDate());
    }

    public List<FieldEntity> findFieldEntityByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType) {
        return fieldRepository.findByFieldOwnerIdAndFieldTypeIdAndStatus(fieldOwner, fieldType, true);
    }

    public Integer countNumberOfFieldByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType) {
        return fieldRepository.countByFieldOwnerIdAndAndFieldTypeIdAndStatus(fieldOwner, fieldType, true);
    }

}
