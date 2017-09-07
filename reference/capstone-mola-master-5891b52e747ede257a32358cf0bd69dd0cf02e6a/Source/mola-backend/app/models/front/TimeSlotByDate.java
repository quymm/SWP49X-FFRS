package models.front;

import models.TimeSlotEntity;

import java.util.List;

/**
 * Created by NGOCHIEU on 2017-06-22.
 */
public class TimeSlotByDate {
    private String date;
    private List<TimeSlotEntity> listTimeSlot;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TimeSlotEntity> getListTimeSlot() {
        return listTimeSlot;
    }

    public void setListTimeSlot(List<TimeSlotEntity> listTimeSlot) {
        this.listTimeSlot = listTimeSlot;
    }


}
