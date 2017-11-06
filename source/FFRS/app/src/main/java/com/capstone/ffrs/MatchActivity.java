package com.capstone.ffrs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
    private String url;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<MatchRequest> opponentList = new ArrayList<MatchRequest>();
    private MatchAdapter adapter;
    private String hostURL;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String displayDateFormat = "dd/MM/yyyy";
    private String serverDateFormat = "dd-MM-yyyy";

    private BroadcastReceiver mRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean flag = intent.getBooleanExtra("isChecked", false);
            if (flag) {
                Button btSendRequest = (Button) findViewById(R.id.btRequest);
                btSendRequest.setEnabled(true);
                btSendRequest.setBackgroundColor(Color.parseColor("#009632"));
            } else {
                Button btSendRequest = (Button) findViewById(R.id.btRequest);
                btSendRequest.setEnabled(false);
                btSendRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = getResources().getString(R.string.local_host);

        loadMatches();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMatches();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRequestReceiver,
                new IntentFilter("RequestButton-Message"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRequestReceiver);
    }

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
            url = String.format(url, 5);
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
        params.put("date", strDate);
        params.put("duration", b.getInt("duration"));

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params), new Response.Listener<JSONObject>() {
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

        };
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    public void onClickSendRequest(View view) {

    }

}
