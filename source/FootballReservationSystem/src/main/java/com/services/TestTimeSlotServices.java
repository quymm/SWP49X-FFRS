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

        public boolean mergeTimeSlot(String date, int fieldOwnerId, int fieldTypeId){
            List<TimeSlotEntity> listTimeSlotEntity = findTimeSlotByDate(date, fieldOwnerId, fieldTypeId);
            while (listTimeSlotEntity.size()>0) {
                listTimeSlotEntity = doMergeTimeSlotInList(listTimeSlotEntity);
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
                int s = DateTimeUtils.timeConvert(tSlotEntity.getStartTime());
                int e = DateTimeUtils.timeConvert(tSlotEntity.getEndTime());
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
