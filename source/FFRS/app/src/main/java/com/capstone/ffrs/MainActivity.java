package com.capstone.ffrs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by HuanPMSE61860 on 10/15/2017.
 */

public class MainActivity extends Activity {

    private String hostURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                } else {
                    getLoginInfoFromSession();
                }
            }
        }, 1000);
    }

    public void getLoginInfoFromSession() {
        hostURL = HostURLUtils.getInstance(this).getHostURL();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            requestLogin(sharedPreferences.getString("username", null), sharedPreferences.getString("password", null));
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    public void requestLogin(String username, final String password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = hostURL + getResources().getString(R.string.url_login);
        url = String.format(url, username, password);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject body = response.getJSONObject("body");
                            if (body != null && body.length() > 0) {
                                JSONObject role = body.getJSONObject("roleId");
                                String roleName = role.getString("roleName");
                                if (roleName.equals("user")) {
                                    changeActivity(body);
                                } else {
                                    clearSharedPreferences();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                }
                            } else {
                                clearSharedPreferences();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && (error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 400)) {
                            try {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
                                    String username = sharedPreferences.getString("username", null);
                                    String password = sharedPreferences.getString("password", null);
                                    String strResponse = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject response = new JSONObject(strResponse);
                                    if (response.getString("message").equals("Account have username: " + username + " is locked!")) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Tài khoản đã bị khóa!").
                                                setMessage("Tài khoản của bạn đã bị khóa bởi quản trị viên.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        clearSharedPreferences();
                                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        overridePendingTransition(0, 0);
                                                    }
                                                }).create();
                                        alertDialog.setCancelable(false);
                                        alertDialog.show();
                                    } else if (response.getString("message").equals("Not found account have username: " + username + " and password: " + password)) {
                                        Toast.makeText(MainActivity.this, "Sai tên tài khoản hay mật khẩu", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            clearSharedPreferences();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        } else {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "Không thể kết nối với máy chủ! Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        }
                    }
                });
        queue.add(getRequest);
    }

    public void changeActivity(JSONObject body) {
        Intent intent = new Intent(this, SearchActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            if (!sharedPreferences.contains("user_id") || !sharedPreferences.contains("username") || !sharedPreferences.contains("password")) {
                editor.putInt("user_id", body.getInt("id"));
                editor.putString("username", body.getString("username"));
                editor.putString("password", body.getString("password"));
            }
            editor.putString("avatarURL", body.getJSONObject("profileId").getString("avatarUrl"));
            editor.putString("teamName", body.getJSONObject("profileId").getString("name"));
            editor.putInt("balance", body.getJSONObject("profileId").getInt("balance"));
            editor.putInt("points", body.getJSONObject("profileId").getInt("bonusPoint"));
            editor.apply();
        } catch (JSONException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown

                AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Yêu cầu quyền vị trí").
                        setMessage("Ứng dụng cần quyền sử dụng vị trí của thiết bị để có thể hoạt động đầy đủ. Vui lòng bật quyền để tiếp tục sử dụng ứng dụng.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create();
                alertDialog.setCancelable(false);
                alertDialog.show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            getLoginInfoFromSession();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getLoginInfoFromSession();
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Ứng dụng phải có quyền sử dụng vị trí của bạn để thực hiện các chức năng chính!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    public void clearSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String hostURL = HostURLUtils.getInstance(this).getHostURL();
        editor.clear();
        editor.putString("host_url", hostURL);
        editor.apply();
    }
}
