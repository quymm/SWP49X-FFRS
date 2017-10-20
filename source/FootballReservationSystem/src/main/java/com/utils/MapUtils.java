package com.utils;

import com.dto.CordinationPoint;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author MinhQuy
 */
public class MapUtils {
    private static final String YOUR_API_KEY = "AIzaSyAkLIvP0GetPFIM8RlSbrVmZGfpfwYpnyo";

    private static final String DISTANCE_BASED_MODE_OF_TRANSPORT = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&mode=%s&language=vi&key=%s";

    public static void main(String[] args) throws IOException {
        String origins = "Dương%20Quảng%20Hàm,%20phường%205,%20Gò%20Vấp,%20Hồ%20Chí%20Minh,%20Vietnam";

        String destination = "Tô%20Ký,%20Tân%20Chánh%20Hiệp,%20Hồ%20Chí%20Minh,%20Vietnam";

        String transport_mode = "driving";

        String url = String.format(DISTANCE_BASED_MODE_OF_TRANSPORT, origins, destination, transport_mode, YOUR_API_KEY);

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("accept", "application/json");
        HttpResponse response = client.execute(request);

        String json = IOUtils.toString(response.getEntity().getContent());
        JSONObject jsonObject = new JSONObject(json);
        System.out.println(jsonObject.toString());
        JSONArray destinationJSON = jsonObject.getJSONArray("destination_addresses");
        System.out.println(destinationJSON.getString(0));



    }

    public static Double calculateDistanceBetweenTwoPoint(CordinationPoint a, CordinationPoint b){
        double num1 = (a.getLongitude() - b.getLongitude())*111.32;
        double num2 = (a.getLatitude() - b.getLatitude())*110.57;
        return Math.sqrt(num1*num1 + num2*num2);
    }





}
