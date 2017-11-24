package com.services;

import com.config.Constant;
import com.dto.InputFieldDTO;
import com.entity.*;
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

//     trả về ngày sân sẽ bắt đầu được xóa
//    public String deleteFieldEntity(int fieldId) {
//        FieldEntity fieldEntity = findFieldEntityById(fieldId);
//        AccountEntity fieldOwner = fieldEntity.getFieldOwnerId();
//        FieldTypeEntity fieldType = fieldEntity.getFieldTypeId();
//        String dateNow = DateTimeUtils.formatDate(new Date());
//
//        // nếu chủ sân chưa có dữ liệu về time enable thì có thể xóa sân thỏa mái
//        if (timeEnableServices.findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwner.getId(), fieldType.getId()).isEmpty()) {
//            fieldRepository.delete(fieldEntity);
//            return dateNow;
//        }
//        int numDateFromNow = 0;
//        // kiểm tra trong 6 ngày tới, ngày nào thời gian rảnh bằng đúng time enable 1 ngày thì ngày đó là ngày xóa được
//        // trả về i, tức là ngày đó cách hiện tại bao lâu
//        for (int i = 7; i >= 0; i--) {
//            boolean checkEqual = true;
//            // bắt đầu từ ngày xa nhất với i = 7;
//            String targetDate = DateTimeUtils.getDateAfter(dateNow, i);
//            // kiểm tra hôm đó là thứ mấy trong tuần
//            String dateInWeek = DateTimeUtils.returnDayInWeek(DateTimeUtils.convertFromStringToDate(targetDate));
//            // get time enable sẽ được apply cho ngày hôm đó
//            Date effectiveDateOfTimeEnable = timeEnableServices.getEffectiveDate(DateTimeUtils.convertFromStringToDate(targetDate));
//            // get list thời gian hoạt động trong giờ trước cao điểm, ko optimal
//            List<TimeEnableEntity> timeEnableEntityList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDateInWeekAndEffectiveDate(fieldOwner, fieldType, dateInWeek, effectiveDateOfTimeEnable, false);
//            if (timeEnableEntityList.isEmpty()) {
//                continue;
//            }
//            Date startTimeNotOptimal = timeEnableEntityList.get(0).getStartTime();
//            Date endTimeNotOptimal = timeEnableEntityList.get(timeEnableEntityList.size() - 1).getEndTime();
//            List<TimeEnableEntity> timeEnableEntityOptimalList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDateInWeekAndEffectiveDate(fieldOwner, fieldType, dateInWeek, effectiveDateOfTimeEnable, true);
//            // thời gian rảnh của chủ sân tại ngày hôm đó
//            List<TimeSlotEntity> freeTimeSlotList = timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwner.getId(), fieldType.getId(), targetDate);
//            List<TimeSlotEntity> freeTimeSlotOptimalList = new ArrayList<>();
//            for (TimeSlotEntity timeSlot : freeTimeSlotList) {
//                // nếu nó ko phải là time slot tối ưu thì nó chỉ có duy nhất 1 cái đối với ngày chưa được đặt hết sân
//                if (!timeSlot.isOptimal()) {
//                    if (!timeSlot.getStartTime().equals(startTimeNotOptimal) && !timeSlot.getEndTime().equals(endTimeNotOptimal)) {
//                        checkEqual = false;
//                        break;
//                    }
//                } else {
//                    // tạo 1 list chỉ gồm những time slot optimal
//                    freeTimeSlotOptimalList.add(timeSlot);
//                }
//            }
//            // nếu time enable có optimal vào giờ cao điểm
//            if (!timeEnableEntityOptimalList.isEmpty()) {
//                // mà free time ko có optimal hoặc là có optimal nhưng ít hơn time enable thì là sai
//                if (freeTimeSlotOptimalList.isEmpty() || freeTimeSlotOptimalList.size() != timeEnableEntityOptimalList.size()) {
//                    checkEqual = false;
//                }
//            }
//            // đi từ cách thời điểm hiện tại 7 ngày trở về trước, nếu ngày nào mà có thời gian rảnh khác thời gian hoạt động
//            // nghĩa là ngày đó đã có đặt sân, ngày đó là ngày expire date
//            if (!checkEqual) {
//                numDateFromNow = i;
//                break;
//            }
//        }
//        // set ngày đó làm ngày cuối cùng mà sân phải hoạt động để đáp ứng cho những người đã đặt sân
//        fieldEntity.setDateTo(DateTimeUtils.convertFromStringToDate(DateTimeUtils.getDateAfter(dateNow, numDateFromNow)));
//        // tạo mới record về thời gian sẽ hoạt động lại
//        FieldEntity newFieldEntity = new FieldEntity();
//        newFieldEntity.setFieldOwnerId(fieldOwner);
//        newFieldEntity.setFieldTypeId(fieldType);
//        newFieldEntity.setName(fieldEntity.getName());
//        newFieldEntity.set
//
//        // xóa những time slot trống từ ngày đó trở về sau nếu đã đổ rồi
//        for (int i = numDateFromNow + 1; i < 7; i++) {
//            String deleteDate = DateTimeUtils.getDateAfter(dateNow, i);
//            List<TimeSlotEntity> freeTimeSlotList = timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwner.getId(), fieldType.getId(), deleteDate);
//            if (!freeTimeSlotList.isEmpty()) {
//                for (TimeSlotEntity timeSlotEntity : freeTimeSlotList) {
//                    timeSlotRepository.delete(timeSlotEntity);
//                }
//            }
//        }
//        return DateTimeUtils.formatDate(fieldRepository.save(fieldEntity).getExpirationDate());
//    }

    public List<FieldEntity> findFieldEntityByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType, Date targetDate) {
        List<FieldEntity> fieldEntityList = fieldRepository.getListFieldWithFieldOwnerTypeAndDate(fieldOwner, fieldType, targetDate, true);
        if (!fieldEntityList.isEmpty()) {
            List<FieldEntity> returnFieldEntity = new ArrayList<>();
            for (FieldEntity fieldEntity : fieldEntityList) {
                if(fieldEntity.getDateTo() != null && !fieldEntity.getDateTo().before(targetDate)){
                    returnFieldEntity.add(fieldEntity);
                }
            }
            return returnFieldEntity;
        }
        return fieldEntityList;
    }

    public Integer countNumberOfFieldByFieldOwnerAndFieldType(AccountEntity fieldOwner, FieldTypeEntity fieldType, Date targetDate) {
        List<FieldEntity> fieldEntityList = fieldRepository.getListFieldWithFieldOwnerTypeAndDate(fieldOwner, fieldType, targetDate,true);
        if (!fieldEntityList.isEmpty()) {
            List<FieldEntity> returnFieldEntity = new ArrayList<>();
            for (FieldEntity fieldEntity : fieldEntityList) {
                if(fieldEntity.getDateTo() != null && !fieldEntity.getDateTo().before(targetDate)){
                    returnFieldEntity.add(fieldEntity);
                }
            }
            return returnFieldEntity.size();
        }
        return 0;
    }

}
