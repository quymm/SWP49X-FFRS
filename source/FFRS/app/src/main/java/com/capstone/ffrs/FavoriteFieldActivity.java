package com.capstone.ffrs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.adapter.FavoriteFieldAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFieldActivity extends AppCompatActivity {
    private String url, hostURL;
    private List<FieldOwner> fieldOwnerList = new ArrayList<FieldOwner>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtNotFound;

    private BroadcastReceiver mRemoveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(true);
            loadFavoriteField();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = getResources().getString(R.string.local_host);

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

        LocalBroadcastManager.getInstance(this).registerReceiver(mRemoveReceiver,
                new IntentFilter("remove-message"));
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRemoveReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private void loadFavoriteField() {
        if (!fieldOwnerList.isEmpty()) {
            fieldOwnerList.clear();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final FavoriteFieldAdapter adapter = new FavoriteFieldAdapter(this, fieldOwnerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                                    FieldOwner fieldOwner = new FieldOwner(obj.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));

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
        });
        queue.add(request);
    }
}
