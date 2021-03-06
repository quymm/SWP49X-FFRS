package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwner;
import com.capstone.ffrs.utils.HostURLUtils;
import com.capstone.ffrs.utils.TimePickerListener;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

        hostURL = HostURLUtils.getInstance(this).getHostURL();

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
        final TextView txtEndTime = (TextView) findViewById(R.id.input_end_time);
        LocalTime localStartTime = LocalTime.parse(b.getString("field_start_time"), dtf);
        LocalTime localEndTime = localStartTime.plusMinutes(duration);
        dtf = DateTimeFormat.forPattern("H:mm");
        txtStartTime.setText(localStartTime.toString(dtf));
        txtEndTime.setText(localEndTime.toString(dtf));

        TextView txtDate = (TextView) findViewById(R.id.date_view);
        txtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.parseLong(b.getString("date")))));

        TimePickerListener startListener = new TimePickerListener(this, txtStartTime);
        try {
            dtf = DateTimeFormat.forPattern("H:mm:ss");
            LocalTime localMaxStartTime = LocalTime.parse(b.getString("field_end_time"), dtf).minusMinutes(duration);
            dtf = DateTimeFormat.forPattern("H:mm");
            Date minStartTime = new SimpleDateFormat("H:mm:ss").parse(b.getString("field_start_time"));
            Date maxStartTime = new SimpleDateFormat("H:mm").parse(localMaxStartTime.toString(dtf));
            if (maxStartTime.getTime() - minStartTime.getTime() == 0) {
                chooseField();
            } else {
                startListener.setDate(new Date(Long.parseLong(b.getString("date"))));
                startListener.setMinTime(minStartTime);
                startListener.setMaxTime(maxStartTime);
            }
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
        chooseField();
    }

    public void chooseField() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int userId = sharedPreferences.getInt("user_id", -1);
        final Bundle b = getIntent().getExtras();

        final EditText txtStartTime = (EditText) findViewById(R.id.input_start_time);
        final TextView txtEndTime = (TextView) findViewById(R.id.input_end_time);

        final int opponentId = b.getInt("opponent_id");

        String url = hostURL + getResources().getString(R.string.url_choose_field);
        url = String.format(url, b.getInt("matching_request_id"));

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();

        final String strDate = b.getString("date");

        Map<String, Object> params = new HashMap<>();
        params.put("expectedDistance", b.getInt("distance"));
        params.put("address", b.getString("address"));
        params.put("duration", b.getInt("duration"));
        params.put("date", strDate);
        params.put("userId", userId);
        params.put("startTime", txtStartTime.getText().toString());
        params.put("endTime", txtEndTime.getText().toString());
        params.put("fieldTypeId", b.getInt("field_type_id"));
        params.put("latitude", b.getDouble("latitude"));
        params.put("longitude", b.getDouble("longitude"));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("body")) {
                                JSONArray body = response.getJSONArray("body");
                                if (body != null && body.length() > 0) {
                                    try {
                                        JSONObject firstItem = body.getJSONObject(0);
                                        ArrayList<FieldOwner> suitableList = new ArrayList<>();
                                        for (int i = 0; i < body.length(); i++) {
                                            JSONObject item = body.getJSONObject(i);
                                            FieldOwner field = new FieldOwner();
                                            field.setId(item.getJSONObject("fieldOwnerId").getInt("id"));
                                            field.setFieldName(item.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                            field.setAddress(item.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                            field.setPrice(item.getInt("price"));
                                            suitableList.add(field);
                                        }
                                        for (int i = 0; i < suitableList.size(); i++) {
                                            Log.d("FIELD_LIST", suitableList.get(i).toString());
                                        }
                                        final Date fromTime = new SimpleDateFormat("H:mm").parse(txtStartTime.getText().toString());
                                        final Date toTime = new SimpleDateFormat("H:mm").parse(txtEndTime.getText().toString());
                                        Intent intent = new Intent(RequestTimeActivity.this, FieldDetailActivity.class);
                                        intent.putExtra("field_id", firstItem.getJSONObject("fieldOwnerId").getInt("id"));
                                        intent.putExtra("field_name", firstItem.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                        intent.putExtra("field_address", firstItem.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                        intent.putExtra("field_type_id", firstItem.getJSONObject("fieldTypeId").getInt("id"));
                                        intent.putExtra("date", new Date(Long.parseLong(strDate)));
                                        intent.putExtra("time_from", fromTime);
                                        intent.putExtra("time_to", toTime);
                                        intent.putExtra("price", firstItem.getInt("price"));
                                        intent.putExtra("user_id", userId);
                                        intent.putExtra("tour_match_mode", true);
                                        intent.putParcelableArrayListExtra("field_list", suitableList);
                                        intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                                        intent.putExtra("created_matching_request_id", b.getInt("created_matching_request_id"));
                                        intent.putExtra("opponent_id", opponentId);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    }
                                } else {
                                    Toast toast = Toast.makeText(RequestTimeActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                }

                            } else {
                                Toast toast = Toast.makeText(RequestTimeActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_LONG);
                                toast.show();
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.d("ParseException", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && (error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404)) {
                            try {
                                String strResponse = new String(error.networkResponse.data, "UTF-8");
                                JSONObject response = new JSONObject(strResponse);
                                if (response.getString("message").equals("Field owner has not created any fields!")) {
                                    Toast toast = Toast.makeText(RequestTimeActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
                        }
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