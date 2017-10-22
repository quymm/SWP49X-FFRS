package com.services;

import com.entity.*;
import com.repository.TimeSlotRepository;
import com.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TestTimeSlotServices {
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

    public List<TimeSlotEntity> findTimeSlotByDate(String dateString, int fieldOwnerId, int fieldTypeId) {
        Date targetDate = DateTimeUtils.convertFromStringToDate(dateString);
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findById(fieldTypeId);
        return timeSlotRepository.findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(accountEntity, fieldTypeEntity, targetDate, false, true);
    }

    //        public TimeSlotEntity mergeTimeSlotInList(List<TimeSlotEntity> listTimeSlotEntity) {
//                TimeSlotEntity timeSlotEntity = listTimeSlotEntity.get(0);
//                TimeSlotEntity lastTimeSlotEntity = listTimeSlotEntity.get(listTimeSlotEntity.size()-1);
//                Date endTime = lastTimeSlotEntity.getEndTime();
//                timeSlotEntity.setEndTime(endTime);
//                for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
//                        timeSlotRepository.delete(tSlotEntity);
//                }
//                return timeSlotRepository.save(timeSlotEntity);
//        }
    public void deleteTimeSlotInList(List<TimeSlotEntity> listTimeSlotEntity) {
        for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
            timeSlotRepository.delete(tSlotEntity);
        }
    }

    public boolean mergeTimeSlot(String date, int fieldOwnerId, int fieldTypeId) {
        List<TimeSlotEntity> listTimeSlotEntity = findTimeSlotByDate(date, fieldOwnerId, fieldTypeId);
        while (listTimeSlotEntity.size() > 0) {
            listTimeSlotEntity = doMergeTimeSlotInList(listTimeSlotEntity);
        }
        return true;
    }

    public boolean mergeTimeSlotInList(List<TimeSlotEntity> listTimeSlotEntity) {
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
            while ((i + 1 < 48) && (timeSlotArray[i + 1]> 0)) {
                i++;
            }
            e = i;

            Date startTime = DateTimeUtils.intToTimeInTimeSlot(s);
            Date endtTime = DateTimeUtils.intToTimeInTimeSlot(e+1);
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
                for (int j = s; j<=e; j++){
                    timeSlotArray[j]--;
                    t--;
                }
            } else {//co thang timeslot nay ko bi thay doi, remove khoi list, ko xet no nua
                listTimeSlotEntity.remove(check);
                for (int j = s; j<=e; j++){
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



    public List<TimeSlotEntity> doMergeTimeSlotInList(List<TimeSlotEntity> listTimeSlotEntity){
//            int n = timeList.size();
        int[] trace = new int[48];
        int[] h = new int[48];
        long max = 0; int p = 0;
        //khoi tao cho h va trace
        for(int i=0; i<48; i++){
            h[i] = 0;
            trace[i] = i;
        }
        int[][] c = new int[48][48];
        for(int i=0; i<48; i++){
            for (int j=0; j<48; j++){
                c[i][j] = 0;
            }
        }
        for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
            int s = DateTimeUtils.timeToIntInTimeSlot(tSlotEntity.getStartTime());
            int e = DateTimeUtils.timeToIntInTimeSlot(tSlotEntity.getEndTime());
            int duration = e - s;
            c[s][e] = listTimeSlotEntity.indexOf(tSlotEntity);
//              dung canh s-d de toi uu cho duong di toi d
            if (h[e]<h[s]+duration){
                h[e]=h[s]+duration;
                trace[e]=s;
                if (h[e]>max) {
                    max = h[e]; // gia tri cua duong di dai nhat
                    p = e; //position - dinh cuoi cung cua duong di dai nhat
                }
            }
        }
        List<TimeSlotEntity> listDeleteTimeSlotEntity = new ArrayList<>();
        TimeSlotEntity lastTimeSlotEntity = listTimeSlotEntity.get(c[trace[p]][p]);
        //list delete time slot : nhung time slot trong nay se dc noi lai, xoa bo nhung time slot nay di
        while (trace[p]!=p) {
            int index = c[trace[p]][p];
            listDeleteTimeSlotEntity.add(listTimeSlotEntity.get(index));
            p = trace[p];
        }

//                timeslot moi dc tao ra la timeslot cuoi cung cua listdeletetimeslot
//                khi trace thang dau tien trong day time slot dc noi bi xep o cuoi cung do trace nguoc tu duoi len dau
//                timeslot dc tao ra la timeslot cuoi cung cua chuoi lon nhat,voi start time la start time cua thang dau tien
        Date startTime = listDeleteTimeSlotEntity.get(listDeleteTimeSlotEntity.size()-1).getStartTime();
        lastTimeSlotEntity.setStartTime(startTime);

        if(listDeleteTimeSlotEntity.size()==1){
            listTimeSlotEntity.remove(lastTimeSlotEntity);
        } else {
            deleteTimeSlotInList(listDeleteTimeSlotEntity);
            timeSlotRepository.save(lastTimeSlotEntity);
            for (TimeSlotEntity tSlotEntity : listDeleteTimeSlotEntity) {
                listTimeSlotEntity.remove(tSlotEntity);
            }
        }
        return listTimeSlotEntity;
    }
}