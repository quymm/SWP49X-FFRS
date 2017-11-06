package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.MatchRequest;
import com.capstone.ffrs.entity.NotificationRequest;
import com.capstone.ffrs.entity.NotificationResponse;
import com.capstone.ffrs.utils.TimePickerListener;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestTimeActivity extends AppCompatActivity {

    private String hostURL;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = getResources().getString(R.string.local_host);

        Bundle b = getIntent().getExtras();
        duration = b.getInt("duration");
        String strDuration = "";
        if (duration / 60 > 0) {
            strDuration += (duration / 60) + " tiếng";
        }
        if (duration % 60 == 30) {
            strDuration += " " + (duration % 60) + " phút";
        }
        TextView txtDuration = (TextView) findViewById(R.id.duration_view);
        txtDuration.setText(strDuration);

        // Button
        DateTimeFormatter dtf = DateTimeFormat.forPattern("H:mm:ss");
        final EditText txtStartTime = (EditText) findViewById(R.id.input_start_time);
        final EditText txtEndTime = (EditText) findViewById(R.id.input_end_time);
        LocalTime localStartTime = LocalTime.parse(b.getString("field_start_time"), dtf);
        LocalTime localEndTime = localStartTime.plusMinutes(duration);
        dtf = DateTimeFormat.forPattern("H:mm");
        txtStartTime.setText(localStartTime.toString(dtf));
        txtEndTime.setText(localEndTime.toString(dtf));

        TimePickerListener startListener = new TimePickerListener(this, txtStartTime);
        try {
            dtf = DateTimeFormat.forPattern("H:mm:ss");
            LocalTime localMaxStartTime = LocalTime.parse(b.getString("field_end_time"), dtf).minusMinutes(duration);
            dtf = DateTimeFormat.forPattern("H:mm");
            Date maxStartTime = new SimpleDateFormat("H:mm").parse(localMaxStartTime.toString(dtf));
            startListener.setMinTime(new SimpleDateFormat("H:mm:ss").parse(b.getString("field_start_time")));
            startListener.setMaxTime(maxStartTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtStartTime.setOnClickListener(startListener);
        txtStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    txtEndTime.setText(sdf.format(new LocalDateTime(sdf.parse(txtStartTime.getText().toString())).plusMinutes(duration).toDate()));
                } catch (ParseException e) {

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void onClickChooseField(View v) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int userId = sharedPreferences.getInt("user_id", -1);
        final Bundle b = getIntent().getExtras();

        final int opponentId = b.getInt("opponent_id");

        String url = hostURL + getResources().getString(R.string.url_choose_field);
        url = String.format(url, b.getInt("matching_request_id"), 5);

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();

        final String strDate = b.getString("date");

        Map<String, Object> params = new HashMap<>();
        params.put("date", strDate);
        params.put("userId", userId);
        params.put("startTime", b.getString("field_start_time"));
        params.put("endTime", b.getString("field_end_time"));
        params.put("fieldTypeId", b.getInt("field_type_id"));
        params.put("latitude", b.getDouble("latitude"));
        params.put("longitude", b.getDouble("longitude"));
        params.put("duration", b.getInt("duration"));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("body")) {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null && body.length() > 0) {
                                    try {
                                        final Date fromTime = new Date(body.getLong("startTime"));
                                        final Date toTime = new Date(body.getLong("endTime"));
                                        Intent intent = new Intent(RequestTimeActivity.this, FieldDetailActivity.class);
                                        intent.putExtra("field_id", body.getJSONObject("fieldOwnerId").getInt("id"));
                                        intent.putExtra("field_name", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                        intent.putExtra("field_address", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                        intent.putExtra("field_type_id", body.getJSONObject("fieldTypeId").getInt("id"));
                                        intent.putExtra("image_url", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("avatarUrl"));
                                        intent.putExtra("date", new Date(Long.parseLong(strDate)));
                                        intent.putExtra("time_from", fromTime);
                                        intent.putExtra("time_to", toTime);
                                        intent.putExtra("price", body.getInt("price"));
                                        intent.putExtra("user_id", userId);
                                        intent.putExtra("time_slot_id", body.getInt("id"));
                                        intent.putExtra("tour_match_mode", true);
                                        intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                                        intent.putExtra("opponent_id", opponentId);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    }
                                } else {
                                    Toast.makeText(RequestTimeActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RequestTimeActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("ParseException", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(postRequest);
    }
}