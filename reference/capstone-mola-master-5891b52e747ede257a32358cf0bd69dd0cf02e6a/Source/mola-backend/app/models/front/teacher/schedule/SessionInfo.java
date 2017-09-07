package models.front.teacher.schedule;

import models.*;
import models.front.UserInfo;

/**
 * Created by stark on 06/06/2017.
 */
public class SessionInfo {
    private UserInfo learner;
    private UserInfo teacher;
    private SessionEntity session;
    private TimeSlotEntity timeSlot;
    private LessonEntity lesson;
    private ChapterEntity chapter;
    private CourseEntity course;
    private boolean isFree;

    public UserInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(UserInfo teacher) {
        this.teacher = teacher;
    }

    public UserInfo getLearner() {
        return learner;
    }

    public void setLearner(UserInfo learner) {
        this.learner = learner;
    }

    public SessionEntity getSession() {
        return session;
    }

    public void setSession(SessionEntity session) {
        this.session = session;
    }

    public TimeSlotEntity getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotEntity timeSlot) {
        this.timeSlot = timeSlot;
    }

    public LessonEntity getLesson() {
        return lesson;
    }

    public void setLesson(LessonEntity lesson) {
        this.lesson = lesson;
    }

    public ChapterEntity getChapter() {
        return chapter;
    }

    public void setChapter(ChapterEntity chapter) {
        this.chapter = chapter;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
