package models.front;

import models.CourseEntity;
import models.RegisterCourseEntity;
import models.TeacherEntity;
import models.UserEntity;

/**
 * Created by NHAT QUANG on 6/23/2017.
 */
public class RegisterCourseInfo {
    CourseEntity course;
    RegisterCourseEntity registerCourse;
    TeacherEntity teacher;
    UserEntity user;

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public RegisterCourseEntity getRegisterCourse() {
        return registerCourse;
    }

    public void setRegisterCourse(RegisterCourseEntity registerCourse) {
        this.registerCourse = registerCourse;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
