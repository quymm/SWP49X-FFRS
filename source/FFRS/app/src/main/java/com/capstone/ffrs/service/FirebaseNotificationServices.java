package com.capstone.ffrs.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.capstone.ffrs.HistoryActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.RequestInfoActivity;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.receiver.FirebaseBroadcastReceiver;
import com.capstone.ffrs.utils.HostURLUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuanPMSE61860 on 10/23/2017.
 */

public class FirebaseNotificationServices extends Service {

    public FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    Context context;

    private int userId;
    private boolean firstRequestCreated, firstResponseCreated;
    private String hostURL;
    private ValueEventListener tourMatchValueListener, matchingRequestValueListener;
    private ChildEventListener tourMatchChildListener, matchingRequestChildListener;

    @Override

    public void onCreate() {
        super.onCreate();
        context = this;

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        firstRequestCreated = false;
        firstResponseCreated = false;
        hostURL = HostURLUtils.getInstance(this).getHostURL();

        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (prefs.contains("user_id")) {
            userId = prefs.getInt("user_id", -1);
        }

        setupNotificationListener();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        myRef.child("tourMatch").child(userId + "").removeEventListener(tourMatchValueListener);
        myRef.child("matchingRequest").child(userId + "").removeEventListener(matchingRequestValueListener);

        myRef.child("tourMatch").child(userId + "").removeEventListener(tourMatchChildListener);
        myRef.child("matchingRequest").child(userId + "").removeEventListener(matchingRequestChildListener);
    }

    private void setupNotificationListener() {
        if (userId != -1) {
            matchingRequestValueListener = myRef.child("matchingRequest").child(userId + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!firstRequestCreated) {
                        firstRequestCreated = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            matchingRequestChildListener = myRef.child("matchingRequest").child(userId + "").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    if (firstRequestCreated == true) {
//                        showNotification(context, "matchingRequest", dataSnapshot);
//                    }
                    showNotification(context, "matchingRequest", dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    showNotification(context, "matchingRequest", dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            tourMatchValueListener = myRef.child("tourMatch").child(userId + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!firstResponseCreated) {
                        firstResponseCreated = true;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            tourMatchChildListener = myRef.child("tourMatch").child(userId + "").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    if (firstResponseCreated == true) {
//                        showNotification(context, "tourMatch", dataSnapshot);
//                    }
                    showNotification(context, "tourMatch", dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    showNotification(context, "tourMatch", dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showNotification(final Context context, String keyword, DataSnapshot snapshot) {
        Log.d("NOTIFICATION", snapshot.toString());
        final String title = "FFRS";

        if (keyword.equalsIgnoreCase("tourMatch")) {
            final int tourMatchId = Integer.valueOf(snapshot.getKey());
            final long status = (long) snapshot.getValue();
            if (status == 0) {
                String url = hostURL + getResources().getString(R.string.url_get_tour_match_by_id);
                url = String.format(url, tourMatchId);

                RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
                JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("body")) {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null) {
                                    String opponentTeamName = body.getJSONObject("userId").getJSONObject("profileId").getString("name");
                                    String content = opponentTeamName + " đã chấp nhận yêu cầu của bạn. Trận đấu đã được sắp xếp.";

                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle(title)
                                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                                            .setContentText(content)
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                                            .setAutoCancel(true);

                                    Intent backIntent = new Intent(context, HistoryActivity.class);
                                    backIntent.putExtra("notification_tour_match_id", tourMatchId);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    backIntent.putExtra("notification_user_id", preferences.getInt("user_id", -1));

                                    backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                                            new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);

                                    mBuilder.setContentIntent(pendingIntent);

                                    Intent deleteIntent = new Intent(getApplicationContext(), FirebaseBroadcastReceiver.class);
                                    deleteIntent.putExtra("notification_tour_match_id", tourMatchId);
                                    deleteIntent.putExtra("notification_user_id", preferences.getInt("user_id", -1));

                                    PendingIntent deletePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, deleteIntent, 0);
                                    mBuilder.setDeleteIntent(deletePendingIntent);

                                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    mNotificationManager.notify(tourMatchId, mBuilder.build());

                                    Intent intent = new Intent("balance-message");
                                    intent.putExtra("balance", body.getJSONObject("opponentId").getJSONObject("profileId").getInt("balance"));
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");

                        return headers;
                    }

                };
                queue.add(newsReq);
            }
        } else if (keyword.equalsIgnoreCase("matchingRequest")) {
            final int matchingRequestId = Integer.valueOf(snapshot.getKey());
            final long status = (long) snapshot.child("status").getValue();
            final long numberOfMatches = (long) snapshot.child("numberOfMatches").getValue();
            if (status == 0) {
                String url = hostURL + getResources().getString(R.string.url_get_matching_request_by_id);
                url = String.format(url, matchingRequestId);

                RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
                JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("body")) {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null) {
                                    if (body.getBoolean("status")) {
                                        String content = "Đã tìm thấy " + numberOfMatches + " đối thủ cho yêu cầu của bạn.";

                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle(title)
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .setContentText(content)
                                                .setAutoCancel(true);

                                        Intent backIntent = new Intent(context, RequestInfoActivity.class);
                                        backIntent.putExtra("field_type_id", body.getJSONObject("fieldTypeId").getInt("id"));
                                        backIntent.putExtra("date", body.getString("date"));
                                        backIntent.putExtra("start_time", body.getString("startTime"));
                                        backIntent.putExtra("end_time", body.getString("endTime"));
                                        backIntent.putExtra("duration", body.getInt("duration"));
                                        backIntent.putExtra("distance", body.getInt("expectedDistance"));
                                        backIntent.putExtra("priorityField", body.getBoolean("priorityField"));
                                        backIntent.putExtra("address", body.getString("address"));
                                        backIntent.putExtra("latitude", body.getDouble("latitude"));
                                        backIntent.putExtra("longitude", body.getDouble("longitude"));
                                        backIntent.putExtra("status", body.getBoolean("status"));
                                        backIntent.putExtra("createMode", false);
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        backIntent.putExtra("user_id", preferences.getInt("user_id", -1));

                                        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = database.getReference();
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("/matchingRequest/" + preferences.getInt("user_id", -1) + "/" + matchingRequestId + "/status", 1);
                                        ref.updateChildren(childUpdates);

                                        final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                                                new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);

                                        mBuilder.setContentIntent(pendingIntent);
                                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(matchingRequestId, mBuilder.build());
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

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");

                        return headers;
                    }

                };
                queue.add(newsReq);
            }
        }
    }
}