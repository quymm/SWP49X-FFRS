package controllers;

import dao.UserDAO;
import models.TeacherEntity;
import models.front.teacher.schedule.TeacherScheduleInfo;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import security.IMolaAuthentication;
import service.TeacherService;
import utils.DateUtils;
import utils.Respond;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by stark on 06/06/2017.
 */
public class TeacherScheduleController extends Controller {
    private final TeacherService teacherService;
    private final UserDAO userDAO;

    @Inject
    public TeacherScheduleController(TeacherService teacherService, UserDAO userDAO) {
        this.teacherService = teacherService;
        this.userDAO = userDAO;
    }

    @IMolaAuthentication("")
    @Transactional
    public Result setFreeTimeSlot() {
        String username = session("username");
        TeacherEntity teacher = teacherService.findTeacherByUsername(username);
        if (teacher == null) {
            return forbidden(Json.toJson(new Respond().setStatus("error").setData("i18n =>> you not a teacher")));
        }

        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String startTime = form.get("startTime")[0];
        String endTime = form.get("endTime")[0];

        Map<String, Object> data = null;

        try {
            if (DateUtils.isSameDay(startTime, endTime)) {
                data = teacherService.setFreeTimeSlot(startTime, endTime, teacher.getId());
            } else {
                data = teacherService.setFreeTimeSlots(startTime, endTime, teacher.getId());
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        Respond r = new Respond().setData(data);
        if (data.containsKey("violated")) {
            r.setStatus("warn");
        } else {
            r.setStatus("ok");
        }

        return ok(Json.toJson(r.setData(data)));
    }

    @IMolaAuthentication("")
    @Transactional(readOnly = true)
    public Result getSessionToday() throws ParseException {
        String username = session("username");
        TeacherEntity teacher = teacherService.findTeacherByUsername(username);
        if (teacher == null) {
            return forbidden(Json.toJson(new Respond().setStatus("error").setData("i18n =>> you not a teacher")));
        }
        String from = request().getQueryString("from"); //"2017-06-07 00:00:00";
        String to = request().getQueryString("to");//"2017-06-08 00:00:00";

        TeacherScheduleInfo data = teacherService.getScheduleToday(from, to, teacher.getId());

        return ok(Json.toJson(new Respond().setStatus("ok").setData(data != null ? data : "Start time or End time violated time slot constraint")));
    }


}
