package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtUsername, txtPassword, txtConfirm, txtTeamName, txtPhone;
    private String hostURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUsername = (EditText) findViewById(R.id.input_username);
        txtPassword = (EditText) findViewById(R.id.input_password);
        txtConfirm = (EditText) findViewById(R.id.input_confirm_password);
        txtTeamName = (EditText) findViewById(R.id.input_team_name);
        txtPhone = (EditText) findViewById(R.id.input_phone);

        hostURL = HostURLUtils.getInstance(this).getHostURL();
    }

    public void onClickRegister(View view) {
        if (validateInput()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = hostURL + getResources().getString(R.string.url_register_user);

            Map<String, Object> params = new HashMap<>();
            params.put("avatarUrl", "string");
            params.put("password", txtPassword.getText().toString());
            params.put("phone", txtPhone.getText().toString());
            params.put("teamName", txtTeamName.getText().toString());
            params.put("username", txtUsername.getText().toString());
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null && body.length() > 0) {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    txtUsername.setError("Tên tài khoản đã có người dùng");
                                }
                            } catch (JSONException e) {
                                Log.d("ParseException", e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                try {
                                    String strResponse = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject response = new JSONObject(strResponse);
                                    if (response.getString("message").equals("Username: " + txtUsername.getText().toString() + " is already exists!")) {
                                        txtUsername.setError("Tên tài khoản đã có người dùng");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            queue.add(getRequest);
        }
    }

    public boolean validateInput() {
        boolean validated = true;
        if (TextUtils.isEmpty(txtUsername.getText().toString())) {
            validated = false;
            txtUsername.setError("Hãy nhập tên tài khoản");
            txtUsername.requestFocus();
        }
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            validated = false;
            txtPassword.setError("Hãy nhập mật khẩu");
            txtPassword.requestFocus();
        }
        if (TextUtils.isEmpty(txtConfirm.getText().toString())) {
            validated = false;
            txtConfirm.setError("Hãy nhập lại mật khẩu");
            txtConfirm.requestFocus();
        } else if (!TextUtils.isEmpty(txtPassword.getText().toString())) {
            if (!txtPassword.getText().toString().equals(txtConfirm.getText().toString())) {
                validated = false;
                txtConfirm.setError("Mật khẩu không khớp");
                txtConfirm.requestFocus();
            }
        }
        if (TextUtils.isEmpty(txtTeamName.getText().toString())) {
            validated = false;
            txtTeamName.setError("Hãy nhập tên đội");
            txtTeamName.requestFocus();
        }
        if (TextUtils.isEmpty(txtPhone.getText().toString())) {
            validated = false;
            txtPhone.setError("Hãy nhập số điện thoại");
            txtPhone.requestFocus();
        }
        return validated;
    }
}
