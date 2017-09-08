package service;

import dao.*;
import models.*;
import models.front.*;
import play.db.jpa.JPA;
import utils.Const;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by NHAT QUANG on 6/6/2017.
 */
public class CourseService {
    public static List<CourseEntity> getByTeacher(String username) {
        List<CourseEntity> listCourse = CourseDAO.getByTeacher(username);
        return listCourse;
    }

    public static List<ChapterEntity> getChapterByCourse(String username, int id) {

        List<ChapterEntity> listChapter = ChapterDAO.getChapterByCourseId(id);
        for (ChapterEntity chapter : listChapter) {
            List<LessonEntity> lessonEntityList = LessonDAO.getLessonByChapterId(chapter.getId());
            for (LessonEntity lesson: lessonEntityList
                 ) {
                lesson.setFinished(SessionDAO.checkFinishedLesson(username, lesson.getId()));
            }
            chapter.setListLesson(lessonEntityList);
        }

        return listChapter;
    }

    public static ChapterEntity addChapter(BChapterInfo bChapterInfo){
        ChapterEntity chapter = new ChapterEntity();
        chapter.setTitle(bChapterInfo.getTitle());
        chapter.setNumber(bChapterInfo.getNumber());
        chapter.setCourseId(bChapterInfo.getCourseId());
        chapter.setActive(true);
        return ChapterDAO.addChapter(chapter);
    }
    public static void editChapter(BChapterInfo bChapterInfo){
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(bChapterInfo.getId());
        chapter.setTitle(bChapterInfo.getTitle());
        chapter.setNumber(bChapterInfo.getNumber());
        chapter.setCourseId(bChapterInfo.getCourseId());
        chapter.setActive(true);
        ChapterDAO.editChapter(chapter);
    }

    public static CourseEntity editCourse(BCourseInfo bCourseInfo){
        CourseEntity c =JPA.em().find(CourseEntity.class, bCourseInfo.getId());
        c.setTitle(bCourseInfo.getTitle());
        c.setIntroduction(bCourseInfo.getIntroduction());
        c.setLanguage(bCourseInfo.getLanguage());
        c.setPrice(bCourseInfo.getPrice());
        return JPA.em().merge(c);

    }

    public static void deleteChapter(int id){
        ChapterEntity c =JPA.em().find(ChapterEntity.class, id);
        c.setActive(false);
        JPA.em().merge(c);
        ChapterDAO.updateChapterNumberDelete(c);
    }

    public static void deleteCourse(int id){
        CourseEntity c =JPA.em().find(CourseEntity.class, id);
        c.setActive(false);
        JPA.em().merge(c);
    }

    public static CourseEntity createCourse(BCourseInfo bCourseInfo, int teacherId) {
        CourseEntity c = new CourseEntity();
        c.setTitle(bCourseInfo.getTitle());
        c.setIntroduction(bCourseInfo.getIntroduction());
        c.setLanguage(bCourseInfo.getLanguage());
        c.setStructured(bCourseInfo.isStructure());
        c.setTeacherId(teacherId);
        c.setPrice(bCourseInfo.getPrice());
        c.setActive(true);
        c.setCover(getRandomCover());
        c.setTimeCreated(new Timestamp(new Date().getTime()));
        c.setLanguage(bCourseInfo.getLanguage());
        return CourseDAO.createCourse(c);
    }

    private static String getRandomCover(){
        Random random = new Random();
        int randomInt  = random.nextInt(Const.COVER_PICTURE_LIST.length);
        return Const.COVER_PICTURE_LIST[randomInt];
    }

    public static void updateListLesson(BListLesson bListLesson) {
        LessonDAO.removeListlesson(bListLesson.getChapterId());

        for (BLessonInfo bLessonInfo : bListLesson.getListLesson()) {
            LessonEntity lessonEntity = new LessonEntity();
            lessonEntity.setTitle(bLessonInfo.getTitle());
            lessonEntity.setDescription(bLessonInfo.getDescription());
            lessonEntity.setNumber(bLessonInfo.getNumber());
            lessonEntity.setChapterId(bListLesson.getChapterId());
            LessonDAO.updateLesson(lessonEntity);
        }
    }

    public static LessonEntity createLesson(BLessonInfo lesson){
        LessonEntity lessonEntity = new LessonEntity();
        lessonEntity.setActive(true);
        lessonEntity.setChapterId(lesson.getChapterId());
        lessonEntity.setTitle(lesson.getTitle());
        lessonEntity.setDescription(lesson.getDescription());
        lessonEntity.setNumber(lesson.getNumber());
        lessonEntity.setDuration(lesson.getDuration());
        JPA.em().persist(lessonEntity);
        return lessonEntity;
    }

    public static LessonEntity updateLesson(BLessonInfo lesson){
        LessonEntity lessonEntity = JPA.em().find(LessonEntity.class, lesson.getId());
        lessonEntity.setTitle(lesson.getTitle());
        lessonEntity.setDescription(lesson.getDescription());
        lessonEntity.setDuration(lesson.getDuration());
        return JPA.em().merge(lessonEntity);
    }

    public static void deleteLesson(int id){
        LessonEntity lessonEntity = JPA.em().find(LessonEntity.class, id);
        lessonEntity.setActive(false);
        JPA.em().merge(lessonEntity);
        LessonDAO.updateLessonNumberDelete(lessonEntity);
    }

    public static List<RegisterCourseInfo> getCourseRegisted(String username){
        List<RegisterCourseEntity> list = RegisterCourseDAO.getCourseRegisted(username);
        List<RegisterCourseInfo> result = new ArrayList<>();
        for (RegisterCourseEntity registerCourse: list) {
            RegisterCourseInfo registerCourseInfo = new RegisterCourseInfo();
            registerCourseInfo.setRegisterCourse(registerCourse);
            CourseEntity course = JPA.em().find(CourseEntity.class, registerCourse.getCourseId());
            registerCourseInfo.setCourse(course);
            registerCourseInfo.setTeacher(JPA.em().find(TeacherEntity.class, course.getTeacherId()));
            registerCourseInfo.setUser(JPA.em().find(UserEntity.class, registerCourseInfo.getTeacher().getUsername()));
            result.add(registerCourseInfo);
        }
        return result;
    }

    public static String enrollUserToCourse(String username, int courseId) throws IOException {
        String result = RegisterCourseDAO.enrollUserToCourse(username, courseId);
        UserEntity learner = JPA.em().find(UserEntity.class, username);
        CourseEntity course = JPA.em().find(CourseEntity.class, courseId);
        UserEntity teacher = TeacherDAO.getTeacherByCourse(course);
        FCMService.sendNotify(teacher.getUsername(), "Register Course", learner.getFirstName() + " has register course " + course.getTitle());
        return result;
    }

    public static String checkEnrolledUserInCourse(String username, int courseId){
        return RegisterCourseDAO.checkEnrolledUserInCourse(username, courseId);
    }

    public static void cancelRegisterCourseRequest(int courseRegisterId) throws IOException {
        RegisterCourseEntity registerCourseEntity = JPA.em().find(RegisterCourseEntity.class, courseRegisterId);
        UserEntity learner = JPA.em().find(UserEntity.class, registerCourseEntity.getUsername());
        CourseEntity course = JPA.em().find(CourseEntity.class, registerCourseEntity.getCourseId());
        UserEntity teacher = TeacherDAO.getTeacherByCourse(course);
        FCMService.sendNotify(teacher.getUsername(), "Cancel Register Course", learner.getFirstName() + " has canceled register course " + course.getTitle());
        JPA.em().remove(registerCourseEntity);
    }

    public static void rejectRegisterCourseRequest(int courseRegisterId) throws IOException {
        RegisterCourseEntity registerCourseEntity = JPA.em().find(RegisterCourseEntity.class, courseRegisterId);
        UserEntity learner = JPA.em().find(UserEntity.class, registerCourseEntity.getUsername());
        CourseEntity course = JPA.em().find(CourseEntity.class, registerCourseEntity.getCourseId());
        UserEntity teacher = TeacherDAO.getTeacherByCourse(course);
        FCMService.sendNotify(learner.getUsername(), "Reject Register Course", teacher.getFirstName() + " has rejected your register course " + course.getTitle());
        JPA.em().remove(registerCourseEntity);
    }
    public static void acceptRegisterCourseRequest(int courseRegisterId) throws IOException {
        RegisterCourseEntity registerCourseEntity = JPA.em().find(RegisterCourseEntity.class, courseRegisterId);
        registerCourseEntity.setStatus("ACCEPTED");
        UserEntity learner = JPA.em().find(UserEntity.class, registerCourseEntity.getUsername());
        CourseEntity course = JPA.em().find(CourseEntity.class, registerCourseEntity.getCourseId());
        UserEntity teacher = TeacherDAO.getTeacherByCourse(course);
        FCMService.sendNotify(learner.getUsername(), "Accepted Register Course", teacher.getFirstName() + " has accepted your register course " + course.getTitle());
        JPA.em().merge(registerCourseEntity);
    }

}
