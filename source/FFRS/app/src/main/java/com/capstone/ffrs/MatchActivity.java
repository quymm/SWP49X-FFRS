package com.capstone.ffrs;

import android.content.Context;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.capstone.ffrs.adapter.MatchAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FirebaseUserInfo;
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
import java.util.Hashtable;
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
            url = localhost + "/swp49x-ffrs/match/suggest-opponent";
            url += "?user-id=" + b.getInt("user_id");
            url += "&field-type-id=" + b.getInt("field_type_id");
            url += "&longitude=" + b.getDouble("longitude");
            url += "&latitude=" + b.getDouble("latitude");
            url += "&deviation-time=120&deviation-distance=5";
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
            params.put("address", "ssss");
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

            // Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            Integer userId = b.getInt("user_id");

            FirebaseUserInfo info = new FirebaseUserInfo();
            info.setStatus(0);
            info.setLatitude(b.getDouble("latitude"));
            info.setLongitude(b.getDouble("longitude"));
            for (MatchRequest opponent : opponentList) {
                myRef.child("request").child(opponent.getUserId() + "").child(opponent.getId() + "").child(userId.toString()).setValue(info);
//                myRef.child("response").child(userId.toString()).child(opponent.getId() + "").setValue(0);
            }

//            sendPushToSingleInstance(this, null);
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    String value = dataSnapshot.getValue(String.class);
//                    Toast.makeText(getApplicationContext(), "Value is: " + value, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w("TAG", "Failed to read value.", error.toException());
//                }
//            });

        } catch (ParseException e) {
            Log.d("LOG", e.getMessage());
        }
    }

//    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue /*your data from the activity*/) {
//
//
//        final String url = "https://fcm.googleapis.com/fcm/send";
//        StringRequest myReq = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//
//            ;
//
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "key=AIzaSyAzwlX8JKSRYcTiqOapjvif-k6yKYDulkY");
//                return headers;
//            }
//
//        };
//
//        Volley.newRequestQueue(activity).add(myReq);
//    }
}
