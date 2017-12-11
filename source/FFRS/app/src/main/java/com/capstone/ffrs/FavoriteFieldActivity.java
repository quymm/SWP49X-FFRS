package com.capstone.ffrs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.adapter.FavoriteFieldAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FavoriteField;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteFieldActivity extends AppCompatActivity {
    private String url, hostURL;
    private List<FavoriteField> fieldOwnerList = new ArrayList<FavoriteField>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadFavoriteField();
            }
        });
        swipeRefreshLayout.setRefreshing(true);

        txtNotFound = (TextView) findViewById(R.id.text_not_found);

        loadFavoriteField();
    }

    @Override
    protected void onResume() {
        super.onResume();

        swipeRefreshLayout.setRefreshing(true);
        if (!fieldOwnerList.isEmpty()) {
            fieldOwnerList.clear();
            loadFavoriteField();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private void loadFavoriteField() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!fieldOwnerList.isEmpty()) {
            fieldOwnerList.clear();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final FavoriteFieldAdapter adapter = new FavoriteFieldAdapter(this, fieldOwnerList);
        adapter.setUserId(preferences.getInt("user_id", -1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        url = hostURL + getResources().getString(R.string.url_get_favorite_fields);
        url = String.format(url, preferences.getInt("user_id", -1));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        if (body != null && body.length() > 0) {
                            for (int i = 0; i < body.length(); i++) {
                                try {
                                    JSONObject obj = body.getJSONObject(i);
                                    JSONObject profile = obj.getJSONObject("fieldOwnerId").getJSONObject("profileId");
                                    FavoriteField fieldOwner = new FavoriteField(obj.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));
                                    fieldOwner.setFieldId(obj.getJSONObject("fieldOwnerId").getInt("id"));

                                    // adding movie to movies array
                                    fieldOwnerList.add(fieldOwner);
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
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }
        };
        queue.add(request);
    }
}
