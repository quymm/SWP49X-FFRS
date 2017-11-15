package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RatingOpponentActivity extends AppCompatActivity {
    private String hostURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_opponent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = getResources().getString(R.string.local_host);
    }

    public void onClickSendRating(View view) {
        Bundle b = getIntent().getExtras();
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        Switch swBlackList = (Switch) findViewById(R.id.switch_black_list);
        if (swBlackList.isChecked()) {
            url = hostURL + getResources().getString(R.string.url_add_black_list);
            url = String.format(url, b.getInt("user_id"), b.getInt("opponent_id"));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(RatingOpponentActivity.this, "Bạn đã thêm đối thủ vào danh sách chặn", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(request);
        }

        url = hostURL + getResources().getString(R.string.url_send_rating_opponent);

        RadioGroup rdSkillGroup = (RadioGroup) findViewById(R.id.rdSkill);
        RadioGroup rdWinnerGroup = (RadioGroup) findViewById(R.id.rdWinner);

        int radioBtnId = rdSkillGroup.getCheckedRadioButtonId();
        int ratingScore = 2;
        switch (radioBtnId) {
            case R.id.rbtGood:
                ratingScore = 3;
                break;
            case R.id.rbtNormal:
                ratingScore = 2;
                break;
            case R.id.rbtNotGood:
                ratingScore = 1;
                break;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ratingScore", ratingScore);
        params.put("tourMatchId", b.getInt("tour_match_id"));
        params.put("userId", b.getInt("user_id"));

        if (rdWinnerGroup.getCheckedRadioButtonId() == R.id.rbtYourTeam) {
            params.put("win", true);
        } else {
            params.put("win", false);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(RatingOpponentActivity.this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(RatingOpponentActivity.this, "Cảm ơn bạn đã tham gia đánh giá đối thủ.", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
