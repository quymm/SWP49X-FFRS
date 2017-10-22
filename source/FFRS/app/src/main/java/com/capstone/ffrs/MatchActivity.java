package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.adapter.MatchAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.MatchRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class MatchActivity extends AppCompatActivity {
    String url;

    RecyclerView recyclerView;
    RequestQueue queue;
    List<MatchRequest> opponentList = new ArrayList<MatchRequest>();
    MatchAdapter adapter;
    String localhost;

    private String displayDateFormat = "dd/MM/yyyy";
    private String serverDateFormat = "dd-MM-yyyy";
    private String timeFormat = "H:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        localhost = getResources().getString(R.string.local_host);
        loadMatches();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void loadMatches() {
        Bundle b = getIntent().getExtras();
        SimpleDateFormat sdf = new SimpleDateFormat(displayDateFormat);
        String strDate;
        try {
            Date date = sdf.parse(b.getString("field_date"));
            sdf = new SimpleDateFormat(serverDateFormat);
            strDate = sdf.format(date);
            url = localhost + "/swp49x-ffrs/match/matching-request";
            url += "?user-id=" + b.getInt("user_id");
            url += "&field-type-id=" + b.getInt("field_type_id");
            url += "&longitude=" + b.getDouble("longitude");
            url += "&latitude=" + b.getDouble("latitude");
            url += "&date=" + strDate;
            url += "&start-time=" + b.getString("field_start_time");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MatchAdapter(this, opponentList);
        adapter.setUserId(b.getInt("user_id"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray body = response.getJSONArray("body");
                    for (int i = 0; i < body.length(); i++) {
                        try {
                            JSONObject obj = body.getJSONObject(i);
                            MatchRequest match = new MatchRequest();
                            match.setId(obj.getInt("id"));
                            match.setTeamName(obj.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                            match.setUserId(obj.getJSONObject("userId").getInt("id"));
                            match.setDate(obj.getString("date"));
                            match.setStartTime(obj.getString("startTime"));
                            match.setEndTime(obj.getString("endTime"));
                            match.setRatingScore(obj.getJSONObject("userId").getJSONObject("profileId").getInt("ratingScore"));
                            // adding movie to movies array
                            opponentList.add(match);

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

        };
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    public void onClickCreate(View view) {
        Bundle b = getIntent().getExtras();
        url = localhost + "/swp49x-ffrs/match/matching-request";

        queue = NetworkController.getInstance(this).getRequestQueue();

        SimpleDateFormat sdf = new SimpleDateFormat(displayDateFormat);
        try {
            Date date = sdf.parse(b.getString("field_date"));
            sdf = new SimpleDateFormat(serverDateFormat);
            String strDate = sdf.format(date);

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("date", strDate);
            params.put("userId", b.getInt("user_id"));
            params.put("startTime", b.getString("field_start_time"));
            params.put("endTime", b.getString("field_end_time"));
            params.put("fieldTypeId", b.getInt("field_type_id"));
            params.put("latitude", b.getDouble("latitude"));
            params.put("longitude", b.getDouble("longitude"));
            JsonObjectRequest createRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Bundle b = getIntent().getExtras();
                            Intent intent = new Intent(MatchActivity.this, CreateMatchingRequestActivity.class);
                            intent.putExtra("user_id", b.getInt("user_id"));
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.getMessage());
                }
            });
            queue.add(createRequest);
        } catch (ParseException e) {
            Log.d("Parse_Exception", e.getMessage());
        }
    }

    public void onClickSendRequest(View view) {
        final Bundle b = getIntent().getExtras();
        SimpleDateFormat sdf = new SimpleDateFormat(displayDateFormat);

        try {
            final Date date = sdf.parse(b.getString("field_date"));

            sdf = new SimpleDateFormat(serverDateFormat);
            final String strDate = sdf.format(date);

            sdf = new SimpleDateFormat(timeFormat);

            String url = localhost + "/swp49x-ffrs/match/choose-field?matching-request-id=" + opponentList.get(0).getId();

            queue = NetworkController.getInstance(this).getRequestQueue();
            Map<String, Object> params = new HashMap<>();
            params.put("date", strDate);
            params.put("userId", b.getInt("user_id"));
            params.put("startTime", b.getString("field_start_time"));
            params.put("endTime", b.getString("field_end_time"));
            params.put("fieldTypeId", b.getInt("field_type_id"));
            params.put("latitude", b.getDouble("latitude"));
            params.put("longitude", b.getDouble("longitude"));
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null && body.length() > 0) {
                                    try {
                                        final Date fromTime = new Date(body.getLong("startTime"));
                                        final Date toTime = new Date(body.getLong("endTime"));
                                        Intent intent = new Intent(MatchActivity.this, FieldDetailActivity.class);
                                        intent.putExtra("field_id", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getInt("id"));
                                        intent.putExtra("field_name", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                        intent.putExtra("field_address", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                        intent.putExtra("field_type_id", body.getJSONObject("fieldTypeId").getInt("id"));
                                        intent.putExtra("image_url", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("avatarUrl"));
                                        intent.putExtra("date", date);
                                        intent.putExtra("time_from", fromTime);
                                        intent.putExtra("time_to", toTime);
                                        intent.putExtra("price", (body.getInt("price") / 2));
                                        intent.putExtra("user_id", b.getInt("user_id"));
                                        intent.putExtra("time_slot_id", body.getInt("id"));
                                        intent.putExtra("tour_match_mode", true);
                                        intent.putExtra("opponent_id", opponentList.get(0).getUserId());
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    }
                                } else {
                                    Toast.makeText(MatchActivity.this, "Không thể đặt sân!", Toast.LENGTH_SHORT).show();
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
        } catch (ParseException e) {
            Log.d("LOG", e.getMessage());
        }
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();
//
//        Bundle b = getIntent().getExtras();
//        List<Integer> matchingRequestIdList = new ArrayList<>();
//        for (MatchRequest opponent: opponentList){
//            matchingRequestIdList.add(opponent.getId());
//        }
//        myRef.child("tourMatch").child(b.getInt("user_id")+"").setValue(matchingRequestIdList);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(getApplicationContext(), "Value is: " + value, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });


    }
}
