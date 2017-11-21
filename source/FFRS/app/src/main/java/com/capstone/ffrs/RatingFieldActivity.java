package com.capstone.ffrs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RatingFieldActivity extends AppCompatActivity {
    private String hostURL, url;
    private Integer favoriteId = null;
    private Button btSendRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        btSendRating = (Button) findViewById(R.id.btRequest);
        btSendRating.setEnabled(false);
        checkFavoriteField();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickSendRating(View view) {
        Bundle b = getIntent().getExtras();
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        Switch swFavoriteField = (Switch) findViewById(R.id.switch_favorite_field);
        if (swFavoriteField.isChecked()) {
            if (favoriteId == null) {
                url = hostURL + getResources().getString(R.string.url_add_favorite_field);
                url = String.format(url, b.getInt("user_id"), b.getInt("field_id"));
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RatingFieldActivity.this, "Bạn đã thêm sân vào danh sách yêu thích", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue.add(request);
            }
        } else {
            if (favoriteId != null) {
                url = hostURL + getResources().getString(R.string.url_remove_favorite_field);
                url = String.format(url, favoriteId);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RatingFieldActivity.this, "Bạn đã xóa sân ra khỏi danh sách yêu thích", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue.add(request);
            }
        }

        url = hostURL + getResources().getString(R.string.url_send_rating_field);

        TextView txtComment = (TextView) findViewById(R.id.txtComment);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        Map<String, Object> params = new HashMap<>();
        params.put("comment", txtComment.getText().toString());
        params.put("fieldOwnerId", b.getInt("field_id"));
        params.put("ratingScore", ratingBar.getRating());
        params.put("userId", b.getInt("user_id"));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Intent intent = new Intent(RatingFieldActivity.this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(RatingFieldActivity.this, "Cảm ơn bạn đã tham gia đánh giá.", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void checkFavoriteField() {
        final Bundle b = getIntent().getExtras();
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        final Switch swFavoriteField = (Switch) findViewById(R.id.switch_favorite_field);
        url = hostURL + getResources().getString(R.string.url_get_favorite_fields);
        url = String.format(url, b.getInt("user_id"));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        for (int i = 0; i < body.length(); i++) {
                            JSONObject item = body.getJSONObject(i);
                            int fieldId = item.getJSONObject("fieldOwnerId").getInt("id");
                            if (fieldId == b.getInt("field_id")) {
                                favoriteId = b.getInt("id");
                                swFavoriteField.setChecked(true);
                                btSendRating.setEnabled(true);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    btSendRating.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
