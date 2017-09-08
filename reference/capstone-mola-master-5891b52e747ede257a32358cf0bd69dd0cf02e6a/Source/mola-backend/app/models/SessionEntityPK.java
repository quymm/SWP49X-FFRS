package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
public class SessionEntityPK implements Serializable {
    private int id;
    private int timeSlotId;
    private int lessonId;
    private String username;

    @Column(name = "ID")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "TimeSlot_ID")
    @Id
    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    @Column(name = "Lesson_ID")
    @Id
    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    @Column(name = "Username")
    @Id
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

        SessionEntityPK that = (SessionEntityPK) o;

        if (id != that.id) return false;
        if (timeSlotId != that.timeSlotId) return false;
        if (lessonId != that.lessonId) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + timeSlotId;
        result = 31 * result + lessonId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
