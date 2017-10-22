package com.utils;

import com.dto.CordinationPoint;
import org.apache.commons.io.IOUtils;
import org.apache.http.ConnectionClosedException;
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
    private static final String DIRECTION_KEY = "AIzaSyD0PY8Ok2pfzMNhcf3JS4LxYmBSR_xVm-A";

    private static final String GEOCODING_KEY = "AIzaSyCqjl1MC_1bqUVYuPpdk8tvpNnlJgwijuk";

    private static final String DISTANCE_BASED_MODE_OF_TRANSPORT = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&mode=%s&language=vi&key=%s";

    private static final String GET_LONGITUDE_LATITUDE_FROM_ADDRESS = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";

    private static final String GET_ADDRESS_FROM_LONGITUDE_LATITUDE = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s";

    public static void main(String[] args) throws IOException {
        String origins = "Dương Quảng Hàm, phường 5,Gò Vấp,Hồ Chí Minh,Vietnam";

        String destination = "Tô Ký, Quận 12, Hồ Chí Minh";

        CordinationPoint cordinationPoint = new CordinationPoint(106.6884028, 10.8298734);
        System.out.println(getAddressFromCordinationPoint(cordinationPoint));
    }

    public static int calculateDistanceBetweenTwoPointWithAddress(String origins, String destination, String transportMode) {
        String url = String.format(DISTANCE_BASED_MODE_OF_TRANSPORT, removeSpace(origins), removeSpace(destination), transportMode, DIRECTION_KEY);
        JSONObject jsonObject = connectGoogleAPI(url);

        JSONArray rows = jsonObject.getJSONArray("rows");
        JSONObject row = (JSONObject) rows.get(0);
        JSONArray elements = row.getJSONArray("elements");
        JSONObject element = (JSONObject) elements.get(0);
        JSONObject distance = element.getJSONObject("distance");
        int value = distance.getInt("value");
        return value;
    }

    public static Double calculateDistanceBetweenTwoPoint(CordinationPoint a, CordinationPoint b) {
        double num1 = (a.getLongitude() - b.getLongitude()) * 111.32;
        double num2 = (a.getLatitude() - b.getLatitude()) * 110.57;
        return Math.sqrt(num1 * num1 + num2 * num2);
    }

    public static String removeSpace(String inputString) {
        String[] terms = inputString.trim().split(" ");
        String output = "";
        if (terms.length != 0)
            for (String term : terms) {
                output += term.trim();
            }
        return output;
    }

    public static CordinationPoint getLongitudeAndLatitudeFromAddress(String address){
        String url = String.format(GET_LONGITUDE_LATITUDE_FROM_ADDRESS, removeSpace(address), GEOCODING_KEY);
        JSONObject jsonObject = connectGoogleAPI(url);

        JSONArray results = jsonObject.getJSONArray("results");

        JSONObject result = (JSONObject) results.get(0);
        JSONObject geometry = result.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        double latitude = location.getDouble("lat");
        double longitude = location.getDouble("lng");
        return new CordinationPoint(longitude, latitude);
    }

    public static String getAddressFromCordinationPoint(CordinationPoint cordinationPoint){
        String url = String.format(GET_ADDRESS_FROM_LONGITUDE_LATITUDE, cordinationPoint.getLatitude(), cordinationPoint.getLongitude(), GEOCODING_KEY);
        JSONObject jsonObject = connectGoogleAPI(url);

        JSONArray results = jsonObject.getJSONArray("results");

        JSONObject result = (JSONObject) results.get(0);
        String address = result.getString("formatted_address");
        return address;
    }

    public static String formatAddress(String address){
        String url = String.format(GET_LONGITUDE_LATITUDE_FROM_ADDRESS, removeSpace(address), GEOCODING_KEY);
        JSONObject jsonObject = connectGoogleAPI(url);

        JSONArray results = jsonObject.getJSONArray("results");

        JSONObject result = (JSONObject) results.get(0);
        String formattedAddress = result.getString("formatted_address");
        return formattedAddress;
    }

    private static JSONObject connectGoogleAPI(String url){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        String json = "";
        try {
            HttpResponse response = client.execute(request);
            json = IOUtils.toString(response.getEntity().getContent());
        } catch (IOException e) {
            throw new IllegalArgumentException("Have error when connect with connect Google API");
        }
        return new JSONObject(json);
    }


}
