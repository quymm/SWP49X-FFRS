package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;

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

        hostURL = HostURLUtils.getInstance(this).getHostURL();
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
                ratingScore = 4;
                break;
            case R.id.rbtNormal:
                ratingScore = 2;
                break;
            case R.id.rbtNotGood:
                ratingScore = 0;
                break;
        }
        radioBtnId = rdWinnerGroup.getCheckedRadioButtonId();
        int winPoints = 3;
        switch (radioBtnId) {
            case R.id.rbtYourTeam:
                winPoints = 3;
                break;
            case R.id.rbtDraw:
                winPoints = 1;
                break;
            case R.id.rbtOpponent:
                winPoints = 0;
                break;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("ratingLevel", ratingScore);
        params.put("tourMatchId", b.getInt("tour_match_id"));
        params.put("userId", b.getInt("user_id"));
        params.put("result", winPoints);
        EditText txtGD = (EditText) findViewById(R.id.text_goal_difference);
        if (txtGD.getText() != null && !txtGD.getText().toString().isEmpty()) {
            params.put("goalsDifference", Integer.valueOf(txtGD.getText().toString()));
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
