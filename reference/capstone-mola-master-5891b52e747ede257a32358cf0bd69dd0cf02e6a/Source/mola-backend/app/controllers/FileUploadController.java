package controllers;

import dao.LanguageTeachDAO;
import dao.TeacherDAO;
import dao.UserDAO;
import models.LanguageTeachEntity;
import models.TeacherEntity;
import models.UserEntity;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import security.IMolaAuthentication;
import utils.Const;
import utils.FileHelper;
import utils.Respond;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * Created by stark on 01/06/2017.
 */
public class FileUploadController extends Controller {
    private final LanguageTeachDAO languageTeachDAO;
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String IMAGE_JPEG = "image/jpeg";
    private List<FileUploadResult> avatar = new ArrayList<>();
    private List<FileUploadResult> videos = new ArrayList<>();

    @Inject
    private JPAApi jpaApi;

    @Inject
    public FileUploadController(LanguageTeachDAO languageTeachDAO) {
        this.languageTeachDAO = languageTeachDAO;
    }


    private void teachLanguage(EntityManager em, TeacherEntity teacher, String name, String videoUri){
        String[] filename = name.split("_");
        String language = filename[filename.length-2];

        LanguageTeachEntity langTeach = LanguageTeachDAO.find(em, teacher, language);
        if (langTeach != null){
            langTeach.setIntroClip(videoUri);
            LanguageTeachDAO.updateLangguageTeach(em, langTeach);
        }
    }

    @IMolaAuthentication("")
    @Transactional
    public Result upload() throws IOException {
        String username = session("username");
        avatar = new ArrayList<>();
        videos = new ArrayList<>();
        Map<String, List<FileUploadResult>> results = new HashMap<>();
        results.clear();
        MultipartFormData<File> body = request().body().asMultipartFormData();

        new Thread(
            () -> {
                body.getFiles().forEach(file -> {
                    try {
                        Map<String, List<FileUploadResult>> result = uploadFile(file);
                        if (result.containsKey("user_avatar")){
                            List<FileUploadResult> avatar = result.get("user_avatar");
                            jpaApi.withTransaction(()-> {
                                EntityManager em = jpaApi.em();
                                avatar.forEach(a -> {
                                    UserEntity user = UserDAO.getUser(em, username);
                                    user.setAvatar(a.getFilePath());
                                    UserDAO.merge(em, user);
                                });
                            });

                        }

                        if (result.containsKey("user_videos")){
                            List<FileUploadResult> avatar = result.get("user_videos");
                            jpaApi.withTransaction(()->{
                                EntityManager em = jpaApi.em();
                                UserEntity userEntity = em.find(UserEntity.class, "thanhov");
                                System.out.println(userEntity.getUsername());
                                avatar.forEach(a -> {
                                    TeacherEntity teacher = TeacherDAO.getTeacherByUsername(username, em);
                                    teachLanguage(em, teacher, file.getKey(), a.getFilePath());
                                });
                            });

                        }

                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
        ).start();

        return ok(Json.toJson(new Respond().setStatus("ok")));
    }

    private Map<String, List<FileUploadResult>> uploadFile(FilePart<File> filePart) throws IOException {
        File file = filePart.getFile();
        Map<String, List<FileUploadResult>> results = new HashMap<>();

        if (file != null) {
            String contentType = filePart.getContentType();
            FileInputStream stream = new FileInputStream(file);
            String fileNameToUpload = String.format("%s_%s", (new Date()).getTime(), filePart.getFilename());

            if (contentType.equals(IMAGE_JPEG)) {

                System.out.println("File name avatar: " + fileNameToUpload);

                if (FileHelper.UploadImage(stream, fileNameToUpload)) {
                    avatar.add(new FileUploadResult(fileNameToUpload, Const.IMG_URL + fileNameToUpload, "avatar"));
                    results.put("user_avatar", avatar);
                }
            } else if (contentType.equals(VIDEO_MP4)) {

                if (!fileNameToUpload.endsWith(".mp4")) {
                    fileNameToUpload += ".mp4";
                }
                System.out.println("File name video: " + fileNameToUpload);

                if (FileHelper.UploadClip(stream, fileNameToUpload)) {
                    String[] fileDescription = filePart.getKey().split("_");
                    videos.add(new FileUploadResult(fileNameToUpload, Const.CLIP_URL + fileNameToUpload, fileDescription[1]));
                    results.put("user_videos", videos);
                }
            }

            stream.close();
        }
        return results;
    }
}

class FileUploadResult {
    private String fileName;
    private String filePath;
    private String description;

    public FileUploadResult(String fileName, String filePath, String description) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDescription() {
        return description;
    }
}