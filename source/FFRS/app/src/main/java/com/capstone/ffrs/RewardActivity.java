package com.capstone.ffrs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RewardActivity extends AppCompatActivity {

    private String hostURL;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtCurrentBonus, txtNotFound;
    private int currentPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVouchers();
            }
        });
        swipeRefreshLayout.setRefreshing(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RewardActivity.this);
        currentPoint = preferences.getInt("points", 0);
        txtCurrentBonus = (TextView) findViewById(R.id.text_current_bonus);
        txtCurrentBonus.setText("Điểm thưởng hiện tại: " + currentPoint + " điểm");

        txtNotFound = (TextView) findViewById(R.id.text_not_found);
        loadVouchers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadVouchers() {
        final GridLayout grid = (GridLayout) findViewById(R.id.grid_layout);
        grid.removeAllViews();

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();

        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels;

        String url = hostURL + getResources().getString(R.string.url_get_vouchers);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        if (body != null && body.length() > 0) {
                            for (int i = 0; i < body.length(); i++) {
                                try {
                                    final JSONObject obj = body.getJSONObject(i);

                                    final double voucherValue = obj.getDouble("voucherValue");
                                    final int bonusPointTarget = obj.getInt("bonusPointTarget");

                                    LayoutInflater inflater = LayoutInflater.from(RewardActivity.this);
                                    View v = inflater.inflate(R.layout.voucher_item, grid, false);
                                    grid.addView(v);
                                    LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.voucher_layout);
                                    linearLayout.setMinimumWidth(width / 2 - 14);

                                    TextView voucherTitle = (TextView) v.findViewById(R.id.text_voucher);
                                    voucherTitle.setText("Voucher trị giá " + String.format("%.0f", voucherValue) + " nghìn đồng");

                                    TextView pointTitle = (TextView) v.findViewById(R.id.text_bonus_points);
                                    pointTitle.setText(bonusPointTarget + " điểm");

                                    Button btExchange = (Button) v.findViewById(R.id.btExchange);
                                    btExchange.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if ((currentPoint * 1.0) < voucherValue) {
                                                AlertDialog alertDialog = new AlertDialog.Builder(RewardActivity.this).setTitle("Đổi voucher thất bại").
                                                        setMessage("Bạn không đủ điểm để đổi voucher")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        }).create();
                                                alertDialog.show();
                                            } else {
                                                redeemVoucher(obj, String.format("%.0f", voucherValue), bonusPointTarget);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.d("EXCEPTION", e.getMessage());
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
                error.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
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

    private void redeemVoucher(JSONObject obj, final String voucherText, final int bonusPointTarget) {
        try {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RewardActivity.this);
            String url = hostURL + getResources().getString(R.string.url_redeem_voucher);
            Map<String, Object> params = new HashMap<>();
            params.put("status", true);
            params.put("voucherId", obj.getInt("id"));
            params.put("userId", preferences.getInt("user_id", -1));
            RequestQueue queue = NetworkController.getInstance(RewardActivity.this).getRequestQueue();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (!response.isNull("body")) {
                        try {
                            JSONObject body = response.getJSONObject("body");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                            editor.putInt("points", body.getJSONObject("userId").getJSONObject("profileId").getInt("bonusPoint"));
                            editor.apply();
                            AlertDialog alertDialog = new AlertDialog.Builder(RewardActivity.this).setTitle("Đổi voucher thành công").
                                    setMessage("Bạn đã đổi voucher thành công. Tài khoản đã được cộng " + Integer.valueOf(voucherText) + " nghìn đồng")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).create();
                            alertDialog.show();
                            currentPoint = preferences.getInt("points", 0) - bonusPointTarget;
                            txtCurrentBonus.setText("Điểm thưởng hiện tại: " + currentPoint + " điểm");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("EXCEPTION", error.getMessage());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
