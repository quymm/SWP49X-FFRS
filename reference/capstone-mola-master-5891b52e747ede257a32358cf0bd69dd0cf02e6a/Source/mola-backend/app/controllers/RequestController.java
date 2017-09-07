package controllers;

import dao.TeacherDAO;
import models.LanguageTeachEntity;
import models.UserEntity;
import models.front.TeacherInfo;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import service.TeacherService;

import java.util.List;

/**
 * Created by NHAT QUANG on 6/18/2017.
 */
public class RequestController extends Controller {
    @Transactional
    public Result index(){
        List<UserEntity> listTeacher = TeacherService.getListTeacherRequest();
        return ok(views.html.request.render(session("name"), listTeacher));
    }

    @Transactional
    public Result getTeacher(String username){
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setUserEntity(JPA.em().find(UserEntity.class, username));
        teacherInfo.setTeacherEntity(TeacherDAO.getTeacherByUsername(username));
        List<LanguageTeachEntity> listLanguageTeach = TeacherService.getListLanguageTeach(teacherInfo.getTeacherEntity().getId());
        return ok(views.html.teacher.render(session("name"), teacherInfo, listLanguageTeach));
    }
}
