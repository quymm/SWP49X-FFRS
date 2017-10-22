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
                    return timeSlotRepository.save(timeSlotEntity2);
                } else if (timeSlotEntity.getStartTime().before(startTime) && timeSlotEntity.getEndTime().equals(startTime)) {
                    // khoảng thời gian trước giờ đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            timeSlotEntity.getStartTime(), startTime, Float.valueOf(0), false, true);
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity1);
                    return timeSlotRepository.save(timeSlotEntity2);
                } else if (timeSlotEntity.getStartTime().equals(startTime) && timeSlotEntity.getEndTime().after(endTime)) {
                    // khoảng thời gian đặt
                    TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            startTime, endTime, price, true, true);
                    // khoảng thời gian sau giờ đặt
                    TimeSlotEntity timeSlotEntity2 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
                            endTime, timeSlotEntity.getEndTime(), Float.valueOf(0), false, true);
                    timeSlotRepository.delete(timeSlotEntity);
                    timeSlotRepository.save(timeSlotEntity2);
                    return timeSlotRepository.save(timeSlotEntity1);
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

}
