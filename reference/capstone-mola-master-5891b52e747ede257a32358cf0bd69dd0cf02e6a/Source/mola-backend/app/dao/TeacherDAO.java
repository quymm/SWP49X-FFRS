package dao;

import models.*;
import models.front.MonthlyRevenue;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
public class TeacherDAO extends BaseDAO<TeacherEntity, Long> {
    public TeacherDAO() {
        this.setClazz(TeacherEntity.class);
    }

    @Override
    protected void setClazz(Class<TeacherEntity> clazzToSet) {
        super.setClazz(clazzToSet);
    }

    public TeacherEntity findTeacherByUsername(String username) {
        String query = "SELECT t from TeacherEntity t WHERE t.username = :username";
        try {
            return (TeacherEntity) JPA.em().createQuery(query)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

public static TeacherEntity getTeacherByUsername(String username){
        List<TeacherEntity> listTeacher = JPA.em().createQuery("select t from TeacherEntity t,UserEntity u where t.username = u.username and u.username = :username")
                .setParameter("username", username)
                .getResultList();
        if (listTeacher == null || listTeacher.isEmpty()){
            return null;
        }
        return listTeacher.get(0);
    }
    public static TeacherEntity getTeacherByUsername(String username, EntityManager em){
        List<TeacherEntity> listTeacher = em.createQuery("select t from TeacherEntity t,UserEntity u where t.username = u.username and u.username = :username")
                .setParameter("username", username)
                .getResultList();
        if (listTeacher == null || listTeacher.isEmpty()){
            return null;
        }
        return listTeacher.get(0);
    }

    public static TeacherEntity getTeacherById(int id) {
        String query = "SELECT t from TeacherEntity t WHERE t.id = :id";
        try {
            return (TeacherEntity) JPA.em().createQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public static TeacherEntity getTeacherByIdWithEM(int id, EntityManager em) {
        String query = "SELECT t from TeacherEntity t WHERE t.id = :id";
        try {
            return (TeacherEntity) em.createQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static TeacherEntity getTeacherById(int id, EntityManager em) {
        String query = "SELECT t from TeacherEntity t WHERE t.id = :id";
        try {
            return (TeacherEntity) em.createQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static long getStudentsCount(int teacherId){
        return (long) JPA.em().createQuery("SELECT count(r.username) FROM CourseEntity c, RegisterCourseEntity r " +
                "WHERE r.courseId = c.id AND c.teacherId = :teacherId")
                .setParameter("teacherId", teacherId)
                .getSingleResult();
    }
    public static long getCourseCount(int teacherId){
        return (long) JPA.em().createQuery("SELECT count(c) FROM CourseEntity c WHERE c.teacherId = :teacherId")
                .setParameter("teacherId", teacherId)
                .getSingleResult();
    }
    public static List<LanguageSpeakEntity> getLanguageSpeak(int teacherId){
        return JPA.em().createQuery("SELECT l FROM LanguageSpeakEntity l, UserEntity u, TeacherEntity t " +
                "WHERE l.username = u.username AND t.username = u.username AND t.id = :teacherId")
                .setParameter("teacherId", teacherId)
                .getResultList();
    }
    public static List<LanguageTeachEntity> getLanguageTeach(int teacherId){
        return JPA.em().createQuery("SELECT l FROM LanguageTeachEntity l WHERE l.teacherId = :teacherId AND l.status = 'ACCEPTED'")
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    public static List<UserEntity> getLanguageTeachRequest(){
        return JPA.em().createQuery("SELECT DISTINCT u FROM LanguageTeachEntity l, TeacherEntity t, UserEntity u " +
                "WHERE l.status = 'PENDING' AND l.teacherId = t.id AND t.username = u.username")
                .getResultList();
    }

    public static List<LanguageTeachEntity> getLanguageTeachRequestList(int teacherId){
        return JPA.em().createQuery("SELECT l FROM LanguageTeachEntity l WHERE l.teacherId = :teacherId AND l.status = 'PENDING'")
                .setParameter("teacherId", teacherId)
                .getResultList();
    }


    public static UserEntity getTeacherByCourse(CourseEntity course) {
        List<UserEntity> result = JPA.em().createQuery("SELECT u FROM UserEntity u, TeacherEntity t, CourseEntity c " +
                "WHERE u.username = t.username AND t.id = c.teacherId AND c.id = :courseId")
                .setParameter("courseId", course.getId())
                .getResultList();
        if (result.isEmpty()){
            return null;
        }
        return result.get(0);
    }
    public static UserEntity getTeacheProfile(int teacherId){
        List<UserEntity> result = JPA.em().createQuery("SELECT u FROM UserEntity u, TeacherEntity t " +
                "WHERE u.username = t.username AND t.id = :teacherId")
                .setParameter("teacherId", teacherId)
                .getResultList();
        if (result.isEmpty()){
            return null;
        }
        return result.get(0);
    }

    public static List<LanguageTeachEntity> getLanguageTeachClip(int teacherId){
        return JPA.em().createQuery("SELECT l FROM LanguageTeachEntity l WHERE l.teacherId = :teacherId AND l.status = 'ACCEPTED'")
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    public static boolean getLearned(String username, int teacherId) {
        return (boolean) JPA.em().createQuery("SELECT (COUNT(s) > 0) FROM SessionEntity s, TimeSlotEntity t " +
                "WHERE s.timeSlotId = t.id AND s.username = :username AND t.teacherId = :teacherId AND s.status = 'FINISHED' ")
                .setParameter("username", username)
                .setParameter("teacherId", teacherId).getSingleResult();
    }
    public static List<MonthlyRevenue> getMonthlyRevenue(int teacherId){
        return JPA.em().createQuery("SELECT NEW models.front.MonthlyRevenue(sum(c.price * l.duration), month(ts.starTime)) FROM SessionEntity s, TeacherEntity t, TimeSlotEntity ts, LessonEntity l, CourseEntity c, ChapterEntity ch " +
                "WHERE s.status = 'FINISHED' " +
                "AND t.id = :teacherId " +
                "AND s.timeSlotId = ts.id AND s.lessonId = l.id AND l.chapterId = ch.id AND ch.courseId = c.id " +
                "AND year(ts.starTime) = year(current_date()) " +
                "GROUP BY month(ts.starTime)", MonthlyRevenue.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }
}