package dao;

import models.RegisterCourseEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by rocks on 6/13/2017.
 */
public class RegisterCourseDAO {

    public static int countByCourseId(int courseId) {
        String query = "SELECT r FROM RegisterCourseEntity r WHERE r.courseId = :courseId";
        return JPA.em().createQuery(query).setParameter("courseId", courseId).getResultList().size();
    }

    public static int countByCourseId(int courseId, EntityManager em) {
        String query = "SELECT r FROM RegisterCourseEntity r WHERE r.courseId = :courseId";
        return em.createQuery(query).setParameter("courseId", courseId).getResultList().size();
    }

    public static int countByTeacherId(int teacherId) {
        String query = "SELECT r " +
                "FROM RegisterCourseEntity r, CourseEntity c " +
                "WHERE r.courseId = c.id AND c.teacherId = :teacherId";
        return JPA.em().createQuery(query).setParameter("teacherId", teacherId).getResultList().size();
    }

    public static int countByTeacherId(int teacherId, EntityManager em) {
        String query = "SELECT r " +
                "FROM RegisterCourseEntity r, CourseEntity c " +
                "WHERE r.courseId = c.id AND c.teacherId = :teacherId";
        return em.createQuery(query).setParameter("teacherId", teacherId).getResultList().size();
    }
    public static List<RegisterCourseEntity> getCourseRegisted(String username){
        return JPA.em().createQuery("SELECT r from RegisterCourseEntity r WHERE r.username = :username AND r.status ='ACCEPTED'")
                .setParameter("username", username).getResultList();
    }


    public static String enrollUserToCourse(String username, int courseId) {
        RegisterCourseEntity enroll = new RegisterCourseEntity();
        enroll.setUsername(username);
        enroll.setCourseId(courseId);
        enroll.setStatus("PENDING");
        enroll.setTimeRequest(new Timestamp((new Date()).getTime()));

        JPA.em().merge(enroll);
        return enroll.getStatus();
    }

    public static String checkEnrolledUserInCourse(String username, int courseId){
        String query = "SELECT r FROM RegisterCourseEntity r WHERE r.username = :username AND r.courseId = :courseId";
        List<RegisterCourseEntity> registerCourseEntities = JPA.em().createQuery(query).setParameter("username", username)
                .setParameter("courseId", courseId).getResultList();
        if (registerCourseEntities.isEmpty()) {
            return "UNENROLLED";
        }
        return registerCourseEntities.get(0).getStatus();
    }

    public static List<RegisterCourseEntity> getIncomingCourseRequest(String username){
        return JPA.em().createQuery("SELECT r FROM RegisterCourseEntity r, CourseEntity c, TeacherEntity t " +
                "WHERE r.courseId = c.id AND c.teacherId = t.id AND t.username = :username " +
                "AND r.status = 'PENDING'")
                .setParameter("username", username)
                .getResultList()
                ;
    }
    public static List<RegisterCourseEntity> getSendingCourseRequest(String username){
        return JPA.em().createQuery("SELECT r FROM RegisterCourseEntity r WHERE r.status = 'PENDING' AND r.username = :username")
                .setParameter("username", username)
                .getResultList()
                ;
    }

}
