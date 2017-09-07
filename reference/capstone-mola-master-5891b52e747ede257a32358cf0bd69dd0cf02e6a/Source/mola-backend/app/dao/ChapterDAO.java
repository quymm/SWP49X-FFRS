package dao;

import models.ChapterEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by NHAT QUANG on 6/6/2017.
 */
public class ChapterDAO extends BaseDAO<ChapterEntity,Long>{
    @Override
    protected void setClazz(Class<ChapterEntity> clazzToSet) {
        super.setClazz(clazzToSet);
    }

    public ChapterDAO() {
        setClazz(ChapterEntity.class);
    }

    public static List<ChapterEntity> getChapterByCourseId(int id){
        return JPA.em().createQuery("select c from ChapterEntity c where c.courseId = :courseid AND c.active = TRUE")
                .setParameter("courseid", id).getResultList();
    }

    public static List<ChapterEntity> getChapterByCourseId(int id, EntityManager em){
        return em.createQuery("select c from ChapterEntity c where c.courseId = :courseid AND c.active = TRUE")
                .setParameter("courseid", id).getResultList();
    }

    public static ChapterEntity addChapter(ChapterEntity chapter){
        JPA.em().persist(chapter);
        return chapter;
    }
    public static void editChapter(ChapterEntity chapter){
        JPA.em().merge(chapter);
    }

    public static void updateChapterNumberDelete(ChapterEntity chapterEntity){
        JPA.em().createQuery("update ChapterEntity c SET c.number = c.number - 1 WHERE c.courseId =:courseId AND c.number >:number")
                .setParameter("courseId", chapterEntity.getCourseId())
                .setParameter("number", chapterEntity.getNumber())
                .executeUpdate();
    }
}
