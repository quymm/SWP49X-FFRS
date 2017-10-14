package com.capstone.ffrs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {

    String localhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        localhost = getResources().getString(R.string.local_host);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public void onClickLogin(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        EditText username = (EditText) findViewById(R.id.text_username);
        EditText password = (EditText) findViewById(R.id.text_password);
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("Hãy nhập tên tài khoản");
            username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Hãy nhập mật khẩu");
            password.requestFocus();
            return;
        }
        String url = localhost + "/swp49x-ffrs/account/login-user?username=" + username.getText().toString() + "&password=" + password.getText().toString();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            changeActivity(response);
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai tên tài khoản hay mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
        };
        queue.add(getRequest);
    }

    public void changeActivity(JSONObject response) {
        Intent intent = new Intent(this, FieldSuggestActivity.class);
        try {
            intent.putExtra("user_id", response.getInt("id"));
        } catch (JSONException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
