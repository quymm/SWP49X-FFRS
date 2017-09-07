package dao;

import models.LanguageLearnEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by rocks on 6/28/2017.
 */
public class LanguageLearnDAO {

    public static void addLanguageLearn(String username, String language) {
        LanguageLearnEntity languageLearnEntity = new LanguageLearnEntity();
        languageLearnEntity.setUsername(username);
        languageLearnEntity.setLanguage(language);
        JPA.em().persist(languageLearnEntity);
    }

    public static List<String> getByUsername(String username) {
        return JPA.em().createQuery("SELECT l.language FROM LanguageLearnEntity l WHERE l.username = :username")
                .setParameter("username", username).getResultList();
    }

    public static List<String> getByUsername(String username, EntityManager em) {
        return em.createQuery("SELECT l.language FROM LanguageLearnEntity l WHERE l.username = :username")
                .setParameter("username", username).getResultList();
    }
}
