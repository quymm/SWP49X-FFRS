package service;

import dao.*;
import models.*;
import models.front.ExtraTeacherInfo;
import models.front.MonthlyRevenue;
import models.front.UserInfo;
import models.front.teacher.schedule.SessionInfo;
import models.front.teacher.schedule.TeacherScheduleInfo;
import org.joda.time.LocalDateTime;
import play.db.jpa.JPA;
import utils.Const;
import utils.DateUtils;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by stark on 03/06/2017.
 */
public class TeacherService {
    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;
    private final ChapterDAO chapterDAO;
    private final LessonDAO lessonDAO;
    private final ScheduleDAO scheduleDAO;
    private final TimeSlotDAO timeSlotDAO;
    private final UserRoleDAO userRoleDAO;
    private final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Inject
    public TeacherService(TeacherDAO teacherDAO, CourseDAO courseDAO, ChapterDAO chapterDAO, LessonDAO lessonDAO, ScheduleDAO scheduleDAO, TimeSlotDAO timeSlotDAO, UserRoleDAO userRoleDAO) {
        this.teacherDAO = teacherDAO;
        this.courseDAO = courseDAO;
        this.chapterDAO = chapterDAO;
        this.lessonDAO = lessonDAO;
        this.scheduleDAO = scheduleDAO;
        this.timeSlotDAO = timeSlotDAO;
        this.userRoleDAO = userRoleDAO;
    }

    public TeacherEntity findTeacherByUsername(String ursername) {
        return teacherDAO.findTeacherByUsername(ursername);
    }

    public TeacherEntity registerTeacher(String username, int expYears, String expAbout) {
        TeacherEntity teacherEntity = teacherDAO.findTeacherByUsername(username);
        if (teacherEntity != null){
            teacherEntity.setExpYears(expYears);
            teacherEntity.setExpDescription(expAbout);

            return teacherDAO.save(teacherEntity);
        }
        TeacherEntity t = new TeacherEntity(username, expYears, expAbout);
        t.setStatus("PENDING");
        t.setTimeRegistered(new Timestamp(new Date().getTime()));
//        int id = teacherDAO.findAll().size();
//        t.setId(id + 1);
        JPA.em().persist(t);
//        UserRoleEntity uRole = new UserRoleEntity();
//        uRole.setRoleName("teacher");
//        uRole.setUsername(teacher.getUsername());
//        userRoleDAO.saveUserRole(uRole);
        return t;
    }


    public TeacherScheduleInfo getScheduleToday(String from, String to, int teacherId) throws ParseException {

        SessionEntity[] sessions = getTeacherSessions(from, to, teacherId);
        TimeSlotEntity[] timeSlotsFree = getTeacherFreeTimeSlot(from, to, teacherId);

        SessionInfo[] sessionInfos = new SessionInfo[sessions.length];
        for (int i = 0; i < sessions.length; i++) {
            SessionEntity session = sessions[i];
            int lessonId = session.getLessonId();
            String username = session.getUsername();
            UserEntity user = UserDAO.getUser(username);

            CourseEntity course = lessonDAO.getCourseByLessionId(lessonId);
            ChapterEntity chapter = lessonDAO.getChapterByLessionId(lessonId);

            LessonEntity lesson = lessonDAO.findOne(lessonId);
            TimeSlotEntity timeSlot = scheduleDAO.getTimeSlotBySession(session);
            if (user != null && course != null && chapter != null && lesson != null && timeSlot != null) {
                SessionInfo schedule = new SessionInfo();
                UserInfo userInfo = new UserInfo();
                userInfo.avatar = user.getAvatar();
                userInfo.firstName = user.getFirstName();
                userInfo.lastName = user.getLastName();
                userInfo.email = user.getEmail();

                schedule.setChapter(chapter);
                schedule.setCourse(course);
                schedule.setLesson(lesson);
                schedule.setLearner(userInfo);
                schedule.setSession(session);
                schedule.setTimeSlot(timeSlot);
                schedule.setFree(timeSlot.getStatus().equals("free"));
                sessionInfos[i] = schedule;
            }
        }
//        return sessionInfos;
        return new TeacherScheduleInfo(sessionInfos, timeSlotsFree);
    }

    public Map<String, Object> setFreeTimeSlots(String from, String to, int teacherId) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);

        Date dFrom = formatter.parse(from);
        Date dTo = formatter.parse(to);
        int hour = dTo.getHours();
        int min = dTo.getMinutes();
        Date dEnd = new Date(dFrom.getTime());
        dEnd.setHours(hour);
        dEnd.setMinutes(min);
        LocalDateTime dateFrom = new LocalDateTime(dFrom);
        LocalDateTime dateTo = new LocalDateTime(dTo);
        LocalDateTime dateEnd = new LocalDateTime(dEnd);
        Map<String, Object> result = new HashMap<>();
        while (dateFrom.isBefore(dateTo)) {
            System.out.println(dateFrom);
            Map<String, Object> data = setFreeTimeSlot(dateFrom.toString(TIME_PATTERN), dateEnd.toString(TIME_PATTERN), teacherId);

            result.putAll(data);
            dateFrom = dateFrom.plusDays(1);
            dateEnd = dateEnd.plusDays(1);
        }

        return result;
    }

    public Map<String, Object> setFreeTimeSlot(String from, String to, int teacherId) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);

        Date dateFrom = formatter.parse(from);
        Date dateTo = formatter.parse(to);
        List<TimeSlotEntity> list = findTimeSlotsByDate(dateFrom, dateTo, teacherId);

        Map<String, Object> result = new HashMap<>();

        if (list.isEmpty()) {
            TimeSlotEntity slotEntity = new TimeSlotEntity(new Timestamp(dateFrom.getTime()), new Timestamp(dateTo.getTime()), teacherId);
            timeSlotDAO.save(slotEntity);
        } else {
            List<TimeSlotEntity> slotsBooked = list.stream()
                    .filter(slot -> slot.getStatus().equals("booked"))
                    .collect(Collectors.toList());

            List<TimeSlotEntity> slotsFree = list.stream()
                    .filter(slot -> slot.getStatus().equals("free"))
                    .collect(Collectors.toList());
            TimeSlotValidator toTestRules = TimeSlotValidator.violatedRules(dateFrom, dateTo, slotsBooked);
            TimeSlotValidator toTestContraints = TimeSlotValidator.violatedTimeContraint(dateFrom, dateTo, slotsBooked);

            if (toTestRules != null) {
                result.put("violated", toTestRules);
                return result;
            }
            if (toTestContraints != null) {
                result.put("violated", toTestContraints);
                return result;
            }

            if (slotsFree.isEmpty()) {
                TimeSlotEntity slotEntity = new TimeSlotEntity(new Timestamp(dateFrom.getTime()), new Timestamp(dateTo.getTime()), teacherId);
                slotEntity.setActive(true);
                timeSlotDAO.save(slotEntity);
                result.put("data", "success");
            } else {
                TimeSlotEntity slotWithin = slotsFree.stream().filter(slot -> dateFrom.getTime() >= slot.getStarTime().getTime() && dateTo.getTime() <= slot.getEndTime().getTime())
                        .findFirst().orElse(null);

                if (slotWithin != null) {
                    result.put("data", "success");
                    return result;
                }

                for (TimeSlotEntity slot : slotsFree) {
                    if (dateFrom.getTime() >= slot.getStarTime().getTime() && dateFrom.getTime() <= slot.getEndTime().getTime()
                            ||
                            dateTo.getTime() >= slot.getStarTime().getTime() && dateTo.getTime() <= slot.getEndTime().getTime()
                            ||
                            dateFrom.getTime() <= slot.getStarTime().getTime() && dateTo.getTime() >= slot.getEndTime().getTime()) {

                        if (slot.getStarTime().getTime() > dateFrom.getTime()) {
                            slot.setStarTime(new Timestamp(dateFrom.getTime()));
                            slot.setActive(true);
                            timeSlotDAO.update(slot);

                        }
                        if (slot.getEndTime().getTime() < dateTo.getTime()) {
                            slot.setEndTime(new Timestamp(dateTo.getTime()));
                            slot.setActive(true);
                            timeSlotDAO.update(slot);

                        }
                    }
                }

                //filter to find slots need to merged
                List<TimeSlotEntity> slotsNeedToMerged = slotsFree.stream()
                        .filter(slot ->
                                dateFrom.getTime() >= slot.getStarTime().getTime() && dateFrom.getTime() <= slot.getEndTime().getTime()
                                ||
                                dateTo.getTime() >= slot.getStarTime().getTime() && dateTo.getTime() <= slot.getEndTime().getTime()
                                ||
                                dateFrom.getTime() <= slot.getStarTime().getTime() && dateTo.getTime() >= slot.getEndTime().getTime()
                        ).collect(Collectors.toList());
                //sort by start time
                slotsNeedToMerged.sort((slot1, slot2) -> {
                    return slot1.getStarTime().compareTo(slot2.getStarTime());
                });

                if (slotsNeedToMerged.size() > 0) {
                    TimeSlotEntity first = slotsNeedToMerged.get(0);
                    TimeSlotEntity end = slotsNeedToMerged.get(slotsNeedToMerged.size() - 1);

                    TimeSlotEntity slotToBeMerge = new TimeSlotEntity(first.getStarTime(), end.getEndTime(), teacherId);
                    slotToBeMerge.setActive(true);
                    timeSlotDAO.save(slotToBeMerge);
                    slotsNeedToMerged.forEach(slot -> {
                        slot.setStatus("merged");
                        slot.setActive(true);
                        timeSlotDAO.update(slot);
                    });
                } else {
                    TimeSlotEntity slotToBeMerge = new TimeSlotEntity(dateFrom, dateTo, teacherId);
                    slotToBeMerge.setActive(true);
                    timeSlotDAO.save(slotToBeMerge);
                }
            }
        }


        if (!result.containsKey("violated")) {
            result.put("data", "success");
        }

        return result;
    }

    private List<TimeSlotEntity> findTimeSlotsByDate(Date dateFrom, Date dateTo, int teacherId) {
        Date dFrom = new Date(dateFrom.getTime());
        dFrom.setHours(0);
        dFrom.setMinutes(0);
        dFrom.setSeconds(0);
        //add one day
        Date dTo = DateUtils.addMinutesToDate(1440, dFrom);
        return timeSlotDAO.findTimeSlotsByDate(dFrom, dTo, teacherId);
    }

    private TimeSlotEntity setTeacherFreeTimeSlot(String from, String to, int teacherId) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);

        Timestamp dataFrom = new Timestamp(formatter.parse(from).getTime());
        Timestamp dataTo = new Timestamp(formatter.parse(to).getTime());

        TimeSlotEntity slot = new TimeSlotEntity(dataFrom, dataTo, teacherId);

        return timeSlotDAO.save(slot);
    }

    private SessionEntity[] getTeacherSessions(String from, String to, int teacherId) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);

        Date dataFrom = formatter.parse(from);
        Date dataTo = formatter.parse(to);
        SessionEntity[] ss = scheduleDAO.getTeacherSessions(dataFrom, dataTo, teacherId, "booked");

        return ss;
    }

    private TimeSlotEntity[] getTeacherFreeTimeSlot(String from, String to, int teacherId) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);

        Date dataFrom = formatter.parse(from);
        Date dataTo = formatter.parse(to);
        return scheduleDAO.getTeacherFreeTimeSlot(dataFrom, dataTo, teacherId, "free");
    }



    private static class TimeSlotValidator {
        private TimeSlotEntity timeSlot;
        private String slotType;
        private String violatedName;

        public TimeSlotValidator(TimeSlotEntity timeSlot) {
            this.timeSlot = timeSlot;
            if (timeSlot != null) {
                this.slotType = timeSlot.getStatus().equals("free") ? "timeslot" : "session";
            }
        }

        public TimeSlotValidator(TimeSlotEntity timeSlot, String violatedName) {
            this.timeSlot = timeSlot;
            if (timeSlot != null) {
                this.slotType = timeSlot.getStatus().equals("free") ? "timeslot" : "session";
            }
            this.violatedName = violatedName;
        }

        /*
        * Receive date from and end of timeslot and check in list timeslots
        *
        * Return null if NOT violate any, else return slot violated
        * */
        private static TimeSlotValidator violatedTimeContraint(Date dateFrom, Date dateTo, List<TimeSlotEntity> bookedSlots) {
            if (bookedSlots == null) {
                return null;
            }

            for (TimeSlotEntity slot : bookedSlots) {
                // violate time contraints
                int min = dateFrom.getMinutes();
                Date dateBufferStart = DateUtils.minusMinutesToDate(Const.MINS_BUFFERED_EACH_TIMESLOT, dateFrom);
                Date dateBufferEnd = DateUtils.addMinutesToDate(Const.MINS_BUFFERED_EACH_TIMESLOT, dateTo);
                if (Math.abs(slot.getEndTime().getTime() - dateFrom.getTime()) < Const.MINS_BUFFERED_EACH_TIMESLOT * 60 * 1000) {
                    return new TimeSlotValidator(slot, "violated_time_constraints");
                }
                if (Math.abs(slot.getStarTime().getTime() - dateTo.getTime()) < Const.MINS_BUFFERED_EACH_TIMESLOT * 60 * 1000) {
                    return new TimeSlotValidator(slot, "violated_time_constraints");
                }
            }


            return null;
        }

        /*
        * Receive date from and end of timeslot and check in list timeslots
        *
        * Return null if NOT violate any, else return slot violated
        * */
        private static TimeSlotValidator violatedRules(Date dateFrom, Date dateTo, List<TimeSlotEntity> slots) {
            for (TimeSlotEntity slot : slots) {
                // violate rules
                if (dateFrom.getTime() <= slot.getStarTime().getTime()
                        && dateTo.getTime() >= slot.getEndTime().getTime()) {
                    return new TimeSlotValidator(slot, "violated_rules");
                }
                if (dateFrom.getTime() >= slot.getStarTime().getTime()
                        && dateFrom.getTime() <= slot.getEndTime().getTime()) {
                    return new TimeSlotValidator(slot, "violated_rules");
                }
                if (dateTo.getTime() >= slot.getStarTime().getTime()
                        && dateTo.getTime() <= slot.getEndTime().getTime()) {
                    return new TimeSlotValidator(slot, "violated_rules");
                }
            }

            return null;
        }

        public TimeSlotEntity getTimeSlot() {
            return timeSlot;
        }

        public String getSlotType() {
            return slotType;
        }

        public String getViolatedName() {
            return violatedName;
        }
    }

    public static ExtraTeacherInfo getExtraInfo(String username , int teacherId){
        ExtraTeacherInfo extraTeacherInfo = new ExtraTeacherInfo();
        extraTeacherInfo.setNumStudent(TeacherDAO.getStudentsCount(teacherId));
        extraTeacherInfo.setNumCourse(TeacherDAO.getCourseCount(teacherId));
        List<LanguageTeachEntity> languagesTeach = TeacherDAO.getLanguageTeach(teacherId);
        extraTeacherInfo.setLanguageTeach(languagesTeach);
        extraTeacherInfo.setLanguageSpeak(TeacherDAO.getLanguageSpeak(teacherId)
                .stream()
                .map(languageSpeakEntity -> languageSpeakEntity.getLanguage())
                .collect(Collectors.toList()));
        extraTeacherInfo.setLearned(TeacherDAO.getLearned(username, teacherId));
        TeacherEntity teacher = TeacherDAO.getTeacherById(teacherId);
        extraTeacherInfo.setNumOfRate(teacher.getNumOfRate());
        extraTeacherInfo.setRating(teacher.getRating());
        return extraTeacherInfo;
    }

    public static List<UserEntity> getListTeacherRequest(){
        return  TeacherDAO.getLanguageTeachRequest();

    }

    public static List<LanguageTeachEntity> getListLanguageTeach(int teacherId){
        return TeacherDAO.getLanguageTeachRequestList(teacherId);
    }

    public static List<LanguageTeachEntity> getLanguageTeachClip(int teacherId) {
        return TeacherDAO.getLanguageTeachClip(teacherId);
    }

    public static List<MonthlyRevenue> getMonthlyRevenue(int teacherId){
        List<MonthlyRevenue> listRevenues = TeacherDAO.getMonthlyRevenue(teacherId);
        for (int i = 1; i <= 12; i++) {
            boolean existed = false;
            for (MonthlyRevenue m :
                    listRevenues) {
                if (m.getMonth() == i){
                    existed = true;
                }
            }
            if (!existed){
                listRevenues.add(new MonthlyRevenue(0, i));
            }
        }
        Collections.sort(listRevenues, Comparator.comparingInt(MonthlyRevenue::getMonth));
        return listRevenues;
    }
}