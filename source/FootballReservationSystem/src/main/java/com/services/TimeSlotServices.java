package com.services;

import com.dto.InputReservationDTO;
import com.dto.MatchReturnDTO;
import com.dto.TimeSlotDTO;
import com.entity.*;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.hibernate.validator.internal.xml.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class TimeSlotServices {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    FieldServices fieldServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    TimeEnableServices timeEnableServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    @Autowired
    MatchServices matchServices;

    public List<MatchReturnDTO> findUpcomingReservationByDate(String dateString, int fieldOwnerId) {
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndReserveStatusAndDateAndStatus(fieldOwner, true, targetDate, true);
        List<MatchReturnDTO> matchReturnDTOList = new ArrayList<>();

        if (!timeSlotEntityList.isEmpty()) {
            for (TimeSlotEntity timeSlotEntity : timeSlotEntityList) {
                FriendlyMatchEntity friendlyMatchEntity = matchServices.findFriendlyMatchByTimeSlot(timeSlotEntity.getId());
                TourMatchEntity tourMatchEntity = matchServices.findTourMatchByTimeSlot(timeSlotEntity.getId());
                if (friendlyMatchEntity != null) {
                    // time slot là friendly match
                    MatchReturnDTO matchReturnDTO = new MatchReturnDTO(friendlyMatchEntity.getUserId(), friendlyMatchEntity.getUserId(), timeSlotEntity);
                    matchReturnDTOList.add(matchReturnDTO);
                } else if (tourMatchEntity != null) {
                    // time slot là tour match
                    MatchReturnDTO matchReturnDTO = new MatchReturnDTO(tourMatchEntity.getUserId(), tourMatchEntity.getOpponentId(), timeSlotEntity);
                    matchReturnDTOList.add(matchReturnDTO);
                } else {
                    // time slot do chủ sân tự đặt sân
                    MatchReturnDTO matchReturnDTO = new MatchReturnDTO(fieldOwner, fieldOwner, timeSlotEntity);
                    matchReturnDTOList.add(matchReturnDTO);
                }
            }
        }
        return matchReturnDTOList;
    }

    public List<TimeSlotEntity> createTimeSlotForDate(Date date, AccountEntity fieldOwnerEntity, FieldTypeEntity fieldTypeEntity) {
        // kiem tra ngay do la thu may trong tuan
        String dayInWeek = DateTimeUtils.returnDayInWeek(date);

        List<TimeSlotEntity> savedTimeSlotEntityList = new ArrayList<>();

        // kiểm tra nếu được tạo rồi thì ko tạo nữa
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwnerEntity, fieldTypeEntity, date, true) == 0) {
            // đếm chủ sân có bao nhiêu sân loại đó
            int numberOfField = fieldServices.countNumberOfFieldByFieldOwnerAndFieldType(fieldOwnerEntity, fieldTypeEntity);
            // get list timeEnable theo chủ sân, loại sân và theo ngày order by theo thời gian từ nhỏ đến lớn
            List<TimeEnableEntity> timeEnableEntityList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDate(fieldOwnerEntity, fieldTypeEntity, dayInWeek);
            if (timeEnableEntityList.size() != 0) {
                TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
                timeSlotEntity.setDate(date);
                timeSlotEntity.setFieldOwnerId(fieldOwnerEntity);
                timeSlotEntity.setFieldTypeId(fieldTypeEntity);
                // Lấy startTime của timeEnable nhỏ nhất
                timeSlotEntity.setStartTime(timeEnableEntityList.get(0).getStartTime());
                // Lấy endTime của timeEnable lớn nhất
                timeSlotEntity.setEndTime(timeEnableEntityList.get(timeEnableEntityList.size() - 1).getEndTime());
                timeSlotEntity.setReserveStatus(false);
                timeSlotEntity.setStatus(true);
                // chủ sân có bao nhiêu sân thì tạo ra bấy nhiêu timeSlot
                for (int i = 0; i < numberOfField; i++) {
                    if (i == 0) {
                        savedTimeSlotEntityList.add(timeSlotRepository.save(timeSlotEntity));
                    } else {
                        TimeSlotEntity moreTimeSlotEntity = new TimeSlotEntity();
                        moreTimeSlotEntity.setDate(date);
                        moreTimeSlotEntity.setFieldOwnerId(fieldOwnerEntity);
                        moreTimeSlotEntity.setFieldTypeId(fieldTypeEntity);
                        moreTimeSlotEntity.setStartTime(timeSlotEntity.getStartTime());
                        moreTimeSlotEntity.setEndTime(timeSlotEntity.getEndTime());
                        moreTimeSlotEntity.setReserveStatus(false);
                        moreTimeSlotEntity.setStatus(true);
                        savedTimeSlotEntityList.add(timeSlotRepository.save(moreTimeSlotEntity));
                    }
                }
            }
        }
        return savedTimeSlotEntityList;
    }

    public TimeSlotEntity reserveTimeSlot(InputReservationDTO inputReservationDTO) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(inputReservationDTO.getFieldOwnerId(), "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputReservationDTO.getFieldTypeId());
        Date targetDate = DateTimeUtils.convertFromStringToDate(inputReservationDTO.getDate());
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwner, fieldType, targetDate, true) == 0) {
            createTimeSlotForDate(targetDate, fieldOwner, fieldType);
        }
        Date startTime = DateTimeUtils.convertFromStringToTime(inputReservationDTO.getStartTime());

        Date endTime = DateTimeUtils.convertFromStringToTime(inputReservationDTO.getEndTime());

        int duration = (int) ((endTime.getTime() - startTime.getTime()) / 60000);
        // get tất cả thời gian rảnh theo chủ sân, loại sân và ngày
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(fieldOwner, fieldType, targetDate, false, true);
        for (TimeSlotEntity timeSlotEntity : timeSlotEntityList) {
            boolean checkStart = timeSlotEntity.getStartTime().before(startTime) || timeSlotEntity.getStartTime().equals(startTime);
            boolean checkEnd = timeSlotEntity.getEndTime().after(endTime) || timeSlotEntity.getEndTime().equals(endTime);
            if (checkStart && checkEnd) {
                float price = calculatePriceForTimeSlot(startTime, duration, targetDate, fieldOwner, fieldType);
                if (timeSlotEntity.getStartTime().before(startTime) && timeSlotEntity.getEndTime().after(endTime)) {
                    // khoảng thời gian trước giờ đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            timeSlotEntity.getStartTime(), startTime, Float.valueOf(0), false, true);
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    // khoảng thời gian sau giờ đặt
                    TimeSlotEntity timeSlotEntity3 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            endTime, timeSlotEntity.getEndTime(), Float.valueOf(0), false, true);

                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity1);
                    timeSlotRepository.save(timeSlotEntity3);
                    TimeSlotEntity savedTimeSlotEntity = timeSlotRepository.save(timeSlotEntity2);
                    mergeTimeSlotInList(savedTimeSlotEntity.getFieldOwnerId(), savedTimeSlotEntity.getFieldTypeId(), savedTimeSlotEntity.getDate());
                    return savedTimeSlotEntity;
                } else if (timeSlotEntity.getStartTime().before(startTime) && timeSlotEntity.getEndTime().equals(endTime)) {
                    // khoảng thời gian trước giờ đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            timeSlotEntity.getStartTime(), startTime, Float.valueOf(0), false, true);
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity1);
                    TimeSlotEntity savedTimeSlotEntity = timeSlotRepository.save(timeSlotEntity2);
                    mergeTimeSlotInList(savedTimeSlotEntity.getFieldOwnerId(), savedTimeSlotEntity.getFieldTypeId(), savedTimeSlotEntity.getDate());
                    return savedTimeSlotEntity;
                } else if (timeSlotEntity.getStartTime().equals(startTime) && timeSlotEntity.getEndTime().after(endTime)) {
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    // khoảng thời gian sau giờ đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            endTime, timeSlotEntity.getEndTime(), Float.valueOf(0), false, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity2);
                    TimeSlotEntity savedTimeSlotEntity = timeSlotRepository.save(timeSlotEntity1);
                    mergeTimeSlotInList(savedTimeSlotEntity.getFieldOwnerId(), savedTimeSlotEntity.getFieldTypeId(), savedTimeSlotEntity.getDate());
                    return savedTimeSlotEntity;
                } else {
                    // khoảng thời gian đặt bằng chính timeslot
                    timeSlotEntity.setReserveStatus(true);
                    timeSlotEntity.setPrice(price);
                    return timeSlotRepository.save(timeSlotEntity);
                }
            }
        }
        return null;
    }

    public List<TimeSlotEntity> findFreeTimeByFieldOwnerTypeAndDate(int fieldOwnerId, int fieldTypeId, String dateString) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(fieldTypeId);
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);

        // kiểm tra trong database đã đổ time-slot rảnh cho ngày mới chưa
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwner, fieldType, targetDate, true) == 0) {
            // nếu chưa có thì tạo mới time-slot
            List<TimeSlotEntity> timeSlotEntityList = createTimeSlotForDate(targetDate, fieldOwner, fieldType);
            List<TimeSlotEntity> returnTimeSlotEntityList = new ArrayList<>();
            if (!timeSlotEntityList.isEmpty()) {
                returnTimeSlotEntityList.add(timeSlotEntityList.get(0));
            }
            return returnTimeSlotEntityList;
        } else {
            // lấy list thời gian rảnh theo chủ sân, loại sân, ngày và sắp xếp theo thứ tự tăng dần của startTime
            List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(fieldOwner,
                    fieldType, targetDate, false, true);
            List<TimeSlotEntity> savedTimeSlotEntityList = new ArrayList<>();
            while (timeSlotEntityList.size() != 0) {
                TimeSlotEntity savedTimeSlotEntity = timeSlotEntityList.get(0);
                // nếu list chỉ có 1 phần tử thì trả về phần tử đó
                if (timeSlotEntityList.size() == 1) {
                    savedTimeSlotEntityList.add(savedTimeSlotEntity);
                    break;
                }
                // chạy for từ timeslot thứ 2, nếu có găp timeslot nào startTime bằng nhưng endTime sau endTime của timeslot hiện tại
                // thì lấy timeslot đó
                for (int i = 1; i < timeSlotEntityList.size(); i++) {
                    if (timeSlotEntityList.get(i).getStartTime().equals(savedTimeSlotEntity.getStartTime())
                            && timeSlotEntityList.get(i).getEndTime().after(savedTimeSlotEntity.getEndTime())) {
                        savedTimeSlotEntity = timeSlotEntityList.get(i);
                    }
                }
                savedTimeSlotEntityList.add(savedTimeSlotEntity);

                // xóa những timeslot có startTime trước endTime của timeslot đã được đưa vào list trả về (savedTimeSlotEntity)
                List<TimeSlotEntity> deleteList = new ArrayList<>();
                for (TimeSlotEntity timeSlotEntity : timeSlotEntityList) {
                    if (timeSlotEntity.getStartTime().before(savedTimeSlotEntity.getEndTime())) {
                        deleteList.add(timeSlotEntity);
                    }
                }
                if (deleteList.size() != 0) {
                    for (TimeSlotEntity timeSlotEntity : deleteList) {
                        timeSlotEntityList.remove(timeSlotEntity);
                    }
                }
            }
            return savedTimeSlotEntityList;
        }
    }

    private Float calculatePriceForTimeSlot(Date startTime, int duration, Date targetDate, AccountEntity fieldOwner, FieldTypeEntity fieldType) {
        // chia thời gian thành những khoảng nhỏ 30 phút
        int numberOfTimeSlide = duration / 30;
        List<TimeSlotDTO> timeSlotDTOList = new ArrayList<>();
        for (int i = 0; i < numberOfTimeSlide; i++) {
            TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
            if (i == 0) {
                timeSlotDTO.setStartTime(startTime);
            } else {
                timeSlotDTO.setStartTime(timeSlotDTOList.get(i - 1).getEndTime());
            }
            timeSlotDTO.setEndTime(new Date(timeSlotDTO.getStartTime().getTime() + 30 * 60000));
            timeSlotDTOList.add(timeSlotDTO);
        }

        String dateInWeek = DateTimeUtils.returnDayInWeek(targetDate);

        List<TimeEnableEntity> timeEnableEntityList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDate(fieldOwner, fieldType, dateInWeek);
        Float priceReturn = Float.valueOf(0);
        for (TimeSlotDTO timeSlotDTO : timeSlotDTOList) {
            for (TimeEnableEntity timeEnableEntity : timeEnableEntityList) {
                boolean checkStart = timeEnableEntity.getStartTime().before(timeSlotDTO.getStartTime()) || timeEnableEntity.getStartTime().equals(timeSlotDTO.getStartTime());
                boolean checkEnd = timeEnableEntity.getEndTime().after(timeSlotDTO.getEndTime()) || timeEnableEntity.getEndTime().equals(timeSlotDTO.getEndTime());
                if (checkStart && checkEnd) {
                    timeSlotDTO.setPrice(timeEnableEntity.getPrice() / 2);
                }
            }
            priceReturn += timeSlotDTO.getPrice();
        }
        return priceReturn;
    }

    public List<FieldEntity> getListFreeFieldAtSpecificTime(String targetDateStr, String targetTimeStr, int fieldOwnerId, int fieldTypeId) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(fieldTypeId);
        Date targetDate = DateTimeUtils.convertFromStringToDate(targetDateStr);
        Date targetTime = DateTimeUtils.convertFromStringToTime(targetTimeStr);

        List<FieldEntity> fieldEntityList = fieldServices.findFieldEntityByFieldOwnerAndFieldType(fieldOwner, fieldType);
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findTimeSlotHaveMatch(fieldOwner, fieldType, targetDate, targetTime, true, true);
        List<FieldEntity> returnFieldEntityList = new ArrayList<>();
        for (FieldEntity fieldEntity : fieldEntityList) {
            boolean check = true;
            for (TimeSlotEntity timeSlotEntity : timeSlotEntityList) {
                if (timeSlotEntity.getFieldId() != null && timeSlotEntity.getFieldId().getId() == fieldEntity.getId()) {
                    check = false;
                }
            }
            if (check) {
                returnFieldEntityList.add(fieldEntity);
            }
        }
        return returnFieldEntityList;
    }

    public TimeSlotEntity findById(int timeSlotId) {
        TimeSlotEntity timeSlotEntity = timeSlotRepository.findByIdAndStatus(timeSlotId, true);
        if (timeSlotEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Time Slot have id = %s", timeSlotId));
        }
        return timeSlotEntity;
    }

    public TimeSlotEntity setFieldForTimeSlot(int timeSlotId, int fieldId) {
        TimeSlotEntity timeSlotEntity = findById(timeSlotId);
        if (timeSlotEntity.getReserveStatus()) {
            FieldEntity fieldEntity = fieldServices.findFieldEntityById(fieldId);
            timeSlotEntity.setFieldId(fieldEntity);
            return timeSlotRepository.save(timeSlotEntity);
        } else {
            throw new IllegalArgumentException(String.format("Time Slot have id = %s is free time slot, not yet reservation!", timeSlotId));
        }
    }

    public void deleteTimeSlotInList(List<TimeSlotEntity> listTimeSlotEntity) {
        for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
            timeSlotRepository.delete(tSlotEntity);
        }
    }

    public boolean cancelReservationTimeSlot(int timeSlotId) {
        TimeSlotEntity timeSlotEntity = findById(timeSlotId);
        if (!timeSlotEntity.getReserveStatus()) {
            throw new IllegalArgumentException(String.format("Time Slot have id = %s is free time, cannot cancel!", timeSlotId));
        }
        timeSlotEntity.setReserveStatus(false);
        TimeSlotEntity updatedTimeSlot = timeSlotRepository.save(timeSlotEntity);
        mergeTimeSlotInList(updatedTimeSlot.getFieldOwnerId(), updatedTimeSlot.getFieldTypeId(), updatedTimeSlot.getDate());
        return true;
    }

    public boolean mergeTimeSlotInList(AccountEntity fieldOwner, FieldTypeEntity fieldTypeEntity, Date date) {
        List<TimeSlotEntity> listTimeSlotEntity = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(fieldOwner, fieldTypeEntity, date, false, true);

        int[] timeSlotArray = new int[48];

        //khoi tao cho quy thoi gian
        for (int i = 0; i < 48; i++) {
            timeSlotArray[i] = 0;
        }

        //cap nhat quy thoi gian trong nhung time slot cua listTImeSlotEntity
        int t = 0;//tong thoi gian trong quy thoi gian
        for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
            int s = DateTimeUtils.timeToIntInTimeSlot(tSlotEntity.getStartTime());
            int e = DateTimeUtils.timeToIntInTimeSlot(tSlotEntity.getEndTime());
            for (int i = s; i < e; i++) {
                timeSlotArray[i]++;
                t++;
            }
        }
        TimeSlotEntity firstTimeSlotEntity = listTimeSlotEntity.get(0);

        //xep quy thoi gian lai thanh nhung timeslot moi
        while (t > 0) {
//                TimeSlotEntity timeSlotEntity =
            //tao 1 time slot moi
            //khoi tao starttime s endtime e cho slot moi
            int s = -1;
            int e = s;
            //
            int i = 0;
            while (timeSlotArray[i] == 0) {
                i++;
            }
            s = i;
            while ((i + 1 < 48) && (timeSlotArray[i + 1] > 0)) {
                i++;
            }
            e = i;

            Date startTime = DateTimeUtils.intToTimeInTimeSlot(s);
            Date endtTime = DateTimeUtils.intToTimeInTimeSlot(e + 1);
            //kiem tra coi co thang timeslot nay trong list ko
            int check = -1;
            for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
                if (tSlotEntity.getStartTime().equals(startTime) && tSlotEntity.getEndTime().equals(endtTime)) {
                    check = listTimeSlotEntity.indexOf(tSlotEntity);
                }
            }
            if (check == -1) { // ko co thang timeslot co starttime endtime nay, phai tao moi
                //tao time slot entity moi voi start time end time la s va e vua tim dc
                TimeSlotEntity newTimeSlotEntity = new TimeSlotEntity();
                newTimeSlotEntity.setDate(firstTimeSlotEntity.getDate());
                newTimeSlotEntity.setFieldId(firstTimeSlotEntity.getFieldId());
                newTimeSlotEntity.setFieldOwnerId(firstTimeSlotEntity.getFieldOwnerId());
                newTimeSlotEntity.setFieldTypeId(firstTimeSlotEntity.getFieldTypeId());
                newTimeSlotEntity.setReserveStatus(false);
                newTimeSlotEntity.setStatus(true);
                newTimeSlotEntity.setPrice(0);
                newTimeSlotEntity.setStartTime(startTime);
                newTimeSlotEntity.setEndTime(endtTime);
                timeSlotRepository.save(newTimeSlotEntity);
                for (int j = s; j <= e; j++) {
                    timeSlotArray[j]--;
                    t--;
                }
            } else {//co thang timeslot nay ko bi thay doi, remove khoi list, ko xet no nua
                listTimeSlotEntity.remove(check);
                for (int j = s; j <= e; j++) {
                    timeSlotArray[j]--;
                    t--;
                }
            }
            //tru di trong t khoang thoi gian vua tao moi timeslot

        }
        if (listTimeSlotEntity != null) {
            deleteTimeSlotInList(listTimeSlotEntity);
        }
        return true;
    }

    public List<TimeSlotEntity> addTimeSlotWhenCreateNewField(AccountEntity fieldOwner, FieldTypeEntity fieldTypeEntity){
        Date targetDate = DateTimeUtils.convertFromStringToDate(DateTimeUtils.formatDate(new Date()));
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findTimeWhenAddNewField(targetDate, true, fieldOwner, fieldTypeEntity);
        List<TimeSlotEntity> savedTimeSlotEntityList = new ArrayList<>();
        if(!timeSlotEntityList.isEmpty())
            for (TimeSlotEntity timeSlot : timeSlotEntityList) {
                String dayInWeek = DateTimeUtils.returnDayInWeek(timeSlot.getDate());
                List<TimeEnableEntity> timeEnableEntityList = timeEnableServices.findTimeEnableByFieldOwnerTypeAndDate(fieldOwner, fieldTypeEntity, dayInWeek);
                if(!timeEnableEntityList.isEmpty()){

                    TimeSlotEntity timeSlotEntity = new TimeSlotEntity();

                    timeSlotEntity.setFieldOwnerId(fieldOwner);
                    timeSlotEntity.setFieldTypeId(fieldTypeEntity);
                    timeSlotEntity.setDate(timeSlot.getDate());
                    timeSlotEntity.setStartTime(timeEnableEntityList.get(0).getStartTime());
                    timeSlotEntity.setEndTime(timeEnableEntityList.get(timeEnableEntityList.size() -1).getEndTime());
                    timeSlotEntity.setReserveStatus(false);
                    timeSlotEntity.setStatus(true);
                    savedTimeSlotEntityList.add(timeSlotRepository.save(timeSlotEntity));
                }
            }
        return timeSlotEntityList;
    }
}
