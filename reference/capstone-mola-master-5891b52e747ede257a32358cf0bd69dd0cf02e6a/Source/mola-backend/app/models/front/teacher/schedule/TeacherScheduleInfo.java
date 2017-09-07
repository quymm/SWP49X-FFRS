package models.front.teacher.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.TimeSlotEntity;

/**
 * Created by stark on 07/06/2017.
 */
public class TeacherScheduleInfo {
    @JsonProperty("sessions")
    private SessionInfo[] sessionInfos;
    private TimeSlotEntity[] timeSlots;

    public SessionInfo[] getSessionInfos() {
        return sessionInfos;
    }

    public void setSessionInfos(SessionInfo[] sessionInfos) {
        this.sessionInfos = sessionInfos;
    }

    public TimeSlotEntity[] getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(TimeSlotEntity[] timeSlots) {
        this.timeSlots = timeSlots;
    }

    public TeacherScheduleInfo() {
    }

    public TeacherScheduleInfo(SessionInfo[] sessionInfos, TimeSlotEntity[] timeSlots) {
        this.sessionInfos = sessionInfos;
        this.timeSlots = timeSlots;
    }
}
