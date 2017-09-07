package dao;

import models.RatingTeacherEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by rocks on 6/13/2017.
 */
public class RatingTeacherDAO {
    public static List<RatingTeacherEntity> getRatingsByTeacherId(int teacherId) {
        String query = "SELECT r FROM RatingTeacherEntity r WHERE r.teacherId = :teacherId";
        return JPA.em().createQuery(query).setParameter("teacherId", teacherId).getResultList();
    }

    public static List<RatingTeacherEntity> getRatingsByTeacherId(int teacherId, EntityManager em) {
        String query = "SELECT r FROM RatingTeacherEntity r WHERE r.teacherId = :teacherId";
        return em.createQuery(query).setParameter("teacherId", teacherId).getResultList();
    }

    public static void addRatingTeacher(RatingTeacherEntity ratingTeacherEntity){
        JPA.em().persist(ratingTeacherEntity);
    }
}
