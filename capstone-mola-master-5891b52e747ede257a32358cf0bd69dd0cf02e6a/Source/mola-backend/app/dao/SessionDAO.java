package dao;

import models.SessionEntity;
import models.front.ConflictSession;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by NGOCHIEU on 2017-06-21.
 */
public class SessionDAO {
    public static SessionEntity findOne(int id){
        return JPA.em().find(SessionEntity.class, id);
    }
    public static SessionEntity update(SessionEntity session){
        return JPA.em().merge(session);
    }

    public static List<SessionEntity> getUpcommingSession(String username){
        return JPA.em().createNativeQuery("SELECT s.* FROM Session s, User u, TimeSlot  t, Teacher te  " +
                "WHERE s.Username = u.Username  " +
                "AND s.TimeSlot_Id = t.id  " +
                "AND t.Teacher_Id = te.id  " +
                "AND (u.Username = :username OR te.Username = :username)  " +
                "AND s.Status = 'ACCEPTED'" +
                "AND t.EndTime > current_time() " +
                "AND t.StarTime < current_date() + interval 3 day", SessionEntity.class)
                .setParameter("username", username)
                .getResultList()
                ;
    }

    public static List<SessionEntity> getListSession(Date from, Date to, String username){
        return JPA.em().createQuery("SELECT s FROM SessionEntity s, TimeSlotEntity t, UserEntity u, TeacherEntity te, UserEntity u1 " +
                "WHERE s.timeSlotId = t.id " +
                "AND t.teacherId = te.id " +
                "AND te.username = u1.username " +
                "AND s.username = u.username " +
                "AND t.starTime <= :from AND t.endTime >= :to " +
                "AND (u.username = :username OR u1.username = :username)")
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("username", username)
                .getResultList()
                ;

    }
	public static List<SessionEntity> getListIncomingRequest(String username){
        return JPA.em().createQuery("SELECT s FROM SessionEntity s, TimeSlotEntity t, TeacherEntity te " +
                "WHERE s.timeSlotId = t.id AND t.teacherId = te.id AND te.username = :username  " +
                "AND s.status = 'PENDING' " +
                "AND t.starTime > current_time ")
                .setParameter("username", username)
                .getResultList()
                ;
    }

    public static List<SessionEntity> getListSendingRequest(String username){
	    return JPA.em().createQuery("SELECT s FROM SessionEntity s, TimeSlotEntity t WHERE s.timeSlotId = t.id " +
                "AND s.username = :username AND s.status = 'PENDING' AND t.starTime > current_time ")
                .setParameter("username", username)
                .getResultList()
                ;
    }

    public static boolean checkFinishedLesson(String username, int lessonId) {
        return JPA.em().createQuery("SELECT 1 FROM SessionEntity s " +
                "WHERE s.username = :username AND s.lessonId = :lessonId AND s.status = 'FINISHED'")
                .setParameter("username", username)
                .setParameter("lessonId", lessonId)
                .getResultList()
                .size() > 0;
    }

    public static boolean checkFinishedLesson(String username, int lessonId, EntityManager em) {
        return em.createQuery("SELECT 1 FROM SessionEntity s " +
                "WHERE s.username = :username AND s.lessonId = :lessonId AND s.status = 'FINISHED'")
                .setParameter("username", username)
                .setParameter("lessonId", lessonId)
                .getResultList()
                .size() > 0;
    }

    public static SessionEntity findSessionByTimeSlotId(int timeslotId){
        List<SessionEntity> sessions = JPA.em().createQuery("SELECT s FROM SessionEntity s, TimeSlotEntity t " +
                "WHERE " +
                "s.timeSlotId = t.id  " +
                "AND t.ID = :timeslotId")
                .setParameter("timeslotId", timeslotId)
                .getResultList();
        return sessions.isEmpty() ? null : sessions.get(0);

    }
    public static SessionEntity findSessionByTimeSlotIdWithEM(int timeslotId, EntityManager em){
        List<SessionEntity> sessions = em.createQuery("SELECT s FROM SessionEntity s, TimeSlotEntity t " +
                "WHERE " +
                "s.notifyStatus = 0 " +
                "AND s.timeSlotId = t.id  " +
                "AND t.id = :timeslotId")
                .setParameter("timeslotId", timeslotId)
                .getResultList();
        return sessions.isEmpty() ? null : sessions.get(0);

    }
    public static void updateWithEM(SessionEntity session, EntityManager em){
        em.merge(session);
    }

	public static ConflictSession getConflictSession(SessionEntity sessionEntity){
        return  JPA.em().createQuery("SELECT NEW models.front.ConflictSession(t.starTime,t.endTime,l.title,u.firstName) " +
                        "FROM SessionEntity s, UserEntity u, TeacherEntity te, TimeSlotEntity t, LessonEntity l " +
                "WHERE s.id = :sessionId AND s.timeSlotId = t.id AND t.teacherId = te.id AND te.username = u.username AND s.lessonId = l.id"
                , ConflictSession.class)
                .setParameter("sessionId", sessionEntity.getId())
                .getResultList().get(0);
    }
}


