package com.capstone.ffrs;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwnerFriendlyNotification;
import com.capstone.ffrs.entity.FieldOwnerTourNotification;
import com.capstone.ffrs.service.TimerServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class FieldDetailActivity extends AppCompatActivity {

    private String name, address;
    private Date from, to, date;
    private int id, price, fieldTypeId;

    String hostURL;
    PendingIntent pendingIntent;
    boolean isPaused;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            stopService(new Intent(context, TimerServices.class));
            Bundle b = getIntent().getExtras();
            int timeSlotId = b.getInt("time_slot_id");
            final String status = intent.getExtras().getString("status");
            String url = hostURL + getResources().getString(R.string.url_cancel_reservation);
            url = String.format(url, timeSlotId);
            RequestQueue queue = NetworkController.getInstance(context).getRequestQueue();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (status.equals("running")) {
                        Toast toast = Toast.makeText(context, "FFR: Đã hết thời gian giữ sân", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        unregisterReceiver(br);
                        Intent intent = new Intent(context, SearchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        pendingIntent =
                                PendingIntent.getActivity(context, 0, intent, 0);
                        if (!isPaused) {
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (status.equals("running")) {
                        Toast toast = Toast.makeText(context, "FFR: Đã hết thời gian giữ sân", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        unregisterReceiver(br);
                        Intent intent = new Intent(context, SearchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        pendingIntent =
                                PendingIntent.getActivity(context, 0, intent, 0);
                        if (!isPaused) {
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            queue.add(request);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, TimerServices.class);
        startService(intent);

        hostURL = getResources().getString(R.string.local_host);

        Bundle b = getIntent().getExtras();

        name = b.getString("field_name");
        TextView txtName = (TextView) findViewById(R.id.field_name);
        txtName.setText(name);

        address = b.getString("field_address");
        TextView txtAddress = (TextView) findViewById(R.id.field_address);
        txtAddress.setText(address);

        date = (Date) b.getSerializable("date");

        from = (Date) b.get("time_from");
        Log.d("START", from.toString());
        to = (Date) b.get("time_to");
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);

        id = b.getInt("field_id");
        fieldTypeId = b.getInt("field_type_id");

        price = b.getInt("price");

        int hours = Double.valueOf(Math.abs(to.getTime() - from.getTime()) / 36e5).intValue();
        int minutes = Double.valueOf((Math.abs(to.getTime() - from.getTime()) / (60 * 1000)) % 60).intValue();

        String duration = (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");

        TextView txtDate = (TextView) findViewById(R.id.text_date);
        txtDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        TextView txtFrom = (TextView) findViewById(R.id.text_from);
        txtFrom.setText(sdf.format(from));
        TextView txtTo = (TextView) findViewById(R.id.text_to);
        txtTo.setText(sdf.format(to));
        TextView txtDuration = (TextView) findViewById(R.id.text_duration);
        txtDuration.setText(duration);

        String strFieldType = "";
        switch (fieldTypeId) {
            case 1:
                strFieldType += "5 vs 5";
                break;
            case 2:
                strFieldType += "7 vs 7";
                break;
            case 3:
                strFieldType += "11 vs 11";
                break;
            default:
                strFieldType += "Chưa xác định";
                break;
        }
        TextView txtFieldType = (TextView) findViewById(R.id.text_field_type);
        txtFieldType.setText(strFieldType);

        TextView txtTotalPrice = (TextView) findViewById(R.id.text_total_price);
        boolean tourMatchMode = b.getBoolean("tour_match_mode");
        if (tourMatchMode) {
            txtTotalPrice.setText((price * 2) + "K đồng");
        } else {
            txtTotalPrice.setText(price + "K đồng");
        }

        TextView txtPrice = (TextView) findViewById(R.id.text_price);
        txtPrice.setText(price + "K đồng");
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, TimerServices.class));
        try {
            unregisterReceiver(br);
        } catch (IllegalArgumentException e) {

        }
        Bundle b = getIntent().getExtras();
        int timeSlotId = b.getInt("time_slot_id");
        String url = hostURL + getResources().getString(R.string.url_cancel_reservation);
        url = String.format(url, timeSlotId);
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        } else {
            registerReceiver(br, new IntentFilter(TimerServices.COUNTDOWN_BR));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    public void onClickGoBackToTime(View view) {
        onBackPressed();
    }

    public void onClickShowMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("field_id", id);
        startActivity(intent);
    }

    public void onClickReserve(View view) {
        try {
            unregisterReceiver(br);
        } catch (IllegalArgumentException e) {

        }
        final Bundle b = getIntent().getExtras();
        final boolean tourMatchMode = b.getBoolean("tour_match_mode");
        String url;
        Map<String, Object> params = null;
        if (!tourMatchMode) {
            url = hostURL + getResources().getString(R.string.url_reserve_friendly_match);
            url = String.format(url, b.getInt("time_slot_id"), b.getInt("user_id"), 0);
        } else {
            url = hostURL + getResources().getString(R.string.url_reserve_tour_match);
            url = String.format(url, b.getInt("time_slot_id"), b.getInt("matching_request_id"), b.getInt("user_id"), 0);
        }

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();

        JSONObject jsonParams = null;
        if (params != null) {
            jsonParams = new JSONObject(params);
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("body")) {
                                JSONObject body = response.getJSONObject("body");
                                if (body != null && body.length() > 0) {
                                    try {
                                        Bundle b = getIntent().getExtras();
                                        Intent intent = new Intent(FieldDetailActivity.this, ReservationResultActivity.class);
                                        intent.putExtra("payment_result", "Succeed");
                                        if (!tourMatchMode) {
                                            intent.putExtra("reserve_id", body.getJSONObject("friendlyMatchId").getInt("id"));

                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FieldDetailActivity.this);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                                            editor.commit();

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = database.getReference();
                                            DatabaseReference friendlyRef = ref.child("fieldOwner").child(b.getInt("field_id") + "")
                                                    .child("friendlyMatch").child(body.getJSONObject("friendlyMatchId").getInt("id") + "");
                                            FieldOwnerFriendlyNotification notification = new FieldOwnerFriendlyNotification();
                                            notification.setIsRead(0);
                                            notification.setIsShowed(0);
                                            notification.setTime(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
                                            notification.setUsername(body.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                                            friendlyRef.setValue(notification);
                                        } else {
                                            intent.putExtra("reserve_id", body.getJSONObject("tourMatchId").getInt("id"));

                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FieldDetailActivity.this);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                                            editor.commit();

                                            if (!b.containsKey("tour_match_id")) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference ref = database.getReference();
                                                ref.child("tourMatch").child(b.getInt("opponent_id") + "").child(body.getJSONObject("tourMatchId").getInt("id") + "").setValue(0);
                                                DatabaseReference tourRef = ref.child("fieldOwner").child(b.getInt("field_id") + "")
                                                        .child("tourMatch").child(body.getJSONObject("tourMatchId").getInt("id") + "");
                                                FieldOwnerTourNotification notification = new FieldOwnerTourNotification();
                                                notification.setIsRead(0);
                                                notification.setIsShowed(0);
                                                notification.setTime(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
                                                tourRef.setValue(notification);
                                            }
                                        }
                                        intent.putExtra("tour_match_mode", tourMatchMode);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    }
                                } else {
                                    Toast.makeText(FieldDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                            try {
                                String strResponse = new String(error.networkResponse.data, "UTF-8");
                                JSONObject response = new JSONObject(strResponse);
                                if (response.getString("message").equals("Not enough money to reserve field")) {
                                    Intent intent = new Intent(FieldDetailActivity.this, ReservationResultActivity.class);
                                    intent.putExtra("field_id", id);
                                    intent.putExtra("field_name", name);
                                    intent.putExtra("field_address", address);
                                    intent.putExtra("field_type_id", fieldTypeId);
                                    intent.putExtra("date", date);
                                    intent.putExtra("time_from", from);
                                    intent.putExtra("time_to", to);
                                    intent.putExtra("price", price);
                                    intent.putExtra("user_id", b.getInt("user_id"));
                                    intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
                                    intent.putExtra("tour_match_mode", tourMatchMode);
                                    if (tourMatchMode) {
                                        intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                                        intent.putExtra("opponent_id", b.getInt("opponent_id"));
                                    }
                                    intent.putExtra("payment_result", "No Money");
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        queue.add(postRequest);


//        Bundle b = getIntent().getExtras();
//        Intent intent = new Intent(FieldDetailActivity.this, PayPalActivity.class);
//        intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
//        intent.putExtra("user_id", b.getInt("user_id"));
//        intent.putExtra("field_id", id);
//        intent.putExtra("field_name", name);
//        intent.putExtra("field_address", address);
//        intent.putExtra("price", -1 * price);
//
//        boolean tourMatchMode = b.getBoolean("tour_match_mode");
//        if (tourMatchMode) {
//            intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
//            intent.putExtra("tour_match_mode", tourMatchMode);
//            if (b.containsKey("tour_match_id")) {
//                intent.putExtra("tour_match_id", b.getInt("tour_match_id"));
//            }
//            if (b.containsKey("opponent_id")) {
//                intent.putExtra("opponent_id", b.getInt("opponent_id"));
//            }
//        }
//        unregisterReceiver(br);
//        startActivity(intent);
    }
}