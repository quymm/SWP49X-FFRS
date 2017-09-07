package dao;

import models.TimeSlotEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Created by stark on 08/06/2017.
 */
public class TimeSlotDAO extends BaseDAO<TimeSlotEntity, Integer> {
    public TimeSlotDAO() {
        this.setClazz(TimeSlotEntity.class);
    }

    @Override
    protected void setClazz(Class<TimeSlotEntity> clazzToSet) {
        super.setClazz(clazzToSet);
    }

    public List<TimeSlotEntity> findTimeSlotsByDate(Date from, Date to, int teacherId) {

        String query = "SELECT ts from TimeSlotEntity ts WHERE" +
                " (ts.starTime < :to AND ts.endTime > :from) " +
                "AND (ts.status = 'booked' OR ts.status = 'free')" +
                "AND (ts.active = true)" +
                "AND (ts.teacherId = :teacherId)";
        return JPA.em().createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .setParameter("teacherId", teacherId)
                .getResultList();

    }

    public List<TimeSlotEntity> findTimeSlotsByDate(Date from, Date to, String status) {

        String query = "SELECT ts from TimeSlotEntity ts WHERE" +
                " (ts.starTime < :to AND ts.endTime > :from) " +
                "AND (ts.status = :status)";
        return JPA.em().createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .setParameter("status", status)
                .getResultList();

    }
    public List<TimeSlotEntity> findTimeSlotsByDate(Date from, Date to, String status, EntityManager em) {

        String query = "SELECT ts from TimeSlotEntity ts WHERE" +
                " (ts.starTime < :to AND ts.endTime > :from) " +
                "AND (ts.status = :status)";
        return em.createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .setParameter("status", status)
                .getResultList();

    }

    public TimeSlotEntity updateStatus(int id, String status) {
        TimeSlotEntity slotToBeUpdate = JPA.em().find(TimeSlotEntity.class, id);
        slotToBeUpdate.setStatus(status);
        return update(slotToBeUpdate);
    }

    public List<TimeSlotEntity> findTimeSlotsDuplicatedTime(Date from, Date to, int teacherId) {
        String query = "SELECT ts FROM TimeSlotEntity ts WHERE ts.starTime = :from AND ts.endTime = :to";
        return JPA.em().createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .getResultList();
    }

    public static List<TimeSlotEntity> findFreeTimeSlotsByDate(Date from, Date to, int teacherId) {

        String query = "SELECT ts from TimeSlotEntity ts WHERE" +
                " (ts.starTime < :to AND ts.endTime > :from) " +
                "AND ts.status = 'free' " +
                "AND (ts.teacherId = :teacherId) AND ts.active = true";
        return JPA.em().createQuery(query)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }





}
