package dao;

import models.CourseEntity;
import models.TeacherEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by NHAT QUANG on 6/6/2017.
 */
public class CourseDAO extends BaseDAO<CourseEntity, Integer> {
    public CourseDAO() {
        setClazz(CourseEntity.class);
    }

    public static List<CourseEntity> getByTeacher(String username) {
        return JPA.em().createQuery("SELECT c FROM UserEntity u, TeacherEntity t, CourseEntity c where u.username = t.username and c.teacherId = t.id and u.username = :username AND c.active = TRUE ")
                .setParameter("username", username)
                .getResultList();
    }

    public static List<CourseEntity> getByTeacher(String username, EntityManager em) {
        return em.createQuery("SELECT c FROM UserEntity u, TeacherEntity t, CourseEntity c where u.username = t.username and c.teacherId = t.id and u.username = :username AND c.active = TRUE ")
                .setParameter("username", username)
                .getResultList();
    }

    public static CourseEntity createCourse(CourseEntity c) {
        JPA.em().persist(c);
        return c;
    }

    public static CourseEntity getCourseById(int id) {
        String query = "SELECT t from CourseEntity t WHERE t.id = :id";
        try {
            return (CourseEntity) JPA.em().createQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static CourseEntity getCourseById(int id, EntityManager em) {
        String query = "SELECT t from CourseEntity t WHERE t.id = :id";
        try {
            return (CourseEntity) em.createQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<CourseEntity> getByLanguage(String language) {
        return JPA.em().createQuery("SELECT c FROM CourseEntity c WHERE c.language = :language")
                .setParameter("language", language)
                .getResultList();
    }

    public static List<CourseEntity> getByLanguage(String language, EntityManager em) {
        return em.createQuery("SELECT c FROM CourseEntity c WHERE c.language = :language")
                .setParameter("language", language)
                .getResultList();
    }

    public TeacherEntity findTeacherByCourse(CourseEntity course) {
        int courseId = course.getId();
        String query = "SELECT t FROM CourseEntity c, TeacherEntity t WHERE c.teacherId = t.id AND c.id = :courseId";
        try {
            return (TeacherEntity) JPA.em().createQuery(query).setParameter("courseId", courseId).getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<CourseEntity> getRegisteredCourses(String username) {
        return JPA.em().createQuery("SELECT c from RegisterCourseEntity r, CourseEntity c " +
                "WHERE r.username = :username AND r.status ='ACCEPTED' AND c.id = r.courseId")
                .setParameter("username", username).getResultList();
    }

    public static List<CourseEntity> getRegisteredCourses(String username, EntityManager em) {
        return em.createQuery("SELECT c from RegisterCourseEntity r, CourseEntity c " +
                "WHERE r.username = :username AND r.status ='ACCEPTED' AND c.id = r.courseId")
                .setParameter("username", username).getResultList();
    }
}
