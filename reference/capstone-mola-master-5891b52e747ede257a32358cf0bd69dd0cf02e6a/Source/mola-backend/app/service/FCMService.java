package service;

import models.UserEntity;
import utils.Const;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.EntityManager;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 * Created by stark on 14/07/2017.
 */
public class FCMService {

    public static void sendNotify(String username, String title, String message) throws IOException {
        UserEntity user = UserService.getUser(username);
        String fcmToken = user.getFirebaseToken();
        String action = "fcm.ACTION.HELLO";
        Notification notif = new Notification(fcmToken, title, message, action);

        URL obj = new URL(Const.FCM_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Authorization", "key=" + Const.FCM_SERVER_KEY);
        con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

        // Send POST body
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));

        writer.write(notif.toJson());
        writer.close();
        wr.close();
        wr.flush();
        wr.close();
        System.out.println("Response: " + con.getResponseCode() + " --- Message: " + con.getResponseMessage());
        con.disconnect();

    }
    public static int sendNotifyWithEM(String username, String title, String message, EntityManager em) throws IOException {
        UserEntity user = UserService.getUserWithEM(username, em);
        String fcmToken = user.getFirebaseToken();
        String action = "fcm.ACTION.HELLO";
        Notification notif = new Notification(fcmToken, title, message, action);

        URL obj = new URL(Const.FCM_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Authorization", "key=" + Const.FCM_SERVER_KEY);
        con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

        // Send POST body
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));

        writer.write(notif.toJson());
        writer.close();
        wr.close();
        wr.flush();
        wr.close();
        int statusCode = con.getResponseCode();
        System.out.println("Response: " + statusCode + " --- Message: " + con.getResponseMessage());
        con.disconnect();
        return statusCode;
    }
    private static class Notification {
        private String token;
        private String title;
        private String body;
        private String action;
        public String toJson(){
            String RAW_JSON = "{\"to\":\"%s\",\"notification\":{\"title\":\"%s\",\"body\":\"%s\",\"icon\":\"https://d2gg9evh47fn9z.cloudfront.net/thumb_COLOURBOX6155992.jpg\"}}";
            return String.format(RAW_JSON, this.token, this.title, this.body);
        }
        public Notification(String token, String title, String body, String action) {
            this.token = token;
            this.title = title;
            this.body = body;
            this.action = action;
        }
    }
}
