package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {

    String hostURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        hostURL = getResources().getString(R.string.local_host);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public void onClickLogin(View view) {
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
        requestLogin(username.getText().toString(), password.getText().toString());
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void requestLogin(String username, String password) {
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
                                    EditText password = (EditText) findViewById(R.id.text_password);
                                    password.setText("");
                                    Toast.makeText(LoginActivity.this, "Sai tên tài khoản hay mật khẩu!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                EditText password = (EditText) findViewById(R.id.text_password);
                                password.setText("");
                                Toast.makeText(LoginActivity.this, "Sai tên tài khoản hay mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("ParseException", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        EditText password = (EditText) findViewById(R.id.text_password);
                        password.setText("");
                        Toast.makeText(LoginActivity.this, "Sai tên tài khoản hay mật khẩu!", Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", error.toString());
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
            editor.putString("teamName", body.getJSONObject("profileId").getString("name"));
            editor.putInt("balance", body.getJSONObject("profileId").getInt("balance"));
            editor.putInt("points", body.getJSONObject("profileId").getInt("bonusPoint"));
            editor.commit();

        } catch (JSONException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
