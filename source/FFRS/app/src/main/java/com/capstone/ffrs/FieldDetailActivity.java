package com.capstone.ffrs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwnerFriendlyNotification;
import com.capstone.ffrs.entity.FieldOwnerTourNotification;
import com.capstone.ffrs.entity.PendingRequest;
import com.capstone.ffrs.utils.HostURLUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FieldDetailActivity extends AppCompatActivity {

    private String name, address;
    private Date startTime, endTime, date;
    private int id, price, fieldTypeId;
    private RelativeLayout relativeLayout;
    private FrameLayout progressBarHolder;

    String hostURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        Bundle b = getIntent().getExtras();

        name = b.getString("field_name");
        TextView txtName = (TextView) findViewById(R.id.field_name);
        txtName.setText(name);

        address = b.getString("field_address");
        TextView txtAddress = (TextView) findViewById(R.id.field_address);
        txtAddress.setText(address);

        date = (Date) b.getSerializable("date");

        startTime = (Date) b.getSerializable("time_from");
        endTime = (Date) b.getSerializable("time_to");
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);

        id = b.getInt("field_id");
        fieldTypeId = b.getInt("field_type_id");

        price = b.getInt("price");

        int hours = Double.valueOf(Math.abs(endTime.getTime() - startTime.getTime()) / 36e5).intValue();
        int minutes = Double.valueOf((Math.abs(endTime.getTime() - startTime.getTime()) / (60 * 1000)) % 60).intValue();

        String duration = (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");

        TextView txtDate = (TextView) findViewById(R.id.text_date);
        txtDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        TextView txtFrom = (TextView) findViewById(R.id.text_from);
        txtFrom.setText(sdf.format(startTime));
        TextView txtTo = (TextView) findViewById(R.id.text_to);
        txtTo.setText(sdf.format(endTime));
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
            txtTotalPrice.setText((price * 2) + " ngàn đồng");
        } else {
            txtTotalPrice.setText(price + " ngàn đồng");
        }

        TextView txtPrice = (TextView) findViewById(R.id.text_price);
        txtPrice.setText(price + " ngàn đồng");

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        relativeLayout.setAlpha(0.5f);
        progressBarHolder.setVisibility(View.VISIBLE);
        final Button btPayment = (Button) findViewById(R.id.btPayment);
        btPayment.setEnabled(false);
        final Button btCancel = (Button) findViewById(R.id.btCancel);
        btCancel.setEnabled(false);
        final Bundle b = getIntent().getExtras();
        if (b.containsKey("created_matching_request_id")) {
            int createdMatchingRequestId = b.getInt("created_matching_request_id");

            RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
            String url = HostURLUtils.getInstance(this).getHostURL() + getResources().getString(R.string.url_cancel_matching_request);
            url = String.format(url, createdMatchingRequestId);
            JsonObjectRequest cancelRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                        Toast.makeText(FieldDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof AuthFailureError) {
//                        Toast.makeText(FieldDetailActivity.this, "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof ServerError) {
//                        Toast.makeText(FieldDetailActivity.this, "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof NetworkError) {
//                        Toast.makeText(FieldDetailActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
//                    } else if (error instanceof ParseError) {
//                        Toast.makeText(FieldDetailActivity.this, "Lỗi parse!", Toast.LENGTH_SHORT).show();
//                    }

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
            queue.add(cancelRequest);
        }
        final boolean tourMatchMode = b.getBoolean("tour_match_mode");
        String url;
        if (!tourMatchMode) {
            url = hostURL + getResources().getString(R.string.url_reserve_friendly_match);
        } else {
            url = hostURL + getResources().getString(R.string.url_reserve_tour_match);
            url = String.format(url, b.getInt("matching_request_id"));
        }

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();

        Map<String, Object> params = new HashMap<>();
        params.put("date", new SimpleDateFormat("dd-MM-yyyy").format(date));
        params.put("endTime", new SimpleDateFormat("H:mm").format(endTime));
        params.put("fieldOwnerId", id);
        params.put("fieldTypeId", fieldTypeId);
        params.put("startTime", new SimpleDateFormat("H:mm").format(startTime));
        params.put("userId", b.getInt("user_id"));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
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
                                            editor.apply();

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = database.getReference();
                                            DatabaseReference friendlyRef = ref.child("fieldOwner").child(b.getInt("field_id") + "")
                                                    .child("friendlyMatch").child(body.getJSONObject("friendlyMatchId").getInt("id") + "");
                                            FieldOwnerFriendlyNotification notification = new FieldOwnerFriendlyNotification();
                                            notification.setIsRead(0);
                                            notification.setIsShowed(0);
                                            notification.setPlayTime(new SimpleDateFormat("MM-dd-yyyy").format(date) + " " + new SimpleDateFormat("H:mm").format(startTime));
                                            notification.setTime(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
                                            notification.setUsername(body.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                                            friendlyRef.setValue(notification);
                                        } else {
                                            intent.putExtra("reserve_id", body.getJSONObject("tourMatchId").getInt("id"));

                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FieldDetailActivity.this);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                                            editor.apply();

                                            if (!b.containsKey("tour_match_id")) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference ref = database.getReference();
                                                ref.child("tourMatch").child(b.getInt("opponent_id") + "").child(body.getJSONObject("tourMatchId").getInt("id") + "").setValue(0);
                                                DatabaseReference tourRef = ref.child("fieldOwner").child(b.getInt("field_id") + "")
                                                        .child("tourMatch").child(body.getJSONObject("tourMatchId").getInt("id") + "");
                                                FieldOwnerTourNotification notification = new FieldOwnerTourNotification();
                                                notification.setIsRead(0);
                                                notification.setIsShowed(0);
                                                notification.setPlayTime(new SimpleDateFormat("dd-MM-yyyy").format(date) + " " + new SimpleDateFormat("H:mm").format(startTime));
                                                notification.setTime(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
                                                tourRef.setValue(notification);
                                            }
                                        }
                                        intent.putExtra("tour_match_mode", tourMatchMode);
                                        startActivity(intent);
                                    } catch (Exception e) {
//                                        Log.d("EXCEPTION", e.getMessage());\
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(FieldDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (!tourMatchMode) {
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(FieldDetailActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(FieldDetailActivity.this);
                                    }
                                    builder.setTitle("Hết sân trống")
                                            .setMessage("Bạn có muốn chọn giờ khác không?")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(FieldDetailActivity.this, SearchActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                } else {
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(FieldDetailActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(FieldDetailActivity.this);
                                    }
                                    builder.setTitle("Hết sân trống")
                                            .setMessage("Bạn có muốn hệ thống chọn sân khác không?")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String url = hostURL + getResources().getString(R.string.url_choose_field);
                                                    url = String.format(url, b.getInt("matching_request_id"), 5);

                                                    RequestQueue queue = NetworkController.getInstance(FieldDetailActivity.this).getRequestQueue();

                                                    final String strDate = b.getString("date");

                                                    Map<String, Object> params = new HashMap<>();
                                                    params.put("date", strDate);
                                                    params.put("userId", b.getInt("user_id"));
                                                    params.put("startTime", new SimpleDateFormat("H:mm").format(startTime));
                                                    params.put("endTime", new SimpleDateFormat("H:mm").format(endTime));
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
                                                                                    Intent intent = new Intent(FieldDetailActivity.this, FieldDetailActivity.class);
                                                                                    intent.putExtra("field_id", body.getJSONObject("fieldOwnerId").getInt("id"));
                                                                                    intent.putExtra("field_name", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                                                                    intent.putExtra("field_address", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                                                                    intent.putExtra("field_type_id", body.getJSONObject("fieldTypeId").getInt("id"));
                                                                                    intent.putExtra("date", new Date(Long.parseLong(strDate)));
                                                                                    intent.putExtra("time_from", fromTime);
                                                                                    intent.putExtra("time_to", toTime);
                                                                                    intent.putExtra("price", body.getInt("price"));
                                                                                    intent.putExtra("user_id", b.getInt("user_id"));
                                                                                    intent.putExtra("tour_match_mode", true);
                                                                                    intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                                                                                    intent.putExtra("opponent_id", b.getInt("opponent_id"));
                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                                    startActivity(intent);
                                                                                } catch (Exception e) {
                                                                                    Log.d("EXCEPTION", e.getMessage());
                                                                                }
                                                                            } else {
                                                                                Toast toast = Toast.makeText(FieldDetailActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_LONG);
                                                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                                                toast.show();
                                                                            }
                                                                        } else {
                                                                            Toast toast = Toast.makeText(FieldDetailActivity.this, "Không tìm thấy sân phù hợp!", Toast.LENGTH_LONG);
                                                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                                                            toast.show();
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
                                            })
                                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(FieldDetailActivity.this, MatchResultActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                    btPayment.setEnabled(true);
                                    btCancel.setEnabled(true);
                                    relativeLayout.setAlpha(1f);
                                    progressBarHolder.setVisibility(View.GONE);
                                }
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
                                    intent.putExtra("time_from", startTime);
                                    intent.putExtra("time_to", endTime);
                                    intent.putExtra("price", price);
                                    intent.putExtra("user_id", b.getInt("user_id"));
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
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(FieldDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(FieldDetailActivity.this, "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FieldDetailActivity.this, "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FieldDetailActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(FieldDetailActivity.this, "Lỗi parse!", Toast.LENGTH_SHORT).show();
                        }
                        btPayment.setEnabled(true);
                        btCancel.setEnabled(true);
                        relativeLayout.setAlpha(1f);
                        progressBarHolder.setVisibility(View.GONE);
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

    public void onClickGoBackToHome(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}