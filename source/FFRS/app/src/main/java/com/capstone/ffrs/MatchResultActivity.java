package com.capstone.ffrs;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.capstone.ffrs.adapter.MatchAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.MatchRequest;
import com.capstone.ffrs.utils.HostURLUtils;

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

public class MatchResultActivity extends AppCompatActivity {
    private String url;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<MatchRequest> opponentList = new ArrayList<MatchRequest>();
    private MatchAdapter adapter;
    private String hostURL;

    private TextView txtNotFound;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String displayDateFormat = "dd/MM/yyyy";
    private String serverDateFormat = "dd-MM-yyyy";

//    private BroadcastReceiver mRequestReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            boolean flag = intent.getBooleanExtra("isChecked", false);
//            if (flag) {
//                Button btSendRequest = (Button) findViewById(R.id.btRequest);
//                btSendRequest.setEnabled(true);
//                btSendRequest.setBackgroundColor(Color.parseColor("#009632"));
//            } else {
//                Button btSendRequest = (Button) findViewById(R.id.btRequest);
//                btSendRequest.setEnabled(false);
//                btSendRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        loadMatches();

        txtNotFound = (TextView) findViewById(R.id.text_not_found);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMatches();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRequestReceiver,
//                new IntentFilter("RequestButton-Message"));
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRequestReceiver);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void loadMatches() {
        if (!opponentList.isEmpty()) {
            opponentList.clear();
        }
        Bundle b = getIntent().getExtras();
        SimpleDateFormat sdf = new SimpleDateFormat(displayDateFormat);
        String strDate = "";
        try {
            Date date = sdf.parse(b.getString("field_date"));
            sdf = new SimpleDateFormat(serverDateFormat);
            strDate = sdf.format(date);
            url = hostURL + getResources().getString(R.string.url_get_matching_requests);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MatchAdapter(this, opponentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Map<String, Object> params = new HashMap<>();
        params.put("userId", b.getInt("user_id"));
        params.put("fieldTypeId", b.getInt("field_type_id"));
        params.put("latitude", b.getDouble("latitude"));
        params.put("longitude", b.getDouble("longitude"));
        params.put("startTime", b.getString("field_start_time"));
        params.put("endTime", b.getString("field_end_time"));
        params.put("expectedDistance", b.getInt("distance"));
        params.put("date", strDate);
        params.put("address", b.getString("address"));
        params.put("priorityField", b.getBoolean("priorityField"));
        params.put("duration", b.getInt("duration"));

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        if (body != null && body.length() > 0) {
                            for (int i = 0; i < body.length(); i++) {
                                try {
                                    JSONObject obj = body.getJSONObject(i);
                                    MatchRequest match = new MatchRequest();
                                    match.setId(obj.getInt("id"));
                                    match.setTeamName(obj.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                                    match.setImageURL(obj.getJSONObject("userId").getJSONObject("profileId").getString("avatarUrl"));
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
                            txtNotFound.setVisibility(View.GONE);
                        } else {
                            txtNotFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txtNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    swipeRefreshLayout.setRefreshing(false);
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
                swipeRefreshLayout.setRefreshing(false);
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
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    @Override
    public void onBackPressed() {
        final Bundle b = getIntent().getExtras();
        boolean createMode = b.getBoolean("createMode");

        if (createMode) {
//            Intent intent = new Intent(MatchResultActivity.this, CreateRequestResultActivity.class);
//            intent.putExtra("user_id", b.getInt("user_id"));
//            intent.putExtra("message", "Hệ thống đã để lại yêu cầu của bạn cho đối thủ phù hợp hơn!");
//            startActivity(intent);
            final int createdMatchingRequestId = b.getInt("created_matching_request_id");
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Bạn có chắc chắn không muốn đá với những đối thủ này?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MatchResultActivity.this, CreateRequestResultActivity.class);
                            intent.putExtra("user_id", b.getInt("user_id"));
                            intent.putExtra("message", "Hệ thống đã để lại yêu cầu của bạn cho đối thủ phù hợp hơn!");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            RequestQueue queue = NetworkController.getInstance(MatchResultActivity.this).getRequestQueue();
                            String url = HostURLUtils.getInstance(MatchResultActivity.this).getHostURL() + getResources().getString(R.string.url_cancel_matching_request);
                            url = String.format(url, createdMatchingRequestId);
                            JsonObjectRequest cancelRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        Toast.makeText(MatchResultActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof AuthFailureError) {
                                        Toast.makeText(MatchResultActivity.this, "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ServerError) {
                                        Toast.makeText(MatchResultActivity.this, "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof NetworkError) {
                                        Toast.makeText(MatchResultActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ParseError) {
                                        Toast.makeText(MatchResultActivity.this, "Lỗi parse!", Toast.LENGTH_SHORT).show();
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
                            queue.add(cancelRequest);
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

}
