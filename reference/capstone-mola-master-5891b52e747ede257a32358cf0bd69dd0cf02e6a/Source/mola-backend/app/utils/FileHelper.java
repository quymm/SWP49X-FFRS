package utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class FileHelper {

    public static final String SERVER_URL = "http://112.78.4.97/fpt/mola/image.php";
    public static final String IMAGE_ATTR = "image";
    public static final String RESPONSE_OK_STATUS = "HTTP/1.1 200 OK";

    public static final String SERVER_CLIP_URL = "http://112.78.4.97/fpt/mola/clip.php";
    public static final String CLIP_ATTR = "clip";

    public static boolean UploadImage(InputStream inputFile, String filename) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httpPost = new HttpPost(SERVER_URL);
        MultipartEntity entity = new MultipartEntity();
        ContentBody body = new InputStreamBody(inputFile, filename);
        entity.addPart(IMAGE_ATTR, body);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        return (response.getStatusLine().toString()).equals(RESPONSE_OK_STATUS);
    }

    public static boolean UploadClip(InputStream inputFile, String filename) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httpPost = new HttpPost(SERVER_CLIP_URL);
        MultipartEntity entity = new MultipartEntity();
        ContentBody body = new InputStreamBody(inputFile, filename);
        entity.addPart(CLIP_ATTR, body);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        return (response.getStatusLine().toString()).equals(RESPONSE_OK_STATUS);
    }


}