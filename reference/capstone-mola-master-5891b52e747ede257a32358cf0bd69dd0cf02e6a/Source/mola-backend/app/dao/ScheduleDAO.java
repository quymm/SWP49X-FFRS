package dao;

import models.SessionEntity;
import models.TimeSlotEntity;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Created by stark on 06/06/2017.
 */
public class ScheduleDAO extends BaseDAO<SessionEntity, Long> {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public ScheduleDAO() {
        this.setClazz(SessionEntity.class);
    }


    @Override
    protected void setClazz(Class<SessionEntity> clazzToSet) {
        super.setClazz(clazzToSet);
    }

    public SessionEntity[] getTeacherSessions(Date from, Date to, int teacherId, String status) {
        String query =
                "SELECT ss FROM SessionEntity ss WHERE ss.timeSlotId IN " +
                        "       (" +
                        "         SELECT ts.id" +
                        "         FROM TimeSlotEntity ts" +
                        "         WHERE" +
                        "           ts.status = :status" +
                        "           AND" +
                        "           ts.teacherId = :teacherId" +
                        "           AND" +
                        "           (ts.starTime >= :from AND ts.endTime <= :to)" +
                        "       ) " +
                        "AND ss.status <> 'FINISHED'";

        List<SessionEntity> sessions = JPA.em()
                .createQuery("SELECT s FROM SessionEntity s, TimeSlotEntity t " +
                        "WHERE s.timeSlotId = t.id AND t.status = :status AND t.teacherId = :teacherId " +
                        "AND (t.starTime >= :from AND t.endTime <= :to) AND s.status <> 'FINISHED'")
                .setParameter("teacherId", teacherId)
                .setParameter("status", status)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to)
                .getResultList();

        return sessions.toArray(new SessionEntity[sessions.size()]);
    }

    public TimeSlotEntity[] getTeacherFreeTimeSlot(Date from, Date to, int teacherId, String status) {
        String query =
                "         SELECT ts" +
                        "         FROM TimeSlotEntity ts" +
                        "         WHERE" +
                        "           ts.status = :status" +
                        "           AND" +
                        "           ts.teacherId = :teacherId" +
                        "           AND" +
                        "           (ts.starTime >= :from AND ts.endTime < :to) " +
                        "AND ts.active = TRUE ";
        List<TimeSlotEntity> timeSlotEntities = JPA.em().createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .setParameter("teacherId", teacherId)
                .setParameter("status", status)
                .getResultList();
        return timeSlotEntities.toArray(new TimeSlotEntity[timeSlotEntities.size()]);
    }



    public TimeSlotEntity getTimeSlotBySession(SessionEntity session) {
        int timeSlot = session.getTimeSlotId();
        String query = "SELECT ts FROM TimeSlotEntity ts, SessionEntity ss WHERE ss.timeSlotId = ts.id AND ts.id = :timeSlotId";
        try {
            return (TimeSlotEntity) JPA.em().createQuery(query)
                    .setParameter("timeSlotId", timeSlot)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SessionEntity> getScheduleLearnerNotFinished(Date from, Date to, String username, String status){
        String query = "SELECT s FROM SessionEntity s, TimeSlotEntity ts "
                + "WHERE (ts.starTime <= :to AND ts.endTime >= :from) "
                + "AND s.timeSlotId = ts.id "
                + "AND s.username = :username "
                + "AND (s.status = 'PENDING' OR s.status = 'ACCEPTED')";
        List<SessionEntity> sessions = JPA.em().createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .setParameter("username", username)
//                .setParameter("status", status)
                .getResultList();
        return sessions;
    }

}
