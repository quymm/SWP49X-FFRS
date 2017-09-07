package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.JOSEException;
import dao.*;
import javafx.util.Pair;
import models.*;
import models.front.*;
import models.front.learner.schedule.LeanerScheduleInfo;
import models.front.teacher.schedule.SessionInfo;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import play.cache.CacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import security.IMolaAuthentication;
import security.JWTProvider;
import service.*;
import utils.Const;
import utils.LuceneInitialIndexed;
import utils.Respond;
import utils.Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class ApiController extends play.mvc.Controller {
    @Inject
    SearchService searchService;
    @Inject
    CourseDAO courseDAO;
    @Inject
    SessionService sessionService;
    @Inject
    private FormFactory formFactory;
    @Inject
    private WSClient ws;
    @Inject
    private JPAApi jpaApi;
    @Inject
    private CacheApi cache;

    @IMolaAuthentication("")
    @Transactional
    public Result saveFirebaseToken() {
        String token = request().body().asFormUrlEncoded().get("token")[0];
        String username = session("username");
        try {
            int updated = UserService.saveFirebaseToken(username, token);
            if (updated > 0) {
                return ok(Json.toJson(new Respond().setStatus("ok")));
            } else {
                return ok(Json.toJson(new Respond().setStatus("err").setData("Cannot update token")));
            }

        } catch (Exception e) {
            return ok(Json.toJson(new Respond().setStatus("err").setData(e.getMessage())));
        }
    }

    @Transactional
    public Result userLogin() throws JOSEException, NoSuchAlgorithmException, ParseException {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String username = form.get("username")[0];
        String password = form.get("password")[0];
        UserLogin userLogin = new UserLogin(username, password);

        Subject currUser = new Subject.Builder().sessionId(Utils.getSessionKey()).buildSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(userLogin.getUsername(), userLogin.getPassword());

        try {
            currUser.login(token);
            if (currUser.hasRole(Const.ROLE_LEARNER)) {
                Utils.setSessionKey(currUser.getSession().getId().toString());

                currUser.getSession().setAttribute("username", userLogin.getUsername());
                String tokenJWT = JWTProvider.generateJWT(userLogin.getUsername());

                UserEntity userEntity = UserService.getUser(userLogin.getUsername());
                UserInfo userInfo = new UserInfo();
                userInfo.username = userEntity.getUsername();
                userInfo.firstName = userEntity.getFirstName();
                userInfo.lastName = userEntity.getLastName();
                userInfo.avatar = userEntity.getAvatar();
                userInfo.email = userEntity.getEmail();
                userInfo.isTeacher = UserDAO.isTeacher(userEntity.getUsername());
                userInfo.fcmToken = userEntity.getFirebaseToken();

                Map<String, Object> result = new HashMap<>();
                result.put("token", tokenJWT);
                result.put("user", userInfo);

                return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
            }
        } catch (UnknownAccountException uae) {
            System.out.println("No user " + userLogin.getUsername());
        } catch (IncorrectCredentialsException ice) {
            System.out.println("Incorrect password");
        } catch (LockedAccountException lae) {
            System.out.println("locked");
        } catch (AuthenticationException e) {
            System.out.println("authen");
        }
        Map<String, String> err = new HashMap<>();
        err.put("status", "error");
        err.put("message", "Invalid username or password");

        return badRequest(Json.toJson(err));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result reLogin() throws JOSEException, NoSuchAlgorithmException, ParseException {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String username = form.get("username")[0];
        if (username.equals(session("username"))) {
            String tokenJWT = JWTProvider.generateJWT(username);
            UserEntity userEntity = UserService.getUser(username);
            UserInfo userInfo = new UserInfo();
            userInfo.username = userEntity.getUsername();
            userInfo.firstName = userEntity.getFirstName();
            userInfo.lastName = userEntity.getLastName();
            userInfo.avatar = userEntity.getAvatar();
            userInfo.email = userEntity.getEmail();
            userInfo.isTeacher = UserDAO.isTeacher(userEntity.getUsername());
            userInfo.fcmToken = userEntity.getFirebaseToken();

            Map<String, Object> result = new HashMap<>();
            result.put("token", tokenJWT);
            result.put("user", userInfo);

            return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
        } else {
            return ok(Json.toJson(new Respond().setStatus("err")));
        }


    }

    @Transactional
    public CompletionStage<Result> userLoginFacebook() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String accessToken = form.get("accessToken")[0];

        WSRequest wsRequest = ws.url(Const.FACEBOOK_GRAPH_API_URL)
                .setQueryParameter("access_token", accessToken)
                .setQueryParameter("fields", Const.FACEBOOK_PERMISSION);
        CompletionStage<JsonNode> fbProfilePromise = wsRequest.get().thenApply(WSResponse::asJson);
        return fbProfilePromise.thenApply(jsonNode -> {
            String id = jsonNode.get("id").asText();
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                UserEntity user = em.find(UserEntity.class, id);
                if (user == null) {
                    String firstname = jsonNode.get("first_name").asText();
                    String lastname = jsonNode.get("last_name").asText();
                    System.out.println(lastname);
                    System.out.println(firstname);
                    String avatar = jsonNode.get("picture").get("data").get("url").asText();
                    user = new UserEntity();
                    user.setUsername(id);
                    user.setPassword(Utils.genRandomPassword());
                    user.setAvatar(avatar);
                    user.setFacebookId(id);
                    user.setFirstName(firstname);
                    user.setLastName(lastname);
                    em.persist(user);

                    UserRoleEntity userRoleEntity = new UserRoleEntity();
                    userRoleEntity.setUsername(user.getUsername());
                    userRoleEntity.setRoleName(Const.ROLE_LEARNER);
                    em.persist(userRoleEntity);

                }

                try {
                    String tokenJWT = JWTProvider.generateJWT(user.getUsername());
                    Map<String, Object> result = new HashMap<>();
                    result.put("token", tokenJWT);
                    result.put("user", user);

                    return ok(Json.toJson(new Respond("ok", result)));

                } catch (NoSuchAlgorithmException e) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("message", e.getMessage());
                    return badRequest(Json.toJson(new Respond("err", result)));
                } catch (JOSEException e) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("message", e.getMessage());
                    return badRequest(Json.toJson(new Respond("err", result)));
                } catch (ParseException e) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("message", e.getMessage());
                    return badRequest(Json.toJson(new Respond("err", result)));
                }

            });
        });
    }


    public Result logout() {
        Subject currUser = new Subject.Builder().sessionId(Utils.getSessionKey()).buildSubject();
        currUser.logout();
        return ok();
    }

    @Transactional
    public Result register() {

        Form<UserRegister> formRegister = formFactory.form(UserRegister.class).bindFromRequest();
        if (formRegister.hasErrors()) {
            Map<String, String> result = new HashMap<>();
            result.put("error", formRegister.globalError().message());
            return badRequest(Json.toJson(result));
        }

        UserRegister userRegister = formRegister.get();
        UserEntity newUser = new UserEntity();
        newUser.setUsername(userRegister.getUsername());
        newUser.setPassword(userRegister.getPassword());
        newUser.setEmail(userRegister.getEmail());
        newUser.setFirstName(userRegister.getFirstName());
        newUser.setLastName(userRegister.getLastName());

        newUser = UserDAO.register(newUser);

        // Auto login for newly registered user
        try {
            String tokenJWT = JWTProvider.generateJWT(userRegister.getUsername());

            Map<String, Object> result = new HashMap<>();
            result.put("token", tokenJWT);
            result.put("user", newUser);

            return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
        } catch (NoSuchAlgorithmException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return badRequest(Json.toJson(new Respond("err", result)));
        } catch (JOSEException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return badRequest(Json.toJson(new Respond("err", result)));
        } catch (ParseException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return badRequest(Json.toJson(new Respond("err", result)));
        }
    }

    @Transactional
    public Result search() {
        String query = request().getQueryString("q");

        List<CourseEntity> courses = searchService.searchCourses(query);
        List<BSearchResultInfo> searchResult = new ArrayList<>();

        courses.forEach(course -> {
            TeacherEntity teacher = courseDAO.findTeacherByCourse(course);
            UserEntity user = UserDAO.getUser(teacher.getUsername());

            searchResult.add(new BSearchResultInfo(course, teacher, user));
        });
        return ok(Json.toJson(new Respond().setStatus("ok").setData(searchResult)));
    }

    @Transactional
    public Result getUserProfile(String username) {
        UserEntity userEntity = UserDAO.getUser(username);
        UserInfo userInfo = new UserInfo();
        userInfo.username = userEntity.getUsername();
        userInfo.firstName = userEntity.getFirstName();
        userInfo.lastName = userEntity.getLastName();
        userInfo.avatar = userEntity.getAvatar();
        userInfo.email = userEntity.getEmail();
        userInfo.isTeacher = UserDAO.isTeacher(userEntity.getUsername());
        userInfo.fcmToken = userEntity.getFirebaseToken();
        return ok(Json.toJson(userInfo));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result updatePassword() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String password = form.get("password")[0];
        String newPassword = form.get("newPassword")[0];
        if (UserService.updatePassword(session("username"), password, newPassword)) {
            return ok(Json.toJson(new Respond().setStatus("ok")));
        } else {
            return ok(Json.toJson(new Respond().setStatus("error")));
        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getCourseByTeacher() {
        List<CourseEntity> list = CourseService.getByTeacher(session("username"));
        return ok(Json.toJson(new Respond().setStatus("ok").setData(list)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getChapterByCourse(int courseId) {
        String username = session("username");

        List<ChapterEntity> list = CourseService.getChapterByCourse(username, courseId);
        return ok(Json.toJson(new Respond().setStatus("ok").setData(list)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result createCourse() {
        TeacherEntity teacher = TeacherDAO.getTeacherByUsername(session("username"));
        Form<BCourseInfo> form = formFactory.form(BCourseInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BCourseInfo courseInfo = form.get();
            CourseEntity course = CourseService.createCourse(courseInfo, teacher.getId());
            return ok(Json.toJson(new Respond("ok", course)));
        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result editCourse() {
        Form<BCourseInfo> form = formFactory.form(BCourseInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BCourseInfo courseInfo = form.get();
            CourseService.editCourse(courseInfo);
            return ok(Json.toJson(new Respond("ok", null)));
        }
    }


    @IMolaAuthentication("")
    @Transactional
    public Result deleteCourse(int id) {
        CourseService.deleteCourse(id);
        return ok(Json.toJson(new Respond().setStatus("ok")));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result deleteChapter(int id) {
        CourseService.deleteChapter(id);
        return ok(Json.toJson(new Respond().setStatus("ok")));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result addChapter() {
        Form<BChapterInfo> form = formFactory.form(BChapterInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BChapterInfo chapterInfo = form.get();
            ChapterEntity chapter = CourseService.addChapter(chapterInfo);
            return ok(Json.toJson(new Respond("ok", chapter)));
        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result editChapter() {
        Form<BChapterInfo> form = formFactory.form(BChapterInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BChapterInfo chapterInfo = form.get();
            CourseService.editChapter(chapterInfo);
            return ok(Json.toJson(new Respond("ok", null)));
        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result updateListLesson() {
        Form<BListLesson> form = formFactory.form(BListLesson.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BListLesson listLesson = form.get();
            CourseService.updateListLesson(listLesson);
            return ok(Json.toJson(new Respond("ok", listLesson)));
        }
    }

    @Transactional
    public Result reIndexLucene() {
        try {
            LuceneInitialIndexed.indexExistedCourses();
            return ok(Json.toJson(new Respond().setStatus("ok").setData("reindex success")));
        } catch (Exception e) {
            return badRequest(Json.toJson(new Respond().setStatus("error").setData("reindex fail")));
        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result createLesson() {
        Form<BLessonInfo> form = formFactory.form(BLessonInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BLessonInfo bLessonInfo = form.get();
            LessonEntity lessonEntity = CourseService.createLesson(bLessonInfo);
            return ok(Json.toJson(new Respond().setStatus("ok").setData(lessonEntity)));

        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result updateLesson() {
        Form<BLessonInfo> form = formFactory.form(BLessonInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", form.globalError().message());
            return ok(Json.toJson(new Respond("err", message)));
        } else {
            BLessonInfo bLessonInfo = form.get();
            LessonEntity lessonEntity = CourseService.updateLesson(bLessonInfo);
            return ok(Json.toJson(new Respond().setStatus("ok").setData(lessonEntity)));

        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result deleteLesson(int id) {
        CourseService.deleteLesson(id);
        return ok(Json.toJson(new Respond().setStatus("ok")));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result userBasedRecommendations() {
        String username = session("username");
        List<Pair> recommendations = RecommendationService.getRecommendation(username, cache);

        List<BSearchResultInfo> coursesRecommend = getRecommendInSearchResult(recommendations);

        return ok(Json.toJson(new Respond().setStatus("ok").setData(coursesRecommend)));

    }

    @IMolaAuthentication("")
    @Transactional
    public Result itemBasedRecommendations(int courseId) {
        String username = session("username");
        List<Pair> recommendations = RecommendationService.getSimilarCourse(courseId, cache);

        List<BSearchResultInfo> coursesRecommend = getRecommendInSearchResult(recommendations);

        return ok(Json.toJson(new Respond().setStatus("ok").setData(coursesRecommend.subList(0, 5))));

    }


    private List<BSearchResultInfo> getRecommendInSearchResult(List<Pair> recommendations) {
        List<BSearchResultInfo> coursesRecommend = new ArrayList<>();
        recommendations.forEach(recommend -> {

            CourseEntity course = null;

            if (recommend.getKey() instanceof String) {
                course = courseDAO.findOne(Integer.valueOf(recommend.getKey().toString()));
            } else {
                course = courseDAO.findOne((Integer) recommend.getKey());
            }
            TeacherEntity teacher = courseDAO.findTeacherByCourse(course);
            UserEntity user = UserDAO.getUser(teacher.getUsername());

            coursesRecommend.add(new BSearchResultInfo(course, teacher, user));
        });
        return coursesRecommend;
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getUpcomingSession() {
        List<BSession> listSession = SessionService.getUpcomingSession(session("username"));
        Collections.sort(listSession, Comparator.comparing(o -> o.getTimeSlotEntity().getStarTime()));
        return ok(Json.toJson(new Respond().setStatus("ok").setData(listSession)));
    }

    @Transactional
    public Result getFreeTimeSlotOfMonth(int teacherId) throws ParseException {
        String selectDate = request().getQueryString("select_date");

        List<TimeSlotByDate> result = SessionService.getFreeTimeSlotByMonth(teacherId, selectDate);
        return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getCourseRegisted() {

        String username = session("username");
        List<RegisterCourseInfo> listCourse = CourseService.getCourseRegisted(username);
        return ok(Json.toJson(new Respond("ok", listCourse)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result enrollCourse() throws IOException {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String username = form.get("username")[0];
        String courseId = form.get("courseId")[0];

        String result = CourseService.enrollUserToCourse(username, Integer.valueOf(courseId));

        return ok(Json.toJson(new Respond("ok", result)));
    }

    @Transactional
    public Result checkEnrollUserInCourse() {
        String username = request().getQueryString("username");
        String courseId = request().getQueryString("courseId");

        String enrolled = CourseService.checkEnrolledUserInCourse(username, Integer.valueOf(courseId));

        return ok(Json.toJson(new Respond().setStatus("ok").setData(enrolled)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result requestSession() throws ParseException {
        String username = session("username");
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        int timeSlotId = Integer.valueOf(form.get("timeSlotId")[0]);
        int lessonId = Integer.valueOf(form.get("lessonId")[0]);
        String startTime = form.get("startTime")[0];
        Object result = SessionService.requestSession(timeSlotId, lessonId, startTime, username);
        if (result == null) {
            return ok(Json.toJson(new Respond().setStatus("ok")));
        } else {
            if (result instanceof String) {
                return ok(Json.toJson(new Respond().setStatus("err").setMessage((String) result)));
            } else {
                return ok(Json.toJson(new Respond().setStatus("err").setData(result)));
            }
        }

    }

    @IMolaAuthentication("")
    @Transactional
    public Result validateRequestSession() {
        String username = session("username");
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        int timeSlotId = Integer.valueOf(form.get("timeSlotId")[0]);
        int lessonId = Integer.valueOf(form.get("lessonId")[0]);
        String startTime = form.get("startTime")[0];
        Object result = SessionService.validateRequestSession(timeSlotId, lessonId, startTime, username);
        if (result == null) {
            return ok(Json.toJson(new Respond().setStatus("ok")));
        } else {
            if (result instanceof String) {
                return ok(Json.toJson(new Respond().setStatus("err").setMessage((String) result)));
            } else {
                return ok(Json.toJson(new Respond().setStatus("err").setData(result)));
            }
        }

    }



    @IMolaAuthentication("")
    @Transactional
    public Result getListIncomingRequest() {
        Map<String, Object> result = SessionService.getListIncomingRequest(session("username"));
        return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getListSendingRequest() {
        Map<String, Object> result = SessionService.getListSendingRequest(session("username"));
        return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getListRatingTeacher(int teacherId) {
        List<BRating> listRating = RatingService.getListRatingTeacher(teacherId);
        return ok(Json.toJson(new Respond().setStatus("ok").setData(listRating)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result setMultiLanguageSpeakLearn() {
        String username = session("username");

        List<String> languageSpeak = request().body().asJson().findValue("languageSpeak").
                findValuesAsText("name");
        for (String language : languageSpeak
                ) {
            LanguageSpeakDAO.addLanguageSpeak(username, language);
        }

        List<String> languageLearn = request().body().asJson().findValue("languageLearn").
                findValuesAsText("name");
        for (String language : languageLearn
                ) {
            LanguageLearnDAO.addLanguageLearn(username, language);
        }

        return ok(Json.toJson(new Respond().setStatus("ok")));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result updateAvatar() {
        String username = session("username");
        String avatar = request().body().asJson().findValue("filePath").asText();
        UserEntity userEntity = UserDAO.updateAvatar(username, avatar);

        return ok(Json.toJson(new Respond().setStatus("ok").setData(userEntity)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getExtraTeacherInfo(int teacherId) {
        ExtraTeacherInfo extraTeacherInfo = TeacherService.getExtraInfo(session("username"), teacherId);
        return ok(Json.toJson(new Respond().setStatus("ok").setData(extraTeacherInfo)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result editUser() {
        String username = session("username");

        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String receivedUsername = form.get("username")[0];
        String email = form.get("email")[0];
        String firstName = form.get("firstName")[0];
        String lastName = form.get("lastName")[0];

        if (!receivedUsername.equals(username)) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "Username not valid for this session!");
            return ok(Json.toJson(new Respond().setStatus("error").setData(result)));
        }
        if (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+" +
                "(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "Invalid email address");
            return ok(Json.toJson(new Respond().setStatus("error").setData(result)));
        }
        if (firstName == null || firstName.isEmpty()) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "First Name is required");
            return ok(Json.toJson(new Respond().setStatus("error").setData(result)));
        }
        if (lastName == null || lastName.isEmpty()) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "Last Name is required");
            return ok(Json.toJson(new Respond().setStatus("error").setData(result)));
        }

        UserEntity userEntity = UserDAO.getUser(username);
        userEntity.setEmail(email);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);

        UserEntity result = UserDAO.updateUser(userEntity);

        return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getMultiLanguage() {
        String username = session("username");
        Map<String, List> result = new HashMap<>();

        List<LanguageSelect> languageSpeak = new ArrayList<>();
        for (String language : LanguageSpeakDAO.getByUsername(username)) {
            languageSpeak.add(new LanguageSelect(language));
        }

        List<LanguageSelect> languageLearn = new ArrayList<>();
        for (String language : LanguageLearnDAO.getByUsername(username)) {
            languageLearn.add(new LanguageSelect(language));
        }

        List<LanguageSelect> languageTeach = new ArrayList<>();
        TeacherEntity teacher = TeacherDAO.getTeacherByUsername(username);
        if (teacher != null) {
            for (String language : LanguageTeachDAO.getByUsername(teacher.getId())) {
                languageTeach.add(new LanguageSelect(language));
            }
        }

        result.put("languageSpeak", languageSpeak);
        result.put("languageLearn", languageLearn);
        result.put("languageTeach", languageTeach);
        return ok(Json.toJson(new Respond().setStatus("ok").setData(result)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result cancelCourseRegisterRequest(int courseRegisterId) throws IOException {
        CourseService.cancelRegisterCourseRequest(courseRegisterId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result rejectCourseRegisterRequest(int courseRegisterId) throws IOException {
        CourseService.rejectRegisterCourseRequest(courseRegisterId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result acceptCourseRegisterRequest(int courseRegisterId) throws IOException {
        CourseService.acceptRegisterCourseRequest(courseRegisterId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result acceptSessionRequest(int sessionId) throws IOException {
        SessionService.acceptSessionRequest(sessionId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result cancelSessionRequest(int sessionId) throws IOException, ParseException {
        sessionService.cancelSessionRequest(sessionId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result rejectSessionRequest(int sessionId) throws IOException, ParseException {
        sessionService.rejectSessionRequest(sessionId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result finishedSession() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        int sessionId = Integer.valueOf(form.get("sessionId")[0]);
        SessionService.finishedSession(sessionId);

        return ok();
    }

    @Transactional
    public Result findUsers() {
        String u = request().getQueryString("u");
        if (u.isEmpty()) {
            return badRequest("u param is empty");
        }
        String[] usernames = u.split(",");

        List<UserEntity> users = new ArrayList<>();
        for (int i = 0; i < usernames.length; i++) {
            UserEntity user = UserService.getUser(usernames[i]);
            users.add(user);
        }
        return ok(Json.toJson(new Respond().setStatus("ok").setData(users)));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getSingleLanguageSpeak() {
        String username = session("username");
        String language = LanguageSpeakDAO.getByUsername(username).get(0);

        return ok(Json.toJson(new Respond().setStatus("ok").setData(language)));
    }

    @Transactional
    public Result sendNotify() {
        String username = request().body().asFormUrlEncoded().get("username")[0];
        String title = request().body().asFormUrlEncoded().get("title")[0];
        String message = request().body().asFormUrlEncoded().get("message")[0];

        try {
            FCMService.sendNotify(username, title, message);
            return ok("sent notify");
        } catch (IOException e) {
            return badRequest("send error");
        }
    }

    @Transactional
    public Result introClip(int teacherId) {
        List<LanguageTeachEntity> listLanguageTeach = TeacherService.getLanguageTeachClip(teacherId);
        return ok(views.html.clip.render(listLanguageTeach));
    }

    @Transactional
    public Result test() throws IOException {
        FCMService.sendNotify("hoangdd", "test", "test");
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional(readOnly = true)
    public Result getLearnerScheduleNotFinished() {
        String username = session("username");
        UserEntity user = UserDAO.getUser(username);
        if (user == null) {
            return forbidden(Json.toJson(new Respond().setStatus("error").setData("i18n =>> you not a teacher")));
        }
        String from = request().getQueryString("from"); //"2017-06-07 00:00:00";
        String to = request().getQueryString("to");//"2017-06-08 00:00:00";

        List<SessionInfo> data = null;
        try {
            data = sessionService.getScheduleLearnerNotFinished(from, to, username);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return internalServerError("Server error: " + e.getMessage());
        }
        if (data != null) {
            LeanerScheduleInfo info = new LeanerScheduleInfo();
            info.setSessionInfos(data.toArray(new SessionInfo[data.size()]));


            return ok(Json.toJson(new Respond().setStatus("ok").setData(info)));
        } else {
            return badRequest(Json.toJson(new Respond("err", "bad request, try again")));
        }

    }

    @IMolaAuthentication("")
    @Transactional
    public Result addTeacherRating() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        int rate = Integer.valueOf(form.get("rate")[0]);
        String comment = form.get("comment")[0];
        int teacherId = Integer.valueOf(form.get("teacherId")[0]);
        RatingService.ratingTeacher(rate, session("username"), comment, teacherId);
        return ok();
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getTeacherInfo(String username){
        TeacherEntity teacher = TeacherDAO.getTeacherByUsername(username);
        if (teacher != null){
            return ok(Json.toJson(new Respond().setData(teacher).setStatus("ok")));
        }
        return badRequest();
    }

    public Result rec() {
        return ok(Json.toJson(new Respond().setStatus("ok").setData(cache.get(Const.USER_BASED_DATA_SET_CACHE_KEY))));
    }

    @IMolaAuthentication("")
    @Transactional
    public Result getMonthlyRevenue(){
        TeacherEntity teacher = TeacherDAO.getTeacherByUsername(session("username"));
        return ok(Json.toJson(new Respond().setStatus("ok").setData(TeacherService.getMonthlyRevenue(teacher.getId()))));
    }
}