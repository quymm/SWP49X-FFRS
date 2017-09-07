package models.front;

import models.*;

/**
 * Created by NGOCHIEU on 2017-06-21.
 */
public class BSession {
    private UserEntity userEntity;
    private TeacherEntity teacherEntity;
    private CourseEntity courseEntity;
    private SessionEntity sessionEntity;
    private TimeSlotEntity timeSlotEntity;
    private UserEntity learner;
    private boolean teaching;
    private boolean onTime;

    public boolean isOnTime() {
        return onTime;
    }

    public void setOnTime(boolean onTime) {
        this.onTime = onTime;
    }

    public UserEntity getLearner() {
        return learner;
    }

    public void setLearner(UserEntity learner) {
        this.learner = learner;
    }

    public boolean isTeaching() {
        return teaching;
    }

    public void setTeaching(boolean teaching) {
        this.teaching = teaching;
    }

    public TimeSlotEntity getTimeSlotEntity() {
        return timeSlotEntity;
    }

    public BSession setTimeSlotEntity(TimeSlotEntity timeSlotEntity) {
        this.timeSlotEntity = timeSlotEntity;
        return this;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public BSession setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;

    }

    public TeacherEntity getTeacherEntity() {
        return teacherEntity;
    }

    public BSession setTeacherEntity(TeacherEntity teacherEntity) {
        this.teacherEntity = teacherEntity;
        return this;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public BSession setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
        return this;
    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public BSession setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
        return this;

    }
}
