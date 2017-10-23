package com.capstone.ffrs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.adapter.MatchAdapter;
import com.capstone.ffrs.adapter.NotificationAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.DataSnapshotEntrySet;
import com.capstone.ffrs.entity.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener listener;
    private List<Notification> notificationList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String localhost, url;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        localhost = getResources().getString(R.string.local_host);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NotificationAdapter(this, notificationList);
        adapter.setUserId(sharedPreferences.getInt("user_id", -1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                notificationList.clear();
                for (DataSnapshot snapshot : iterable) {
                    queue = NetworkController.getInstance(NotificationActivity.this).getRequestQueue();
                    url = localhost + "/swp49x-ffrs/match/matching-request?matching-request-id=" + snapshot.getKey();
                    JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null) {
                                    int userId = body.getJSONObject("userId").getInt("id");
                                    if (userId == sharedPreferences.getInt("user_id", -1)) {
                                        try {
                                            Notification notification = new Notification();
                                            notification.setRequestId(body.getInt("id"));
                                            notification.setUserId(userId);

                                            notification.setDate(new Date(body.getLong("date")));
                                            notification.setStartTime(new SimpleDateFormat("HH:mm:ss").parse(body.getString("startTime")));
                                            notification.setEndTime(new SimpleDateFormat("HH:mm:ss").parse(body.getString("endTime")));

                                            notification.setTeamName(body.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                                            notificationList.add(notification);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        } finally {
                                            //Notify adapter about data changes
                                            adapter.notifyItemChanged(notificationList.size());
                                        }
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
                    queue.add(newsReq);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        myRef.child("request").addValueEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.child("request").removeEventListener(listener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
