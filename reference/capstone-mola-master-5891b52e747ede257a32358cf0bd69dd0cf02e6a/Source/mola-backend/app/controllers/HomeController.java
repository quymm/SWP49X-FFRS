package controllers;

import models.UserEntity;
import models.front.UserLogin;
import play.Routes;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;
import service.TeacherService;

import javax.inject.Inject;
import java.util.List;


/**
 * Created by NGOCHIEU on 2017-05-26.
 */
public class HomeController extends Controller{
    @Inject
    FormFactory formFactory;

    @Transactional
    public Result index(){
        String user = session("name");
        if (user != null) {
            List<UserEntity> listTeacher = TeacherService.getListTeacherRequest();
            return ok(views.html.request.render(user,listTeacher));
        } else {
            return ok(views.html.login.render(formFactory.form(UserLogin.class)));
        }
    }


    public Result javascriptRoutes(){
        return ok(
                JavaScriptReverseRouter.create("myJsRoutes",
                        routes.javascript.StaffController.addStaff(),
                        routes.javascript.StaffController.updateStaff(),
                        routes.javascript.StaffController.acceptLanguageTeachRequest())
        ).as("text/javascripts");
    }

    public static String getSessionKey(){
        return session("seskey");
    }

    public static void setSessionKey(String sessionKey){
        session("seskey", sessionKey);
    }

}
