package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportOpponentActivity extends AppCompatActivity {

    private String url, hostURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_opponent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickSendReport(View view) {
        url = hostURL + getResources().getString(R.string.url_send_report_opponent);

        TextView txtOtherReason = (TextView) findViewById(R.id.text_content);
        String strReportReason = txtOtherReason.getText().toString();

        Bundle b = getIntent().getExtras();
        Map<String, Object> params = new HashMap<>();
        params.put("accusedId", b.getInt("opponent_id"));
        params.put("accuserId", b.getInt("user_id"));
        params.put("reason", strReportReason);
        params.put("tourMatchId", b.getInt("tour_match_id"));

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(ReportOpponentActivity.this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(ReportOpponentActivity.this, "Cảm ơn bạn đã tố cáo đối thủ.", Toast.LENGTH_LONG).show();
                startActivity(intent);
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
