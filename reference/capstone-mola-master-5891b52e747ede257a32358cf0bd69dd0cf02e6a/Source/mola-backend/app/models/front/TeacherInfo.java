package models.front;

import models.TeacherEntity;
import models.UserEntity;

/**
 * Created by NHAT QUANG on 7/11/2017.
 */
public class TeacherInfo {
    private UserEntity userEntity;
    private TeacherEntity teacherEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public TeacherEntity getTeacherEntity() {
        return teacherEntity;
    }

    public void setTeacherEntity(TeacherEntity teacherEntity) {
        this.teacherEntity = teacherEntity;
    }
}
