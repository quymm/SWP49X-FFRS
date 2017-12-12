package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.PendingRequest;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RequestInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();

        TextView txtAddress = (TextView) findViewById(R.id.text_address);
        txtAddress.setText(b.getString("address"));

        TextView txtDate = (TextView) findViewById(R.id.text_date);
        String strDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.valueOf(b.getString("date"))));
        txtDate.setText(strDate);

        TextView txtFieldType = (TextView) findViewById(R.id.text_field_type);
        txtFieldType.setText(getResources().getStringArray(R.array.field_types)[b.getInt("field_type_id") - 1]);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            Date startTime = sdf.parse(b.getString("start_time"));
            Date endTime = sdf.parse(b.getString("end_time"));

            TextView txtStartTime = (TextView) findViewById(R.id.text_from);
            txtStartTime.setText(sdf.format(startTime));

            TextView txtEndTime = (TextView) findViewById(R.id.text_to);
            txtEndTime.setText(sdf.format(endTime));

            int duration = b.getInt("duration");
            int hours = (duration / 60);
            int minutes = (duration % 60);
            String strDuration = (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");
            TextView txtDuration = (TextView) findViewById(R.id.text_duration);
            txtDuration.setText(strDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView txtStatus = (TextView) findViewById(R.id.text_status);
        Button btCancel = (Button) findViewById(R.id.btCancel);
        Button btViewList = (Button) findViewById(R.id.btViewList);

        boolean status = b.getBoolean("status");
        if (status) {
            txtStatus.setText("Đang chờ");
            txtStatus.setTextColor(getResources().getColor(R.color.green));
            btCancel.setVisibility(View.VISIBLE);
            btViewList.setVisibility(View.VISIBLE);
        } else {
            txtStatus.setText("Đã hủy");
            txtStatus.setTextColor(getResources().getColor(R.color.red));
            btCancel.setVisibility(View.GONE);
            btViewList.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void onClickCancelRequest(View view) {
        Bundle b = getIntent().getExtras();
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        String url = HostURLUtils.getInstance(this).getHostURL() + getResources().getString(R.string.url_cancel_matching_request);
        url = String.format(url, b.getInt("id"));
        JsonObjectRequest cancelRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(RequestInfoActivity.this, "Bạn đã hủy yêu cầu đá chung", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RequestInfoActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(RequestInfoActivity.this, "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(RequestInfoActivity.this, "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(RequestInfoActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(RequestInfoActivity.this, "Lỗi parse!", Toast.LENGTH_SHORT).show();
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

    public void onClickViewList(View view) {
        TextView txtStartTime = (TextView) findViewById(R.id.text_from);
        TextView txtEndTime = (TextView) findViewById(R.id.text_to);

        Bundle b = getIntent().getExtras();
        Intent intent = new Intent(this, MatchResultActivity.class);
        intent.putExtra("field_type_id", b.getInt("field_type_id"));
        intent.putExtra("field_date", new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.valueOf(b.getString("date")))));
        intent.putExtra("field_start_time", txtStartTime.getText().toString());
        intent.putExtra("field_end_time", txtEndTime.getText().toString());
        intent.putExtra("duration", b.getInt("duration"));
        intent.putExtra("distance", b.getInt("distance"));
        intent.putExtra("priorityField", b.getBoolean("priorityField"));
        intent.putExtra("address", b.getString("address"));
        intent.putExtra("latitude", b.getDouble("latitude"));
        intent.putExtra("longitude", b.getDouble("longitude"));
        intent.putExtra("current_matching_request_id", b.getInt("id"));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        intent.putExtra("user_id", preferences.getInt("user_id", -1));
        startActivity(intent);
    }
}
