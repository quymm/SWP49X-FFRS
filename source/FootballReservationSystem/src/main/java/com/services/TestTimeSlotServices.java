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

        public TimeSlotEntity mergeTimeSlotInList(List<TimeSlotEntity> listTimeSlotEntity) {
                TimeSlotEntity timeSlotEntity = listTimeSlotEntity.get(0);
                TimeSlotEntity lastTimeSlotEntity = listTimeSlotEntity.get(listTimeSlotEntity.size()-1);
                Date endTime = lastTimeSlotEntity.getEndTime();
                timeSlotEntity.setEndTime(endTime);
                for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
                        timeSlotRepository.delete(tSlotEntity);
                }
                return timeSlotRepository.save(timeSlotEntity);
        }
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
                while (trace[p]!=p) {
                        int index = c[trace[p]][p];
//                        timeSlotRepository.delete(listTimeSlotEntity.get(index));
                        listDeleteTimeSlotEntity.add(listTimeSlotEntity.get(index));
                        p = trace[p];
                }

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

        public boolean mergeTimeSlotInDate(String date, int fieldOwnerId, int fieldTypeId) {

                List<TimeSlotEntity> listTimeSlotEntity = findTimeSlotByDate(date, fieldOwnerId, fieldTypeId);

                //lay tat ca start time bo vo starttimelist, endtime bo vao endtimelist
                List<Date> startTime = new ArrayList<>();
                List<Date> endTime = new ArrayList<>();
                List<Date> timeList = new ArrayList<>();// tap hop cac dinh trong do thi, la tat ca cac moc thoi gian
                for (int i=0; i<listTimeSlotEntity.size() ; i++) {
                        Date s = listTimeSlotEntity.get(i).getStartTime();
                        Date e = listTimeSlotEntity.get(i).getEndTime();
                        startTime.add(s);
                        endTime.add(e);
                        if(!timeList.contains(s)){
                                timeList.add(s);
                        }
                        if(!timeList.contains(e)){
                                timeList.add(e);
                        }
                }

                Collections.sort(timeList); //sap xep cac moc thoi gian theo thu tu tang dan
                //tao tap hop cac dinh trong do thi, la cac moc thoi gian


                //khoi tao mang 2 chieu a[starttime][endtime], time slot nao co thi a[starttime][endtime] = endtime-starttime
                int n = timeList.size();
                long[][] a = new long[n][n];
                int[][] trace = new int[n][n];
                for(int i=0; i<n; i++){
                        for(int j=i; j<n ; j++){
                                for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
                                        boolean test = tSlotEntity.getStartTime().equals(startTime.get(i));
                                        boolean test2 = tSlotEntity.getEndTime().equals(endTime.get(j));
                                        if((tSlotEntity.getStartTime().equals(startTime.get(i)))&&(tSlotEntity.getEndTime().equals(endTime.get(j)))){
//                                                long duration = (long) ((DateTimeUtils.convertFromStringToTime(endTime.get(j)) - startTime.get(i)) / 60000);
                                                long duration = (long) ((endTime.get(j).getTime() - startTime.get(i).getTime()) / 3600000);
                                                a[i][j] = duration;
                                        } else {
                                                a[i][j] = -100;
                                        }
                                        trace[i][j] = j;
                                }
                        }
                }
//                for (TimeSlotEntity tSlotEntity : listTimeSlotEntity) {
//                        if((tSlotEntity.getStartTime()==startTime.get(i))&&(tSlotEntity.getEndTime()==endTime.get(j))){
//                                long duration = (long) ((DateTimeUtils.convertFromStringToTime(endTime.get(j)) - startTime.get(i)) / 60000);
//                                                a[i][j] = duration;
//                                        } else {
//                                                a[i][j] = -100;
//                                        }
//                                        trace[i][j] = j;
//                                }
                //sort
                //for k theo tat ca cac khoan thoi gian trong time
                for(int k=0; k<n; k++){
                        for(int i=0; i<n; i++){
                                for(int j=0; j<n; j++){
                                        if (a[i][j]<a[i][k]+a[k][j]){
                                                a[i][j] = a[i][k]+a[k][j];
                                                trace[i][j] = trace[i][k];
                                        }
                                }
                        }
                }

                //sau khi sort xong, truy vet, dua ve list nhung time slot co the noi


                //noi lai time slot
                return true;
        }
}
