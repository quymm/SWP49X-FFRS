package com.services;

import com.dto.InputReservationDTO;
import com.dto.TimeSlotDTO;
import com.entity.*;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.hibernate.validator.internal.xml.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    FriendlyMatchServices friendlyMatchServices;

    public List<TimeSlotEntity> findUpcomingReservationByDate(String dateString, int fieldOwnerId, int fieldTypeId) {
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(accountEntity, fieldTypeEntity, targetDate, true, true);
    }

    public List<TimeSlotEntity> createTimeSlotForDate(Date date, AccountEntity fieldOwnerEntity, FieldTypeEntity fieldTypeEntity) {
        // kiem tra ngay do la thu may trong tuan
        String dayInWeek = DateTimeUtils.returnDayInWeek(date);

        List<AccountEntity> fieldOwnerList = accountServices.findAccountByRole("owner");
        List<FieldTypeEntity> fieldTypeEntityList = fieldTypeServices.findAllFieldType();
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

    public FriendlyMatchEntity reserveFriendlyMatch(InputReservationDTO inputReservationDTO) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(inputReservationDTO.getFieldOwnerId(), "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(inputReservationDTO.getFieldTypeId());
        Date targetDate = DateTimeUtils.convertFromStringToDate(inputReservationDTO.getDate());
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwner, fieldType, targetDate, true) == 0) {
            createTimeSlotForDate(targetDate, fieldOwner, fieldType);
        }
        Date startTime = DateTimeUtils.convertFromStringToTime(inputReservationDTO.getStartTime());

        Date endTime = new Date(startTime.getTime() + inputReservationDTO.getDuration() * 60000);
//        endTime.setTime(startTime.getTime() + inputReservationDTO.getDuration() * 60000);
        // get tất cả thời gian rảnh theo chủ sân, loại sân và ngày
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(fieldOwner, fieldType, targetDate, false, true);
        for (TimeSlotEntity timeSlotEntity : timeSlotEntityList) {
            boolean checkStart = timeSlotEntity.getStartTime().before(startTime) || timeSlotEntity.getStartTime().equals(startTime);
            boolean checkEnd = timeSlotEntity.getEndTime().after(endTime) || timeSlotEntity.getEndTime().equals(endTime);
            if (checkStart && checkEnd) {
                float price = calculatePriceForTimeSlot(startTime, inputReservationDTO.getDuration(), targetDate, fieldOwner, fieldType);
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

                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity2), inputReservationDTO.getUserId());
                } else if (timeSlotEntity.getStartTime().before(startTime) && timeSlotEntity.getEndTime().equals(startTime)) {
                    // khoảng thời gian trước giờ đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            timeSlotEntity.getStartTime(), startTime, Float.valueOf(0), false, true);
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity1);
                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity2), inputReservationDTO.getUserId());
                } else if (timeSlotEntity.getStartTime().equals(startTime) && timeSlotEntity.getEndTime().after(endTime)) {
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    // khoảng thời gian sau giờ đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            endTime, timeSlotEntity.getEndTime(), Float.valueOf(0), false, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity2);
                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity1), inputReservationDTO.getUserId());
                } else {
                    // khoảng thời gian đặt bằng chính timeslot
                    timeSlotEntity.setReserveStatus(true);
                    timeSlotEntity.setPrice(price);
                    return friendlyMatchServices.createNewFriendlyMatch(timeSlotRepository.save(timeSlotEntity), inputReservationDTO.getUserId());
                }
            }
        }
        return null;
    }

    public List<TimeSlotEntity> findFreeTimeByFieldOwnerTypeAndDate(int fieldOwnerId, int fieldTypeId, String dateString) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldType = fieldTypeServices.findById(fieldTypeId);
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
        if (timeSlotRepository.countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(fieldOwner, fieldType, targetDate, true) == 0) {
            List<TimeSlotEntity> timeSlotEntityList = createTimeSlotForDate(targetDate, fieldOwner, fieldType);
            List<TimeSlotEntity> returnTimeSlotEntityList = new ArrayList<>();
            returnTimeSlotEntityList.add(timeSlotEntityList.get(0));
            return returnTimeSlotEntityList;
        } else {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(fieldOwner,
                    fieldType, targetDate, false, true);
            List<TimeSlotEntity> savedTimeSlotEntityList = new ArrayList<>();
            while (timeSlotEntityList.size() != 0) {
                TimeSlotEntity savedTimeSlotEntity = timeSlotEntityList.get(0);
                if (timeSlotEntityList.size() == 1) {
                    savedTimeSlotEntityList.add(savedTimeSlotEntity);
                    break;
                }
                for (int i = 1; i < timeSlotEntityList.size(); i++) {
                    if (timeSlotEntityList.get(i).getStartTime().equals(savedTimeSlotEntity.getStartTime())
                            && timeSlotEntityList.get(i).getEndTime().after(savedTimeSlotEntity.getEndTime())) {
                        savedTimeSlotEntity = timeSlotEntityList.get(i);
                    }
                }
                savedTimeSlotEntityList.add(savedTimeSlotEntity);
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
        return timeSlotRepository.findByIdAndStatus(timeSlotId, true);
    }

    public TimeSlotEntity setTimeForTimeSlot(int timeSlotId, int fieldId) {
        TimeSlotEntity timeSlotEntity = findById(timeSlotId);
        FieldEntity fieldEntity = fieldServices.findFieldEntityById(fieldId);
        timeSlotEntity.setFieldId(fieldEntity);
        return timeSlotRepository.save(timeSlotEntity);
    }


}
