package controllers;

import dao.LanguageSpeakDAO;
import dao.LanguageTeachDAO;
import models.LanguageTeachEntity;
import models.TeacherEntity;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import security.IMolaAuthentication;
import service.LanguageTeachService;
import service.TeacherService;
import utils.Respond;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by stark on 01/06/2017.
 */
public class TeacherSignUpController extends Controller {
    private final TeacherService teacherService;
    private final LanguageTeachService languageTeachService;

    @Inject
    public TeacherSignUpController(TeacherService teacherService, LanguageTeachService languageTeachService) {
        this.teacherService = teacherService;
        this.languageTeachService = languageTeachService;
    }

    @IMolaAuthentication("")
    @Transactional
    public Result signUp() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();

        String username = form.get("username")[0];
        String birthday = form.get("birthday")[0];
        String experienceYear = form.get("experienceYear")[0];
        String experienceAbout = form.get("experienceAbout")[0];
//        TeacherFileURI[] avatarUri = parseWithProperty(form, "avatarURI");//form.get("avatarURI")[0];
//        TeacherFileURI[] videoUri = parseWithProperty(form, "videoURI");//form.get("videoURI")[0];
        String[] languagesSpeak = parseWithPropertyLanguage(form, "languagesSpeak");
        String[] languagesTeach = parseWithPropertyLanguage(form, "languagesTeach");

        TeacherEntity t = teacherService.registerTeacher(username, Integer.valueOf(experienceYear), experienceAbout);
        for (int i = 0; i < languagesSpeak.length; i++) {
            LanguageSpeakDAO.addLanguageSpeak(t.getUsername(), languagesSpeak[i]);
        }

        for (int i = 0; i < languagesTeach.length; i++) {
            LanguageTeachDAO.addLanguageTeach(t.getId(), languagesTeach[i]);
        }


//        List<LanguageTeachEntity> teach = teachLanguages(t, videoUri);

//        boolean isOK = languageTeachService.registerLanguagesTeach(teach);

//        if (isOK){
        return ok(Json.toJson(new Respond().setStatus("ok").setData(t).setMessage("")));

//        } else {
//            return badRequest(Json.toJson(new Respond().setStatus("error").setData("teacher signup fail")));
//        }
    }

    public String[] parseWithPropertyLanguage(Map<String, String[]> form, String type) {
        String propLanguagesSpeak = "%s[%s]";
        List<String> list = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                String language = form.get(String.format(propLanguagesSpeak, type, i))[0];

                if (language == null) {
                    break;
                }
                list.add(language);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return list.toArray(new String[list.size()]);
    }

    public List<LanguageTeachEntity> teachLanguages(TeacherEntity teacher, TeacherFileURI[] videoUri) {
        List<LanguageTeachEntity> teach = new ArrayList<>();
        for (TeacherFileURI u : videoUri) {
            teach.add(new LanguageTeachEntity(teacher.getId(), u.getLanguage(), u.getFilePath()));
        }
        return teach;
    }

    public TeacherFileURI[] parseWithProperty(Map<String, String[]> form, String property) {
        String propURIFilePath = "%s[%s][filePath]";
        String propURIFileName = "%s[%s][fileName]";
        String propURIDescription = "%s[%s][description]";

        List<TeacherFileURI> files = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                String path = form.get(String.format(propURIFilePath, property, i))[0];
                String name = form.get(String.format(propURIFileName, property, i))[0];
                String description = form.get(String.format(propURIDescription, property, i))[0];
                if (path == null || name == null || description == null) {
                    break;
                }
                files.add(new TeacherFileURI(name, path, description, description));
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return files.toArray(new TeacherFileURI[files.size()]);
    }
}

class TeacherFileURI extends FileUploadResult {
    private String language;

    public TeacherFileURI(String fileName, String filePath, String description, String language) {
        super(fileName, filePath, description);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}