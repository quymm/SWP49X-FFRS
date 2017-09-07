package models.front;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by NGOCHIEU on 2017-07-18.
 */
public class ConflictSession {
    private Date startTime;
    private Date endTime;
    private String lessonTitle;
    private String teacherName;


    public ConflictSession(Date startTime, Date endTime, String lessonTitle, String teacherName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.lessonTitle = lessonTitle;
        this.teacherName = teacherName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }


    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
