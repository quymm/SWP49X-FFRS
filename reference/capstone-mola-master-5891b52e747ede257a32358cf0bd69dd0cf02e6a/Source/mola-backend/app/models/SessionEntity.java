package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "Session", schema = "mola", catalog = "")
public class SessionEntity {
    private int id;
    private int timeSlotId;
    private int lessonId;
    private String status;
    private String username;
    private Timestamp timeRequest;
    private String message;
    private String sessionRoom;
    private boolean notifyStatus;

    @Column(name = "SessionRoom")
    public String getSessionRoom() {
        return sessionRoom;
    }

    public void setSessionRoom(String sessionRoom) {
        this.sessionRoom = sessionRoom;
    }

    @Basic
    @Column(name = "NotifyStatus")
    public boolean getNotifyStatus() {
        return notifyStatus;
    }
    public void setNotifyStatus(boolean notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    @Basic
    @Column(name = "Message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "TimeRequest")
    public Timestamp getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(Timestamp timeRequest) {
        this.timeRequest = timeRequest;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TimeSlot_ID")
    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    @Basic
    @Column(name = "Lesson_ID")
    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    @Basic
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntity that = (SessionEntity) o;

        if (id != that.id) return false;
        if (timeSlotId != that.timeSlotId) return false;
        if (lessonId != that.lessonId) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + timeSlotId;
        result = 31 * result + lessonId;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
