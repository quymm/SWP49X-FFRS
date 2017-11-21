package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.fragment.RechargeFailFragment;
import com.capstone.ffrs.fragment.RechargeSucessFragment;
import com.capstone.ffrs.fragment.ReserveNoMoneyFragment;
import com.capstone.ffrs.fragment.ReserveSuccessFragment;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ReservationResultActivity extends AppCompatActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        String result = b.getString("payment_result");
        switch (result) {
            case "Succeed":
                mFragment = new ReserveSuccessFragment();
                break;
            case "Cancelled":
                mFragment = new RechargeFailFragment();
                break;
            case "Invalid configuration":
                mFragment = new RechargeFailFragment();
                break;
            case "No Money":
                mFragment = new ReserveNoMoneyFragment();
                break;
            case "Recharged":
                mFragment = new RechargeSucessFragment();
                break;
            default:
                mFragment = new RechargeFailFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_layout, mFragment);
        fragmentTransaction.commit();
        setContentView(R.layout.activity_reservation_result);
    }

    @Override
    public void onBackPressed() {
        final Bundle b = getIntent().getExtras();
        String result = b.getString("payment_result");
        Intent intent;
        switch (result) {
            case "Succeed":
                boolean tourMatchMode = b.getBoolean("tour_match_mode");
                if (!tourMatchMode) {
                    intent = new Intent(this, FieldTimeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, HistoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            case "Cancelled":
                intent = new Intent(this, PaymentOptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "Invalid configuration":
                intent = new Intent(this, PaymentOptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "No Money":
                if (!b.getBoolean("tour_match_mode")) {
                    intent = new Intent(this, FieldTimeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, MatchResultActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
            case "Recharged":
                if (b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
                    String url;
                    RequestQueue queue;
                    Map<String, Object> params = new HashMap<>();

                    if (b.getBoolean("tour_match_mode")) {
                        url = HostURLUtils.getInstance(this).getHostURL() + getResources().getString(R.string.url_choose_field);
                        url = String.format(url, b.getInt("matching_request_id"), 5);

                        queue = NetworkController.getInstance(this).getRequestQueue();

                        params.put("date", new SimpleDateFormat("dd-MM-yyyy").format(b.getSerializable("date")));
                        params.put("userId", b.getInt("user_id"));
                        params.put("startTime", new SimpleDateFormat("H:mm").format(b.getSerializable("time_from")));
                        params.put("endTime", new SimpleDateFormat("H:mm").format(b.getSerializable("time_to")));
                        params.put("fieldTypeId", b.getInt("field_type_id"));
                        params.put("latitude", b.getDouble("latitude"));
                        params.put("longitude", b.getDouble("longitude"));
                        params.put("duration", b.getInt("duration"));
                    } else {
                        url = HostURLUtils.getInstance(this).getHostURL() + getResources().getString(R.string.url_reserve_time_slot);
                        queue = NetworkController.getInstance(this).getRequestQueue();

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
                                                    Intent intent = new Intent(ReservationResultActivity.this, FieldDetailActivity.class);
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
                                                Toast.makeText(ReservationResultActivity.this, "Không còn sân trống!", Toast.LENGTH_SHORT).show();
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
                    intent = new Intent(this, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
            default:
                intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickGoBackToHome(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
