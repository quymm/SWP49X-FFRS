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
import com.capstone.ffrs.FieldDetailActivity;
import com.capstone.ffrs.HistoryActivity;
import com.capstone.ffrs.NotificationActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;
import com.google.firebase.database.ChildEventListener;
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
import java.util.Date;

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

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        firstRequestCreated = false;
        firstResponseCreated = false;
        hostURL = getResources().getString(R.string.local_host);

        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (prefs.contains("user_id")) {
            userId = prefs.getInt("user_id", -1);
        }

        setupNotificationListener();
    }


    private void setupNotificationListener() {
//
//        mDatabase.getReference().child("notifications")
//                .orderByChild("status").equalTo(0)
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        if(dataSnapshot != null){
//                            NotificationRequest notification = dataSnapshot.getValue(NotificationRequest.class);
//
//                            showNotification(context,notification,dataSnapshot.getKey());
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Utilities.log("onChildChanged",dataSnapshot);
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                        Utilities.log("onChildRemoved",dataSnapshot);
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                        Utilities.log("onChildMoved",dataSnapshot);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Utilities.log("onCancelled",databaseError);
//                    }
//                });
//
        if (userId != -1) {
//            myRef.child("request").child(userId + "").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (firstRequestCreated == false) {
//                        firstRequestCreated = true;
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            myRef.child("request").child(userId + "").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    if (firstRequestCreated == true) {
//                        showNotification(context, "request", null);
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//
//                }
//            });
            myRef.child("tourMatch").child(userId + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (firstResponseCreated == false) {
                        firstResponseCreated = true;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            myRef.child("tourMatch").child(userId + "").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (firstResponseCreated == true) {
                        showNotification(context, "tourMatch", dataSnapshot);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showNotification(final Context context, String keyword, DataSnapshot snapshot) {
        final String title = "FFRS";

//        if (keyword.equalsIgnoreCase("request")) {
//            String content = "Có đối thủ yêu cầu đá chung.";
//
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setDefaults(NotificationCompat.DEFAULT_ALL)
//                    .setContentText(content)
//                    .setAutoCancel(true);
//
//            Intent backIntent = new Intent(context, NotificationActivity.class);
//            backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
//                    new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);
//
//            mBuilder.setContentIntent(pendingIntent);
//
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(1, mBuilder.build());
//        } else if (keyword.equalsIgnoreCase("response")) {
//            final int tourMatchId = Integer.valueOf(snapshot.getKey());
//            String url = hostURL + getResources().getString(R.string.url_get_tour_match_by_id);
//            url = String.format(url, tourMatchId);
//
//            RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
//            JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        if (!response.isNull("body")) {
//                            JSONObject body = response.getJSONObject("body");
//                            if (body != null) {
//
//                                String opponentTeamName = body.getJSONObject("userId").getJSONObject("profileId").getString("name");
//                                String content = opponentTeamName + " đã đồng ý. Mời bạn thanh toán để tham gia trận đấu";
//
//                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                                        .setSmallIcon(R.mipmap.ic_launcher)
//                                        .setContentTitle(title)
//                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
//                                        .setContentText(content)
//                                        .setAutoCancel(true);
//
//                                Intent backIntent = new Intent(context, FieldDetailActivity.class);
//
//                                final Date fromTime = new SimpleDateFormat("HH:mm:ss").parse(body.getJSONObject("timeSlotId").getString("startTime"));
//                                final Date toTime = new SimpleDateFormat("HH:mm:ss").parse(body.getJSONObject("timeSlotId").getString("endTime"));
//
//                                backIntent.putExtra("field_id", body.getJSONObject("timeSlotId").getJSONObject("fieldOwnerId").getInt("id"));
//                                backIntent.putExtra("field_name", body.getJSONObject("timeSlotId").getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
//                                backIntent.putExtra("field_address", body.getJSONObject("timeSlotId").getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
//                                backIntent.putExtra("field_type_id", body.getJSONObject("timeSlotId").getJSONObject("fieldTypeId").getInt("id"));
//                                backIntent.putExtra("date", new Date(body.getJSONObject("timeSlotId").getLong("date")));
//                                backIntent.putExtra("time_from", fromTime);
//                                backIntent.putExtra("time_to", toTime);
//                                backIntent.putExtra("price", (body.getJSONObject("timeSlotId").getInt("price") / 2));
//                                backIntent.putExtra("user_id", body.getJSONObject("opponentId").getInt("id"));
//                                backIntent.putExtra("time_slot_id", body.getInt("id"));
//                                backIntent.putExtra("tour_match_id", tourMatchId);
//                                backIntent.putExtra("tour_match_mode", true);
//                                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
//                                        new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);
//
//                                mBuilder.setContentIntent(pendingIntent);
//
//                                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                                mNotificationManager.notify(1, mBuilder.build());
//
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
//
//                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                        Toast.makeText(getApplicationContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof AuthFailureError) {
//                        Toast.makeText(getApplicationContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof ServerError) {
//                        Toast.makeText(getApplicationContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof NetworkError) {
//                        Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof ParseError) {
//                        Toast.makeText(getApplicationContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }) {
//                @Override
//                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                    try {
//                        String utf8String = new String(response.data, "UTF-8");
//                        return Response.success(new JSONObject(utf8String), HttpHeaderParser.parseCacheHeaders(response));
//                    } catch (UnsupportedEncodingException e) {
//                        // log error
//                        return Response.error(new ParseError(e));
//                    } catch (JSONException e) {
//                        // log error
//                        return Response.error(new ParseError(e));
//                    }
//                }
//
//            };
//            queue.add(newsReq);
//
//        } else
        if (keyword.equalsIgnoreCase("tourMatch")) {
            final int tourMatchId = Integer.valueOf(snapshot.getKey());
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
                                String content = opponentTeamName + " đã chấp nhận yêu cầu của bạn.\nTrận đấu đã được sắp xếp.";

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle(title)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setContentText(content)
                                        .setAutoCancel(true);

                                Intent backIntent = new Intent(context, HistoryActivity.class);

                                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                                        new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);

                                mBuilder.setContentIntent(pendingIntent);

                                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(body.getInt("id"), mBuilder.build());

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

            };
            queue.add(newsReq);

        }


    }

}