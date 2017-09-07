package service;

import akka.actor.ActorSystem;
import dao.LessonDAO;
import dao.SessionDAO;
import dao.TeacherDAO;
import dao.TimeSlotDAO;
import models.*;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import utils.DateUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by stark on 18/07/2017.
 */
@Singleton
public class NotificationScheduleService {

    private final TimeSlotDAO timeSlotDAO;
    private final LessonDAO lessonDAO;
    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    private JPAApi jpaApi;

    @Inject
    public NotificationScheduleService(TimeSlotDAO timeSlotDAO, LessonDAO lessonDAO, ActorSystem actorSystem, ExecutionContext executionContext) {
        this.timeSlotDAO = timeSlotDAO;
        this.lessonDAO = lessonDAO;
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        this.initialize();
    }

    private void initialize() {

        this.actorSystem.scheduler().schedule(
                Duration.create(1, TimeUnit.MINUTES), // initialDelay
                Duration.create(1, TimeUnit.MINUTES), // interval
                () -> this.scheduleNotify(),
                this.executionContext
        );
    }

    @Transactional
    private void scheduleNotify() {
        jpaApi.withTransaction(() -> {
            EntityManager em = jpaApi.em();
            System.out.println("[Notify]: Start schedule" + new Date());
            List<TimeSlotEntity> slots = findTimeSlotToNotify(em);

            slots.forEach(slot -> {
                TeacherEntity teacherById = TeacherDAO.getTeacherByIdWithEM(slot.getTeacherId(), em);
                SessionEntity session = SessionDAO.findSessionByTimeSlotIdWithEM(slot.getId(), em);
                if (session != null && session.getNotifyStatus() == false) {
                    int lessonId = session.getLessonId();
                    LessonEntity lesson = lessonDAO.findWithEM(lessonId, em);

                    if (lesson != null) {
                        String learnerUsername = session.getUsername();
                        String teacherUsername = teacherById.getUsername();
                        UserEntity learner = UserService.getUserWithEM(learnerUsername, em);
                        UserEntity teacher = UserService.getUserWithEM(teacherUsername, em);

                        try {
                            System.out.println("[Notify] sending to " + learnerUsername + " at " + new Date());
                            String message = String.format("%s@%s@%s",
                                    learner.getFirstName() + " " + learner.getLastName(),
                                    teacher.getFirstName() + " " + teacher.getLastName(),
                                    lesson.getTitle());
                            int statusCodeSendToLearner = FCMService.sendNotifyWithEM(learnerUsername, "notifyUpcoming", message, em);

                            System.out.println("[Notify] sending to " + teacherUsername + " at " + new Date());

                            int statusCodeSendToTeacher = FCMService.sendNotifyWithEM(teacherUsername, "notifyUpcoming", message, em);

                            if (statusCodeSendToLearner == 200 && statusCodeSendToLearner == statusCodeSendToTeacher) {
                                session.setNotifyStatus(true);
                                SessionDAO.updateWithEM(session, em);
                            }

                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            });
            System.out.println("[Notify]: End schedule" + new Date());
        });
    }

    private List<TimeSlotEntity> findTimeSlotToNotify(EntityManager em) {
        Date now = new Date();
        Date upperTime = DateUtils.minusMinutesToDate(5, now);
        Date lowerTime = DateUtils.addMinutesToDate(5, now);
        List<TimeSlotEntity> slots = timeSlotDAO.findTimeSlotsByDate(upperTime, lowerTime, "booked", em);
        return slots;
    }


}
