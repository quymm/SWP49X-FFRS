package com.capstone.ffrs.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.FieldDetailActivity;
import com.capstone.ffrs.SearchActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuanPMSE61860 on 11/7/2017.
 */

public class RechargeSucessFragment extends Fragment {
    private Button btHome;

    public RechargeSucessFragment() {
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
        View view = inflater.inflate(R.layout.fragment_recharge_success, container, false);
        btHome = (Button) view.findViewById(R.id.btHome);
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle b = getActivity().getIntent().getExtras();
                if (b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
                    String url;
                    RequestQueue queue;
                    Map<String, Object> params = new HashMap<>();

                    if (b.getBoolean("tour_match_mode")) {
                        url = getResources().getString(R.string.local_host) + getResources().getString(R.string.url_choose_field);
                        url = String.format(url, b.getInt("matching_request_id"), 5);

                        queue = NetworkController.getInstance(getContext()).getRequestQueue();

                        params.put("date", new SimpleDateFormat("dd-MM-yyyy").format(b.getSerializable("date")));
                        params.put("userId", b.getInt("user_id"));
                        params.put("startTime", new SimpleDateFormat("H:mm").format(b.getSerializable("time_from")));
                        params.put("endTime", new SimpleDateFormat("H:mm").format(b.getSerializable("time_to")));
                        params.put("fieldTypeId", b.getInt("field_type_id"));
                        params.put("latitude", b.getDouble("latitude"));
                        params.put("longitude", b.getDouble("longitude"));
                        params.put("duration", b.getInt("duration"));
                    } else {
                        url = getResources().getString(R.string.local_host) + getResources().getString(R.string.url_reserve_time_slot);
                        queue = NetworkController.getInstance(getContext()).getRequestQueue();

                        params.put("date", new SimpleDateFormat("dd-MM-yyyy").format(b.getSerializable("date")));
                        params.put("endTime", new SimpleDateFormat("H:mm").format(b.getSerializable("time_to")));
                        params.put("fieldOwnerId", b.getInt("field_id"));
                        params.put("fieldTypeId", b.getInt("field_type_id"));
                        params.put("startTime", new SimpleDateFormat("H:mm").format(b.getSerializable("time_from")));
                        params.put("userId", b.getInt("user_id"));
                    }

                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (!response.isNull("body")) {
                                            JSONObject body = response.getJSONObject("body");
                                            if (body != null && body.length() > 0) {
                                                try {
                                                    Intent intent = new Intent(getContext(), FieldDetailActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    intent.putExtra("field_id", b.getInt("field_id"));
                                                    intent.putExtra("field_name", b.getString("field_name"));
                                                    intent.putExtra("field_address", b.getString("field_address"));
                                                    intent.putExtra("field_type_id", b.getInt("field_type_id"));
                                                    intent.putExtra("date", b.getSerializable("date"));
                                                    intent.putExtra("time_from", b.getSerializable("time_from"));
                                                    intent.putExtra("time_to", b.getSerializable("time_to"));
                                                    intent.putExtra("price", body.getInt("price"));
                                                    intent.putExtra("user_id", b.getInt("user_id"));
                                                    intent.putExtra("time_slot_id", body.getInt("id"));
                                                    intent.putExtra("tour_match_mode", b.getBoolean("tour_match_mode"));
                                                    if (b.getBoolean("tour_match_mode")) {
                                                        intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                                                        intent.putExtra("opponent_id", b.getInt("opponent_id"));
                                                    }
                                                    startActivity(intent);
                                                } catch (Exception e) {
                                                    Log.d("EXCEPTION", e.getMessage());
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Không còn sân trống!", Toast.LENGTH_SHORT).show();
                                            }
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
                } else {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
//        Bundle b = getActivity().getIntent().getExtras();
//        TextView code = (TextView) view.findViewById(R.id.text_code);
//        code.setText("Mã đặt sân: " + b.getInt("reserve_id"));

            }
        });
        return view;
    }
}
