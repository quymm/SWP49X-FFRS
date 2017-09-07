package service;

import dao.LanguageTeachDAO;
import models.LanguageTeachEntity;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by stark on 04/06/2017.
 */
public class LanguageTeachService {
    private final LanguageTeachDAO languageTeachDAO;
    @Inject
    public LanguageTeachService(LanguageTeachDAO languageTeachDAO) {
        this.languageTeachDAO = languageTeachDAO;
    }

    public boolean registerLanguagesTeach(List<LanguageTeachEntity> languagesTeach) {
        try {
            languageTeachDAO.persist(languagesTeach);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }
}
