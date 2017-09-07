package dao;

import models.RatingCourseEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Created by rocks on 6/9/2017.
 */
public class RatingCourseDAO implements Serializable {
    public static List<RatingCourseEntity> getAllRatings() {
        List<RatingCourseEntity> result = JPA.em().createQuery("SELECT r FROM RatingCourseEntity r").getResultList();
        return result;
    }

    public static List<RatingCourseEntity> getAllRatings(EntityManager em) {
        List<RatingCourseEntity> result = em.createQuery("SELECT r FROM RatingCourseEntity r").getResultList();
        return result;
    }

    public static List<RatingCourseEntity> getRatingsByCourseId(int courseId) {
        String query = "SELECT r FROM RatingCourseEntity r WHERE r.courseId = :courseId";
        return JPA.em().createQuery(query).setParameter("courseId", courseId).getResultList();
    }

    public static List<RatingCourseEntity> getRatingsByCourseId(int courseId, EntityManager em) {
        String query = "SELECT r FROM RatingCourseEntity r WHERE r.courseId = :courseId";
        return em.createQuery(query).setParameter("courseId", courseId).getResultList();
    }
}
