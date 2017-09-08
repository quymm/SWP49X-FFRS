package models.front;

import models.CourseEntity;
import models.TeacherEntity;
import models.UserEntity;

/**
 * Created by stark on 14/06/2017.
 */
public class BSearchResultInfo {
    private CourseEntity course;
    private TeacherEntity teacher;
    private UserEntity userInfo;



    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public UserEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserEntity userInfo) {
        this.userInfo = userInfo;
    }

    public BSearchResultInfo() {
    }

    public BSearchResultInfo(CourseEntity courses, TeacherEntity teacher, UserEntity userInfo) {
        this.course = courses;
        this.teacher = teacher;
        this.userInfo = userInfo;
    }
}
