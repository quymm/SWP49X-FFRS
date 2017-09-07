package dao;

import models.LanguageTeachEntity;
import models.TeacherEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by stark on 03/06/2017.
 */
public class LanguageTeachDAO extends BaseDAO<LanguageTeachEntity, Integer> implements Serializable{

    @Override
    protected void setClazz(Class<LanguageTeachEntity> clazzToSet) {
        super.setClazz(clazzToSet);
    }

    public LanguageTeachDAO() {
        setClazz(LanguageTeachEntity.class);
    }

    public static boolean acceptLanguageTeachRequest(int teacherId, String language){
        return JPA.em().createQuery("UPDATE LanguageTeachEntity l SET l.status = 'ACCEPTED' WHERE l.teacherId = :teacherId AND l.language = :language")
                .setParameter("teacherId", teacherId)
                .setParameter("language", language)
                .executeUpdate() > 0;
    }

    public static List<String> getByUsername(int teacherId) {
        return JPA.em().createQuery("SELECT l.language FROM LanguageTeachEntity l WHERE l.teacherId = :teacherId")
                .setParameter("teacherId", teacherId).getResultList();
    }

    public static LanguageTeachEntity find(TeacherEntity teacher, String language){
        try {
            return (LanguageTeachEntity) JPA.em().createQuery("SELECT l from LanguageTeachEntity l WHERE l.teacherId = :teacherId AND l.language = :language")
                    .setParameter("teacherId", teacher.getId())
                    .setParameter("language", language)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null;
        }


    }
    public static LanguageTeachEntity find(EntityManager em,TeacherEntity teacher, String language){
        try {
            return (LanguageTeachEntity) em.createQuery("SELECT l from LanguageTeachEntity l WHERE l.teacherId = :teacherId AND l.language = :language")
                    .setParameter("teacherId", teacher.getId())
                    .setParameter("language", language)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null;
        }


    }
    public static void updateLangguageTeach(LanguageTeachEntity entity){
        JPA.em().merge(entity);
    }
    public static void updateLangguageTeach(EntityManager em, LanguageTeachEntity entity){
        em.merge(entity);
    }
    public static void addLanguageTeach(int teacherId, String language){
        LanguageTeachEntity languageTeachEntity = new LanguageTeachEntity();
        languageTeachEntity.setTeacherId(teacherId);
        languageTeachEntity.setLanguage(language);
//        languageTeachEntity.setIntroClip(videoUri);
        languageTeachEntity.setStatus("PENDING");
        JPA.em().merge(languageTeachEntity);
    }
}
