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
import com.capstone.ffrs.adapter.PendingRequestAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.PendingRequest;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class PendingRequestFragment extends Fragment {
    private String url;
    private String hostURL;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<PendingRequest> requestList = new ArrayList<>();
    private PendingRequestAdapter adapter;
    private TextView txtNotFound;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SharedPreferences sharedPreferences;

    public PendingRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            swipeRefreshLayout.setRefreshing(true);
            loadPendingRequests(getView());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_pending_request, container, false);

        hostURL = HostURLUtils.getInstance(getContext()).getHostURL();

        //Initialize RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new PendingRequestAdapter(this.getContext(), requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        txtNotFound = (TextView) view.findViewById(R.id.text_not_found_pending_request);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPendingRequests(view);
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        return view;
    }

    public void loadPendingRequests(View view) {
        if (!requestList.isEmpty()) {
            requestList.clear();
            adapter.notifyDataSetChanged();
        }

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        }

        url = hostURL + getResources().getString(R.string.url_get_pending_match_by_id);
        url = String.format(url, sharedPreferences.getInt("user_id", -1));
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        if (body != null && body.length() > 0) {
                            for (int i = 0; i < body.length(); i++) {
                                try {
                                    JSONObject obj = body.getJSONObject(i);
                                    PendingRequest request = new PendingRequest();
                                    request.setMatchingRequestId(obj.getInt("id"));
                                    request.setFieldTypeId(obj.getJSONObject("fieldTypeId").getInt("id"));
                                    request.setDate(obj.getString("date"));
                                    request.setStartTime(obj.getString("startTime"));
                                    request.setEndTime(obj.getString("endTime"));
                                    request.setLatitude(obj.getDouble("latitude"));
                                    request.setLongitude(obj.getDouble("longitude"));
                                    request.setDuration(obj.getInt("duration"));
                                    request.setDistance(obj.getInt("expectedDistance"));
                                    request.setAddress(obj.getString("address"));
                                    request.setStatus(obj.getBoolean("status"));
                                    requestList.add(request);
                                } catch (Exception e) {
                                    Log.d("EXCEPTION", e.getMessage());
                                } finally {
                                    //Notify adapter about data changes
                                    Collections.sort(requestList, new Comparator<PendingRequest>() {
                                        @Override
                                        public int compare(PendingRequest o1, PendingRequest o2) {
                                            int compareStatus = compareStatus(o1.isStatus(), o2.isStatus());
                                            if (compareStatus == 0) {
                                                return compareDateTime(o1, o2);
                                            } else return compareStatus;
                                        }

                                        private int compareDateTime(PendingRequest o1, PendingRequest o2) {
                                            try {
                                                String strFirstTime = getStringDate(o1) + " " + o1.getStartTime();
                                                String strSecondTime = getStringDate(o2) + " " + o2.getStartTime();
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");
                                                Date startDate = sdf.parse(strFirstTime);
                                                Date endDate = sdf.parse(strSecondTime);
                                                return endDate.compareTo(startDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }

                                        private int compareStatus(boolean b1, boolean b2) {
                                            if (b2 && !b1) {
                                                return 1;
                                            } else if (b1 && !b2) {
                                                return -1;
                                            } else return 0;
                                        }

                                        private String getStringDate(PendingRequest o) {
                                            Date date = new Date(Long.valueOf(o.getDate()));
                                            return new SimpleDateFormat("dd/MM/yyyy").format(date);
                                        }
                                    });
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
}
