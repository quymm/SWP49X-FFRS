package service;


import dao.*;
import models.*;
import models.front.BRequest;
import models.front.BSession;
import models.front.ConflictSession;
import models.front.TimeSlotByDate;
import models.front.UserInfo;
import models.front.teacher.schedule.SessionInfo;
import org.bouncycastle.jcajce.provider.symmetric.TEA;
import play.db.jpa.JPA;
import utils.Const;
import utils.DateUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SessionService {
    private final ScheduleDAO scheduleDAO;
    private final LessonDAO lessonDAO;
    private final TeacherService teacherService;
    private final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Inject
    public SessionService(ScheduleDAO scheduleDAO, LessonDAO lessonDAO, TeacherService teacherService) {
        this.scheduleDAO = scheduleDAO;
        this.lessonDAO = lessonDAO;
        this.teacherService = teacherService;
    }

    public static List<BSession> getUpcomingSession(String username) {
        List<SessionEntity> listSession = SessionDAO.getUpcommingSession(username);
        return listSession.stream().map(sessionEntity -> {
            BSession session = new BSession();
            session.setSessionEntity(sessionEntity);
            session.setTimeSlotEntity(JPA.em().find(TimeSlotEntity.class, sessionEntity.getTimeSlotId()));
            session.setTeacherEntity(JPA.em().find(TeacherEntity.class, session.getTimeSlotEntity().getTeacherId()));
            session.setUserEntity(JPA.em().find(UserEntity.class, session.getTeacherEntity().getUsername()));
            session.setCourseEntity(LessonDAO.getCourseByLesson(sessionEntity.getLessonId()));
            session.setTeaching(username.equals(session.getTeacherEntity().getUsername()));
            session.setLearner(JPA.em().find(UserEntity.class, sessionEntity.getUsername()));
            session.setOnTime(new Date().after(session.getTimeSlotEntity().getStarTime())
                    && new Date().before(session.getTimeSlotEntity().getEndTime()));
            return session;
        })
                .collect(Collectors.toList());
    }

    public static List<TimeSlotByDate> getFreeTimeSlotByMonth(int teacherID, String selectDateStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(Const.DATE_PATTERN);
        HashMap<String, List<TimeSlotEntity>> listTimeSlotByDate = new HashMap<>();
        Date selectDate = sdf.parse(selectDateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);
        //set minimum date of month
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Calendar currentDate = new GregorianCalendar();
        Date startDate = currentDate.after(calendar) ? currentDate.getTime() : calendar.getTime();
        //set maximum date of month
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        List<TimeSlotEntity> listTimeSlot = TimeSlotDAO.findFreeTimeSlotsByDate(startDate, endDate, teacherID);
        for (TimeSlotEntity timeSlot : listTimeSlot) {
            Date date = new Date();
            date.setTime(timeSlot.getStarTime().getTime());
            String dateStr = sdf.format(date);
            if (listTimeSlotByDate.containsKey(dateStr)) {
                listTimeSlotByDate.get(dateStr).add(timeSlot);
            } else {
                List<TimeSlotEntity> timeSlotEntityList = new ArrayList<>();
                timeSlotEntityList.add(timeSlot);
                listTimeSlotByDate.put(dateStr, timeSlotEntityList);
            }

        }

        List<TimeSlotByDate> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        int startDay = c.get(Calendar.DAY_OF_MONTH);
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int co = startDay; co <= maxDay; co++, c.add(Calendar.DATE, 1)) {
            String date = sdf.format(c.getTime());
            TimeSlotByDate timeSlotByDate = new TimeSlotByDate();
            timeSlotByDate.setDate(date);
            if (listTimeSlotByDate.containsKey(date)) {
                timeSlotByDate.setListTimeSlot(listTimeSlotByDate.get(date));
            }
            result.add(timeSlotByDate);
        }


        return result;
    }

    public static SessionEntity checkSession(Date from, Date to, String username) {
        List<SessionEntity> sessionEntityList = SessionDAO.getListSession(from, to, username);
        if (sessionEntityList != null && !sessionEntityList.isEmpty()) {
            return sessionEntityList.get(0);
        }

        return null;
    }

    public static Object validateRequestSession(int originTimeslotId, int lessonId, String startTimeMili, String learnerUsername){
        Date startTime = new Date(Long.valueOf(startTimeMili));
        TimeSlotEntity originTimeSlot = JPA.em().find(TimeSlotEntity.class, originTimeslotId);
        LessonEntity lesson = JPA.em().find(LessonEntity.class, lessonId);
        if (originTimeSlot == null) {
            return "Invalid timeslot";
        }
        if (lesson == null) {
            return "Invalid lesson";
        }

        Calendar startSession = Calendar.getInstance();
        startSession.setTime(startTime);

        //set end time of session = start time + duration of lesson + buffer 15min
        Calendar endSession = Calendar.getInstance();
        endSession.setTime(startTime);
        endSession.add(Calendar.MINUTE, lesson.getDuration());
        endSession.add(Calendar.MINUTE, Const.MINS_BUFFERED_EACH_TIMESLOT);

        Calendar startFreeTime = Calendar.getInstance();
        startFreeTime.setTime(originTimeSlot.getStarTime());

        Calendar endFreeTime = Calendar.getInstance();
        endFreeTime.setTime(originTimeSlot.getEndTime());

        if (startSession.before(startFreeTime) || endSession.after(endFreeTime)) {
            return "Invalid time";
        }
        SessionEntity conflictSessionEntity = checkSession(startSession.getTime(), endSession.getTime(), learnerUsername);
        if (conflictSessionEntity != null) {
            ConflictSession conflictSession = SessionDAO.getConflictSession(conflictSessionEntity);
            return conflictSession;
        }
        return null;
    }


    public static Object requestSession(int originTimeslotId, int lessonId, String startTimeMili, String learnerUsername) throws ParseException {
        Date startTime = new Date(Long.valueOf(startTimeMili));
        TimeSlotEntity originTimeSlot = JPA.em().find(TimeSlotEntity.class, originTimeslotId);
        LessonEntity lesson = JPA.em().find(LessonEntity.class, lessonId);
        if (originTimeSlot == null) {
            return "Invalid timeslot";
        }
        if (lesson == null) {
            return "Invalid lesson";
        }

        Calendar startSession = Calendar.getInstance();
        startSession.setTime(startTime);

        //set end time of session = start time + duration of lesson + buffer 15min
        Calendar endSession = Calendar.getInstance();
        endSession.setTime(startTime);
        endSession.add(Calendar.MINUTE, lesson.getDuration());
        endSession.add(Calendar.MINUTE, Const.MINS_BUFFERED_EACH_TIMESLOT);

        Calendar startFreeTime = Calendar.getInstance();
        startFreeTime.setTime(originTimeSlot.getStarTime());

        Calendar endFreeTime = Calendar.getInstance();
        endFreeTime.setTime(originTimeSlot.getEndTime());

        if (startSession.before(startFreeTime) || endSession.after(endFreeTime)) {
            return "Invalid time";
        }
        SessionEntity conflictSessionEntity = checkSession(startSession.getTime(), endSession.getTime(), learnerUsername);
        if (conflictSessionEntity != null) {
            ConflictSession conflictSession = SessionDAO.getConflictSession(conflictSessionEntity);
            return conflictSession;
        }

        // If time from start time of origin slot to start time of new slot > 30p, create new time slot
        long diffStart = startSession.getTimeInMillis() - startFreeTime.getTimeInMillis();
        if (diffStart > TimeUnit.MILLISECONDS.convert(Const.MIN_TIME_SESSION_MINUTE, TimeUnit.MINUTES)) {
            TimeSlotEntity startTimeSlot = new TimeSlotEntity();
            startTimeSlot.setTeacherId(originTimeSlot.getTeacherId());
            startTimeSlot.setStatus("free");
            startTimeSlot.setStarTime(new Timestamp(startFreeTime.getTimeInMillis()));
            startTimeSlot.setEndTime(new Timestamp(startSession.getTimeInMillis()));
            startTimeSlot.setActive(true);
            JPA.em().persist(startTimeSlot);
        }

        // If time from end time of new slot to end time of origin slot > 30p, create new time slot
        long diffEnd = endFreeTime.getTimeInMillis() - endSession.getTimeInMillis();
        if (diffEnd > TimeUnit.MILLISECONDS.convert(Const.MIN_TIME_SESSION_MINUTE, TimeUnit.MINUTES)) {
            TimeSlotEntity endTimeSlot = new TimeSlotEntity();
            endTimeSlot.setStatus("free");
            endTimeSlot.setTeacherId(originTimeSlot.getTeacherId());
            endTimeSlot.setStarTime(new Timestamp(endSession.getTimeInMillis()));
            endTimeSlot.setEndTime(new Timestamp(endFreeTime.getTimeInMillis()));
            endTimeSlot.setActive(true);
            JPA.em().persist(endTimeSlot);

        }

        //disable origin timeslot
        originTimeSlot.setActive(false);
        JPA.em().merge(originTimeSlot);


        //Add new time slot
        TimeSlotEntity newTimeSlot = new TimeSlotEntity();
        newTimeSlot.setStarTime(new Timestamp(startSession.getTimeInMillis()));
        newTimeSlot.setEndTime(new Timestamp(endSession.getTimeInMillis()));
        newTimeSlot.setStatus("booked");
        newTimeSlot.setTeacherId(originTimeSlot.getTeacherId());
        newTimeSlot.setActive(true);
        JPA.em().persist(newTimeSlot);

        //Add new session
        SessionEntity newSession = new SessionEntity();
        newSession.setLessonId(lessonId);
        newSession.setStatus("PENDING");
        newSession.setTimeRequest(new Timestamp(new Date().getTime()));
        newSession.setUsername(learnerUsername);
        newSession.setTimeSlotId(newTimeSlot.getId());
        JPA.em().persist(newSession);

        UserEntity teacher = TeacherDAO.getTeacheProfile(originTimeSlot.getTeacherId());

        String room = teacher.getUsername() + "@" + learnerUsername + "@" + newSession.getId();
        newSession.setSessionRoom(room);

        JPA.em().merge(newSession);

        return null;
    }

    public static Map<String, Object> getListIncomingRequest(String username) {
        Map<String, Object> result = new HashMap<>();
        List<BRequest> incomingRequests = new ArrayList<>();
        List<SessionEntity> listSession = SessionDAO.getListIncomingRequest(username);
        for (SessionEntity sessionEntity : listSession) {
            BRequest request = new BRequest();
            request.setSessionReq(true);
            request.setTime(sessionEntity.getTimeRequest());
            request.setSession(sessionEntity);
            request.setLesson(JPA.em().find(LessonEntity.class, sessionEntity.getLessonId()));
            request.setChapter(JPA.em().find(ChapterEntity.class, request.getLesson().getChapterId()));
            request.setCourse(JPA.em().find(CourseEntity.class, request.getChapter().getCourseId()));
            request.setTimeSlot(JPA.em().find(TimeSlotEntity.class, sessionEntity.getTimeSlotId()));
            request.setLearner(JPA.em().find(UserEntity.class, sessionEntity.getUsername()));
            incomingRequests.add(request);
        }
        List<RegisterCourseEntity> listCourseRequest = RegisterCourseDAO.getIncomingCourseRequest(username);
        for (RegisterCourseEntity registerCourse : listCourseRequest) {
            BRequest request = new BRequest();
            request.setSessionReq(false);
            request.setTime(registerCourse.getTimeRequest());
            request.setRegisterCourse(registerCourse);
            request.setCourse(JPA.em().find(CourseEntity.class, registerCourse.getCourseId()));
            request.setLearner(JPA.em().find(UserEntity.class, registerCourse.getUsername()));
            incomingRequests.add(request);
        }

        Collections.sort(incomingRequests, (o1, o2) -> o2.getTime().compareTo(o1.getTime()));

        result.put("listIncomingRequest", incomingRequests);
        return result;
    }

    public static Map<String, Object> getListSendingRequest(String username) {
        Map<String, Object> result = new HashMap<>();
        List<BRequest> sendingRequests = new ArrayList<>();
        List<SessionEntity> listSession = SessionDAO.getListSendingRequest(username);
        for (SessionEntity sessionEntity : listSession) {
            BRequest request = new BRequest();
            request.setSessionReq(true);
            request.setTime(sessionEntity.getTimeRequest());
            request.setSession(sessionEntity);
            request.setLesson(JPA.em().find(LessonEntity.class, sessionEntity.getLessonId()));
            request.setChapter(JPA.em().find(ChapterEntity.class, request.getLesson().getChapterId()));
            request.setCourse(JPA.em().find(CourseEntity.class, request.getChapter().getCourseId()));
            request.setTimeSlot(JPA.em().find(TimeSlotEntity.class, sessionEntity.getTimeSlotId()));
            request.setTeacher(JPA.em().find(TeacherEntity.class, request.getTimeSlot().getTeacherId()));
            request.setTeacherInfo(JPA.em().find(UserEntity.class, request.getTeacher().getUsername()));
            sendingRequests.add(request);

        }

        List<RegisterCourseEntity> listSendingCourseRequest = RegisterCourseDAO.getSendingCourseRequest(username);
        for (RegisterCourseEntity registerCourse : listSendingCourseRequest) {
            BRequest request = new BRequest();
            request.setSessionReq(false);
            request.setTime(registerCourse.getTimeRequest());
            request.setRegisterCourse(registerCourse);
            request.setCourse(JPA.em().find(CourseEntity.class, registerCourse.getCourseId()));
            request.setTeacher(JPA.em().find(TeacherEntity.class, request.getCourse().getTeacherId()));
            request.setTeacherInfo(JPA.em().find(UserEntity.class, request.getTeacher().getUsername()));
            sendingRequests.add(request);
        }
        Collections.sort(sendingRequests, (o1, o2) -> o2.getTime().compareTo(o1.getTime()));

        result.put("listSendingRequest", sendingRequests);
        return result;
    }

    public void cancelSessionRequest(int sessionId) throws IOException, ParseException {
        SessionEntity sessionEntity = JPA.em().find(SessionEntity.class, sessionId);
        UserEntity learner = JPA.em().find(UserEntity.class, sessionEntity.getUsername());
        LessonEntity lesson = JPA.em().find(LessonEntity.class, sessionEntity.getLessonId());
        TimeSlotEntity timeSlot = JPA.em().find(TimeSlotEntity.class, sessionEntity.getTimeSlotId());
        UserEntity teacher = TeacherDAO.getTeacheProfile(timeSlot.getTeacherId());
        FCMService.sendNotify(teacher.getUsername(), "Cancel Session Request", learner.getFirstName() + " has canceled session request lesson " + lesson.getTitle());
        Map<String, Object> result = mergeTimeslot(timeSlot);
        JPA.em().remove(sessionEntity);
    }

    private Map<String, Object> mergeTimeslot(TimeSlotEntity timeSlot) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_PATTERN);
        //set time slot status back to free
        timeSlot.setStatus("free");
        timeSlot.setActive(false);
        JPA.em().merge(timeSlot);
        //merge time slot back to schedule
        Map<String, Object> result = teacherService.setFreeTimeSlot(sdf.format(timeSlot.getStarTime()), sdf.format(timeSlot.getEndTime()), timeSlot.getTeacherId());
        return result;
    }

    public void rejectSessionRequest(int sessionId) throws IOException, ParseException {
        SessionEntity sessionEntity = JPA.em().find(SessionEntity.class, sessionId);
        UserEntity learner = JPA.em().find(UserEntity.class, sessionEntity.getUsername());
        LessonEntity lesson = JPA.em().find(LessonEntity.class, sessionEntity.getLessonId());
        TimeSlotEntity timeSlot = JPA.em().find(TimeSlotEntity.class, sessionEntity.getTimeSlotId());
        UserEntity teacher = TeacherDAO.getTeacheProfile(timeSlot.getTeacherId());
        FCMService.sendNotify(learner.getUsername(), "Reject Session Request", teacher.getFirstName() + " has rejected your request lesson " + lesson.getTitle());
        mergeTimeslot(timeSlot);
        JPA.em().remove(sessionEntity);
    }

    public static void acceptSessionRequest(int sessionId) throws IOException {
        SessionEntity sessionEntity = JPA.em().find(SessionEntity.class, sessionId);
        sessionEntity.setStatus("ACCEPTED");
        UserEntity learner = JPA.em().find(UserEntity.class, sessionEntity.getUsername());
        LessonEntity lesson = JPA.em().find(LessonEntity.class, sessionEntity.getLessonId());
        TimeSlotEntity timeSlot = JPA.em().find(TimeSlotEntity.class, sessionEntity.getTimeSlotId());
        UserEntity teacher = TeacherDAO.getTeacheProfile(timeSlot.getTeacherId());
        FCMService.sendNotify(learner.getUsername(), "Accept Session Request", teacher.getFirstName() + " has accepted your request lesson " + lesson.getTitle());
        JPA.em().merge(sessionEntity);
    }

    public List<SessionInfo> getScheduleLearnerNotFinished(String from, String to, String learnerUsername) throws ParseException {
        Date dFrom = DateUtils.stringToDate(from, TIME_PATTERN);
        Date dTo = DateUtils.stringToDate(to, TIME_PATTERN);
        final String statusFinished = "FINISHED";

        List<SessionEntity> sessions = scheduleDAO.getScheduleLearnerNotFinished(dFrom, dTo, learnerUsername, statusFinished);

        List<SessionInfo> sessionInfos = new ArrayList<>();
        sessions.forEach(session -> {
            int lessonId = session.getLessonId();
            CourseEntity course = lessonDAO.getCourseByLessionId(lessonId);
            ChapterEntity chapter = lessonDAO.getChapterByLessionId(lessonId);

            LessonEntity lesson = lessonDAO.findOne(lessonId);

            UserEntity teacher = TeacherDAO.getTeacherByCourse(course);


            TimeSlotEntity timeSlot = scheduleDAO.getTimeSlotBySession(session);
            if (teacher != null && course != null && chapter != null && lesson != null && timeSlot != null) {
                SessionInfo schedule = new SessionInfo();
                UserInfo teacherInfo = new UserInfo();
                teacherInfo.avatar = teacher.getAvatar();
                teacherInfo.firstName = teacher.getFirstName();
                teacherInfo.lastName = teacher.getLastName();
                teacherInfo.email = teacher.getEmail();
                teacherInfo.username = teacher.getUsername();

                schedule.setChapter(chapter);
                schedule.setCourse(course);
                schedule.setLesson(lesson);
                schedule.setTeacher(teacherInfo);
                schedule.setSession(session);
                schedule.setTimeSlot(timeSlot);
                schedule.setFree(timeSlot.getStatus().equals("free"));

                sessionInfos.add(schedule);
            }
        });
        return sessionInfos;

    }

    public static void finishedSession(int sessionId){

        SessionEntity session = SessionDAO.findOne(sessionId);
        if (session!=null){
            session.setStatus("FINISHED");
        }
        SessionDAO.update(session);
    }
}
