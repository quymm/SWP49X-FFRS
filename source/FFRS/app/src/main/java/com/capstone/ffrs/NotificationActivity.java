package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.capstone.ffrs.adapter.NotificationAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FirebaseUserInfo;
import com.capstone.ffrs.entity.NotificationRequest;
import com.capstone.ffrs.entity.NotificationResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener requestListener, responseListener;
    private List<Object> notificationRequestList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String hostURL;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private NotificationAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int userId = sharedPreferences.getInt("user_id", -1);

        hostURL = getResources().getString(R.string.local_host);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NotificationAdapter(this, notificationRequestList);
        adapter.setUserId(userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                notificationRequestList.clear();
                for (final DataSnapshot snapshot : iterable) {
                    queue = NetworkController.getInstance(NotificationActivity.this).getRequestQueue();
                    String url = hostURL + getResources().getString(R.string.url_get_matching_request_by_id);
                    url = String.format(url, snapshot.getKey());
                    JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!response.isNull("body")) {
                                    JSONObject body = response.getJSONObject("body");
                                    if (body != null) {
                                        Iterable<DataSnapshot> list = snapshot.getChildren();
                                        for (DataSnapshot snapshot : list) {
                                            getTeamNameByUserId(snapshot, body);
                                        }
                                    } else {
                                        Toast.makeText(NotificationActivity.this, "Không còn sân trống", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(NotificationActivity.this, "Không còn sân trống", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.statusCode == 404) {

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
        responseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                notificationRequestList.clear();
                for (final DataSnapshot snapshot : iterable) {
                    if ((Long) snapshot.getValue() == 0) {
                        queue = NetworkController.getInstance(NotificationActivity.this).getRequestQueue();
                        String url = hostURL + getResources().getString(R.string.url_get_tour_match_by_id);
                        url = String.format(url, snapshot.getKey());
                        Log.d("KEY", snapshot.getKey());
                        JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!response.isNull("body")) {
                                        JSONObject body = response.getJSONObject("body");
                                        Log.d("START", "START");
                                        if (body != null) {
                                            String opponentTeamName = body.getJSONObject("userId").getJSONObject("profileId").getString("name");

                                            final Date fromTime = new SimpleDateFormat("HH:mm:ss").parse(body.getJSONObject("timeSlotId").getString("startTime"));
                                            Log.d("START", new SimpleDateFormat("H:mm").format(fromTime));
                                            final Date toTime = new SimpleDateFormat("HH:mm:ss").parse(body.getJSONObject("timeSlotId").getString("endTime"));
                                            NotificationResponse notification = new NotificationResponse();

                                            notification.setTeamName(opponentTeamName);
                                            notification.setFieldId(body.getJSONObject("timeSlotId").getJSONObject("fieldOwnerId").getInt("id"));
                                            notification.setFieldName(body.getJSONObject("timeSlotId").getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                            notification.setFieldAddress(body.getJSONObject("timeSlotId").getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                            notification.setPrice((body.getJSONObject("timeSlotId").getInt("price") / 2));
                                            notification.setTourMatchId(body.getInt("id"));
                                            notification.setStartTime(fromTime);
                                            notification.setEndTime(toTime);
                                            notification.setDate(new Date(body.getJSONObject("timeSlotId").getLong("date")));
                                            notification.setUserId(body.getJSONObject("opponentId").getInt("id"));
                                            notification.setFieldTypeId(body.getJSONObject("timeSlotId").getJSONObject("fieldTypeId").getInt("id"));
                                            notification.setTimeSlotId(body.getJSONObject("timeSlotId").getInt("id"));
                                            notificationRequestList.add(notification);
                                        }
                                    }
                                    refreshLayout.setRefreshing(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } finally {
                                    //Notify adapter about data changes
                                    adapter.notifyItemChanged(notificationRequestList.size());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.child("request").child(userId + "").addValueEventListener(requestListener);
        myRef.child("response").child(userId + "").addValueEventListener(responseListener);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.child("request").child(sharedPreferences.getInt("user_id", -1) + "").removeEventListener(requestListener);
        myRef.child("response").child(sharedPreferences.getInt("user_id", -1) + "").removeEventListener(responseListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getTeamNameByUserId(DataSnapshot snapshot, JSONObject body) {
        final NotificationRequest notificationRequest = new NotificationRequest();
        try {
            Log.d("START", new SimpleDateFormat("HH:mm:ss").parse(body.getString("startTime")).toString());
            Log.d("START", new SimpleDateFormat("H:mm").format(new SimpleDateFormat("HH:mm:ss").parse(body.getString("startTime"))));
            int opponentId = Integer.parseInt(snapshot.getKey());
            notificationRequest.setRequestId(body.getInt("id"));
            notificationRequest.setUserId(opponentId);
            notificationRequest.setDate(new Date(body.getLong("date")));
            notificationRequest.setFieldTypeId(body.getJSONObject("fieldTypeId").getInt("id"));
            notificationRequest.setStartTime(new SimpleDateFormat("HH:mm:ss").parse(body.getString("startTime")));
            Log.d("START", notificationRequest.getStartTime().toString());
            notificationRequest.setEndTime(new SimpleDateFormat("HH:mm:ss").parse(body.getString("endTime")));
            FirebaseUserInfo info = snapshot.getValue(FirebaseUserInfo.class);
            notificationRequest.setLatitude(info.getLatitude());
            notificationRequest.setLongitude(info.getLongitude());

            String url = hostURL + getResources().getString(R.string.url_get_user_by_id);
            url = String.format(url, opponentId);
            queue = NetworkController.getInstance(NotificationActivity.this).getRequestQueue();
            JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (!response.isNull("body")) {
                            JSONObject body = response.getJSONObject("body");
                            if (body != null) {
                                notificationRequest.setTeamName(body.getJSONObject("profileId").getString("name"));
                                notificationRequestList.add(notificationRequest);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(notificationRequestList.size());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {

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
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
