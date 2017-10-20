package com.services;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeSlotEntity;
import com.services.TestTimeSlotServices;
import com.dto.CordinationPoint;
import com.utils.DateTimeUtils;
import com.utils.MapUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

public class Demo {
    public static void main(String[] args) throws IOException, ParseException {
//        int ch;
//        String abc = "";
//        System.out.print("Input date ");
//        while((ch = System.in.read()) != '\n'){
//            abc += (char) ch;
//        }
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EE");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//        String b = "10.8567888";
//        Double bDouble = Double.parseDouble(b);

//        CordinationPoint cordinationPoint1 = new CordinationPoint();
//        cordinationPoint1.setLongitude("10.797507");
//        cordinationPoint1.setLatitude("106.675181");
//
//        CordinationPoint cordinationPoint2 = new CordinationPoint();
//        cordinationPoint2.setLongitude("10.796088");
//        cordinationPoint2.setLatitude("106.677798");

//        System.out.println(MapUtils.calculateDistanceBetweenTwoPoint(cordinationPoint1, cordinationPoint2));

//        Date date = DateTimeUtils.convertFromStringToDate(abc);
//        Date dateTime = DateTimeUtils.convertFromStringToDateTime(abc);
//        System.out.println(bDouble);
//        System.out.println(format.format(dateTime));
//        System.out.println(timeFormat.format(dateTime));

//        TestTimeSlotServices testTimeSlotServices;
//        List<TimeSlotEntity> list = findTimeSlotByDate("10-12-2017", 1, 1);
//        mergeTimeSlot(list);
    }

//    public static List<TimeSlotEntity> findTimeSlotByDate(String dateString, int fieldOwnerId, int fieldTypeId) {
//        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
//        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
//        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
//        return timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(accountEntity, fieldTypeEntity, targetDate, false, true);
//    }
//
//    public static TimeSlotEntity mergeTimeSlot(List<TimeSlotEntity> listTimeSlotEntity) {
////                AccountEntity fieldOwner = accountServices.findAccountEntityById(inputReservationDTO.getFieldOwnerId(), "owner");
////                FieldTypeEntity fieldType = fieldTypeServices.findById(inputReservationDTO.getFieldTypeId());
////                Date targetDate = DateTimeUtils.convertFromStringToDate(inputReservationDTO.getDate());
////                TimeSlotEntity timeSlotEntity1 = new TimeSlotEntity(fieldOwner, fieldType, targetDate,
////                        timeSlotEntity.getStartTime(), startTime, Float.valueOf(0), false, true);
//        TimeSlotEntity timeSlotEntity = listTimeSlotEntity.get(0);
//        TimeSlotEntity lastTimeSlotEntity = listTimeSlotEntity.get(listTimeSlotEntity.size()-1);
//        Date endTime = lastTimeSlotEntity.getEndTime();
//        timeSlotEntity.setEndTime(endTime);
//        for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
//            timeSlotRepository.delete(tSlotEntity);
//        }
//        return timeSlotRepository.save(timeSlotEntity);
//    }
}
