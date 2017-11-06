package com.capstone.ffrs.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.R;
import com.capstone.ffrs.adapter.PaidRequestAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.PaidFriendlyMatchRequest;
import com.capstone.ffrs.entity.PaidTourMatchRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PaidMatchFragment extends Fragment {

    private String url;
    private String hostURL;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<Object> requestList = new ArrayList<>();
    private PaidRequestAdapter adapter;
    private TextView txtNotFound;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SharedPreferences sharedPreferences;

    public PaidMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_paid_match, container, false);

        hostURL = getResources().getString(R.string.local_host);

        loadPaidMatches(view);

        txtNotFound = (TextView) view.findViewById(R.id.text_not_found);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPaidMatches(view);
            }
        });
        return view;
    }

    public void loadPaidMatches(View view) {
        if (!requestList.isEmpty()) {
            requestList.clear();
        }

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        }

        //Initialize RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new PaidRequestAdapter(this.getContext(), requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        url = hostURL + getResources().getString(R.string.url_get_paid_match_by_id);
        url = String.format(url, sharedPreferences.getInt("user_id", -1));
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONArray body = response.getJSONArray("body");
                        if (body != null && body.length() > 0) {
                            for (int i = 0; i < body.length(); i++) {
                                try {
                                    JSONObject obj = body.getJSONObject(i);
                                    if (!obj.isNull("friendlyMatchId")) {
                                        PaidFriendlyMatchRequest request = new PaidFriendlyMatchRequest();
                                        request.setId(obj.getInt("id"));

                                        request.setUserId(obj.getJSONObject("userId").getInt("id"));
                                        request.setFieldId(obj.getJSONObject("fieldOwnerId").getInt("id"));
                                        request.setFieldName(obj.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));

                                        JSONObject friendlyMatch = obj.getJSONObject("friendlyMatchId");
                                        request.setDate(friendlyMatch.getJSONObject("timeSlotId").getString("date"));
                                        request.setStartTime(friendlyMatch.getJSONObject("timeSlotId").getString("startTime"));
                                        request.setEndTime(friendlyMatch.getJSONObject("timeSlotId").getString("endTime"));
                                        requestList.add(request);
                                    }
                                    if (!obj.isNull("tourMatchId")) {
                                        PaidTourMatchRequest request = new PaidTourMatchRequest();
                                        int userId = obj.getJSONObject("userId").getInt("id");
                                        request.setId(obj.getInt("id"));
                                        request.setUserId(userId);
                                        request.setFieldId(obj.getJSONObject("fieldOwnerId").getInt("id"));
                                        request.setFieldName(obj.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));

                                        JSONObject tourMatch = obj.getJSONObject("tourMatchId");
                                        int tourUserId = tourMatch.getJSONObject("userId").getInt("id");
                                        int tourOpponentId = tourMatch.getJSONObject("opponentId").getInt("id");
                                        if (userId == tourUserId) {
                                            request.setOpponentId(tourOpponentId);
                                            request.setTeamName(tourMatch.getJSONObject("opponentId").getJSONObject("profileId").getString("name"));
                                        } else if (userId == tourOpponentId) {
                                            request.setOpponentId(tourUserId);
                                            request.setTeamName(tourMatch.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                                        }
                                        request.setTourMatchId(tourMatch.getInt("id"));
                                        request.setDate(tourMatch.getJSONObject("timeSlotId").getString("date"));
                                        request.setStartTime(tourMatch.getJSONObject("timeSlotId").getString("startTime"));
                                        request.setEndTime(tourMatch.getJSONObject("timeSlotId").getString("endTime"));
                                        requestList.add(request);
                                    }
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
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
}