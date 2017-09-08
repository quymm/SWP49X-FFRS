package service;

import dao.RatingTeacherDAO;
import dao.TeacherDAO;
import models.RatingTeacherEntity;
import models.TeacherEntity;
import models.UserEntity;
import models.front.BRating;
import play.db.jpa.JPA;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by NHAT QUANG on 6/28/2017.
 */
public class RatingService {
    public static List<BRating> getListRatingTeacher(int teacherId){
        List<RatingTeacherEntity> ratingTeacher = RatingTeacherDAO.getRatingsByTeacherId(teacherId);
        List<BRating> result = new ArrayList<>();
        for(RatingTeacherEntity r: ratingTeacher){
            BRating rating = new BRating();
            rating.setRatingTeacher(r);
            rating.setUser(JPA.em().find(UserEntity.class, r.getUsername()));
            result.add(rating);
        }
        Collections.sort(result, (o1, o2) -> o2.getRatingTeacher().getTime().compareTo(o1.getRatingTeacher().getTime()));
        return result;
    }

    public static void ratingTeacher(int rate, String username, String comment, int teacherId){
        RatingTeacherEntity ratingTeacher = new RatingTeacherEntity();
        ratingTeacher.setComment(comment);
        ratingTeacher.setRating(rate);
        ratingTeacher.setTeacherId(teacherId);
        ratingTeacher.setUsername(username);
        ratingTeacher.setTime(new Timestamp(new Date().getTime()));
        RatingTeacherDAO.addRatingTeacher(ratingTeacher);

        TeacherEntity teacher = TeacherDAO.getTeacherById(teacherId);
        teacher.addRating(rate);
        JPA.em().merge(teacher);
    }

}
