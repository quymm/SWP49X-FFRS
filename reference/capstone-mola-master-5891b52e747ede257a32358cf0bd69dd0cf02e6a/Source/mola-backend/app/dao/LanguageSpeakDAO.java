package dao;

import models.LanguageSpeakEntity;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by rocks on 6/28/2017.
 */
public class LanguageSpeakDAO {

    public static void addLanguageSpeak(String username, String language) {
        LanguageSpeakEntity languageSpeakEntity = new LanguageSpeakEntity();
        languageSpeakEntity.setUsername(username);
        languageSpeakEntity.setLanguage(language);
        JPA.em().merge(languageSpeakEntity);
    }

    public static List<String> getByUsername(String username) {
        return JPA.em().createQuery("SELECT l.language FROM LanguageSpeakEntity l WHERE l.username = :username")
                .setParameter("username", username).getResultList();
    }
}
