package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "TimeSlot", schema = "mola", catalog = "")
public class TimeSlotEntity {
    private int id;
    private Timestamp starTime;
    private Timestamp endTime;
    private int teacherId;
    private String status;
    private boolean active = true;

    @Basic
    @Column(name = "Active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
    @Column(name = "StarTime")
    public Timestamp getStarTime() {
        return starTime;
    }

    public void setStarTime(Timestamp starTime) {
        this.starTime = starTime;
    }

    @Basic
    @Column(name = "EndTime")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "Teacher_ID")
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }


    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TimeSlotEntity(Timestamp starTime, Timestamp endTime, int teacherId) {
        this.starTime = starTime;
        this.endTime = endTime;
        this.teacherId = teacherId;
        this.status = "free";

    }
    public TimeSlotEntity(Date starTime, Date endTime, int teacherId) {
        this.starTime = new Timestamp(starTime.getTime());
        this.endTime = new Timestamp(endTime.getTime());
        this.teacherId = teacherId;
        this.status = "free";

    }


    public TimeSlotEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSlotEntity that = (TimeSlotEntity) o;

        if (id != that.id) return false;
        if (teacherId != that.teacherId) return false;
        if (starTime != null ? !starTime.equals(that.starTime) : that.starTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (starTime != null ? starTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + teacherId;
        return result;
    }
}
