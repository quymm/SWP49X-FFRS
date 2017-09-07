package models.front;

import models.RatingCourseEntity;
import models.RatingTeacherEntity;
import models.UserEntity;

/**
 * Created by NHAT QUANG on 6/28/2017.
 */
public class BRating {
    private UserEntity user;
    private RatingTeacherEntity ratingTeacher;
    private RatingCourseEntity ratingCourse;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RatingTeacherEntity getRatingTeacher() {
        return ratingTeacher;
    }

    public void setRatingTeacher(RatingTeacherEntity ratingTeacher) {
        this.ratingTeacher = ratingTeacher;
    }

    public RatingCourseEntity getRatingCourse() {
        return ratingCourse;
    }

    public void setRatingCourse(RatingCourseEntity ratingCourse) {
        this.ratingCourse = ratingCourse;
    }
}
