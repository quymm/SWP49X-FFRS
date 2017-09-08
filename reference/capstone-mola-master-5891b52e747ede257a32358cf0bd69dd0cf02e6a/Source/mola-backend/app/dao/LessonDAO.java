package dao;

import models.ChapterEntity;
import models.CourseEntity;
import models.LessonEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by stark on 06/06/2017.
 */
public class LessonDAO extends BaseDAO<LessonEntity, Integer> {
    public LessonDAO() {
        setClazz(LessonEntity.class);
    }

    @Override
    protected void setClazz(Class<LessonEntity> clazzToSet) {
        super.setClazz(clazzToSet);
    }

    public ChapterEntity getChapterByLessionId(int lessionId) {
        String query = "SELECT chapter FROM ChapterEntity chapter, LessonEntity lesson " +
                "WHERE chapter.id = lesson.chapterId AND lesson.id = :lessionId";
        try {
            return (ChapterEntity) JPA.em().createQuery(query).setParameter("lessionId", lessionId).getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }


    }

    public CourseEntity getCourseByLessionId(int lessionId) {
        String query = "SELECT course FROM CourseEntity course " +
                "JOIN ChapterEntity chapter " +
                "ON course.id = chapter.courseId " +
                "JOIN LessonEntity lession " +
                "ON lession.chapterId = chapter.id " +
                "AND lession.id = :lessionId";
        try {
            return (CourseEntity) JPA.em().createQuery(query)
                    .setParameter("lessionId", lessionId)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static List<LessonEntity> getLessonByChapterId(int chapterId){
        return JPA.em().createQuery("SELECT l FROM LessonEntity l WHERE l.chapterId = :chapterId AND l.active = true")
                .setParameter("chapterId", chapterId)
                .getResultList();
    }

    public static List<LessonEntity> getLessonByChapterId(int chapterId, EntityManager em){
        return em.createQuery("SELECT l FROM LessonEntity l WHERE l.chapterId = :chapterId AND l.active = true")
                .setParameter("chapterId", chapterId)
                .getResultList();
    }

    public static void updateLesson(LessonEntity l){
         JPA.em().persist(l);
    }
    public static void removeListlesson(int chapterId){
        JPA.em().createQuery("delete from LessonEntity l where l.chapterId =:chapterId")
                .setParameter("chapterId", chapterId)
                .executeUpdate();
    }

    public static void updateLessonNumberDelete(LessonEntity lessonToDelete){
        JPA.em().createQuery("update LessonEntity l SET l.number = l.number - 1 WHERE l.chapterId =:chapterId AND l.number >:number")
                .setParameter("chapterId", lessonToDelete.getChapterId())
                .setParameter("number", lessonToDelete.getNumber())
                .executeUpdate();
    }

    public static CourseEntity getCourseByLesson(int lessonId){
        String query = "SELECT course FROM CourseEntity course " +
                "JOIN ChapterEntity chapter " +
                "ON course.id = chapter.courseId " +
                "JOIN LessonEntity lession " +
                "ON lession.chapterId = chapter.id " +
                "AND lession.id = :lessionId";
        try {
            return (CourseEntity) JPA.em().createQuery(query)
                    .setParameter("lessionId", lessonId)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public LessonEntity find(int id){
        return findOne(id);
    }
    public LessonEntity findWithEM(int id, EntityManager em){
        return (LessonEntity) em.createQuery("SELECT l FROM LessonEntity l WHERE l.id = :id")
                .setParameter("id", id)
                .getSingleResult();

    }

}
