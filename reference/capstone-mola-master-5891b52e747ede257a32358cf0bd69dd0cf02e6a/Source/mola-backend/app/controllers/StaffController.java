package controllers;

import dao.TeacherDAO;
import models.TeacherEntity;
import models.UserEntity;
import models.UserRoleEntity;
import models.front.BStaff;
import models.front.BStaffUpdate;
import models.front.UserLogin;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.FCMService;
import service.StaffService;
import service.TeacherService;
import service.UserService;
import utils.Respond;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by NGOCHIEU on 2017-05-26.
 */
public class StaffController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Transactional
    public Result login(){
        Form<UserLogin> formLogin = formFactory.form(UserLogin.class).bindFromRequest();
        if (formLogin.hasErrors()) {
            return ok(views.html.login.render(formLogin));
        } else {
            String username = formLogin.get().getUsername();
            UserEntity staff = JPA.em().find(UserEntity.class, username);
            session("name", staff.getFirstName());
            List<UserEntity> listTeacher = TeacherService.getListTeacherRequest();
            return ok(views.html.request.render(session("name"),listTeacher));
        }
    }

    @Transactional
    public Result staffManagement(){
        Form<BStaff> staffForm = formFactory.form(BStaff.class);
        return ok(views.html.user.render(StaffService.getAllStaff(), session("name"), staffForm));
    }

    public Result logout(){
        session().clear();
        return redirect(routes.HomeController.index());
    }

    @Transactional
    public Result addStaff(){
        Form<BStaff> form = formFactory.form(BStaff.class).bindFromRequest();
        if (form.hasErrors()){

            return badRequest(form.globalError().message());
//            return ok(views.html.user.render(StaffService.getAllStaff(), session("name"), form));
//            return ok("error");
        } else {
            BStaff bStaff = form.get();
            StaffService.addStaff(bStaff);
//            return ok(loadUserManagementPage());
            return ok("success");
        }
    }

    @Transactional
    public Result updateStaff(){
        Form<BStaffUpdate> form = formFactory.form(BStaffUpdate.class).bindFromRequest();
        if (form.hasErrors()){
            return badRequest(form.globalError().message());
        } else {
            BStaffUpdate bStaffUpdate = form.get();
            StaffService.updateStaff(bStaffUpdate);
            return ok("success");
        }
    }

    @Transactional
    public Result deleteStaff(String username){
        StaffService.deleteStaff(username);
        return redirect(routes.StaffController.staffManagement());
    }


    @Transactional
    public Result acceptLanguageTeachRequest() throws IOException {
        String teacherId = request().body().asFormUrlEncoded().get("teacherId")[0];
        String language = request().body().asFormUrlEncoded().get("language")[0];
        if (StaffService.acceptLanguageTeachRequest(teacherId, language)){
            TeacherEntity teacherEntity = TeacherDAO.getTeacherById(Integer.valueOf(teacherId));
            teacherEntity.setStatus("ACCEPTED");
            JPA.em().merge(teacherEntity);
            UserService.addRoleTeacher(teacherEntity.getUsername());

            FCMService.sendNotify(teacherEntity.getUsername(), "Accept Teaching", "You have been accepted to teach language " + language);
            return ok(Json.toJson(new Respond().setStatus("ok")));
        } else {
            return ok(Json.toJson(new Respond().setStatus("err")));
        }

    }

}
