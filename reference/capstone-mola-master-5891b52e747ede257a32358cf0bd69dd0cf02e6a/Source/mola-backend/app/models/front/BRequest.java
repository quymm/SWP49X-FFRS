package models.front;

import models.*;

import java.sql.Timestamp;

/**
 * Created by NHAT QUANG on 6/27/2017.
 */
public class BRequest {
    private ChapterEntity chapter;
    private CourseEntity course;
    private UserEntity learner;
    private LessonEntity lesson;
    private SessionEntity session;
    private TimeSlotEntity timeSlot;
    private TeacherEntity teacher;
    private UserEntity teacherInfo;
    private RegisterCourseEntity registerCourse;
    private Timestamp time;
    private boolean sessionReq;

    public UserEntity getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(UserEntity teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public RegisterCourseEntity getRegisterCourse() {
        return registerCourse;
    }

    public void setRegisterCourse(RegisterCourseEntity registerCourse) {
        this.registerCourse = registerCourse;
    }

    public boolean isSessionReq() {
        return sessionReq;
    }

    public void setSessionReq(boolean sessionReq) {
        this.sessionReq = sessionReq;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
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

    public UserEntity getLearner() {
        return learner;
    }

    public void setLearner(UserEntity learner) {
        this.learner = learner;
    }

    public LessonEntity getLesson() {
        return lesson;
    }

    public void setLesson(LessonEntity lesson) {
        this.lesson = lesson;
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
}
