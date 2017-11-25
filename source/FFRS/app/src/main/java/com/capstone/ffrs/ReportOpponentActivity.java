package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportOpponentActivity extends AppCompatActivity {

    private String url, hostURL, strReportReason;

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

        CheckBox chBox1 = (CheckBox) findViewById(R.id.checkBox1);
        addCheckedTextToString(chBox1);
        CheckBox chBox2 = (CheckBox) findViewById(R.id.checkBox2);
        addCheckedTextToString(chBox2);
        CheckBox chBox3 = (CheckBox) findViewById(R.id.checkBox3);
        addCheckedTextToString(chBox3);
        CheckBox chBox4 = (CheckBox) findViewById(R.id.checkBox4);
        addCheckedTextToString(chBox4);

        TextView txtOtherReason = (TextView) findViewById(R.id.text_content);
        strReportReason += txtOtherReason.getText().toString();

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

    private void addCheckedTextToString(CheckBox chBox) {
        if (chBox.isChecked()) {
            strReportReason += chBox.getText().toString() + ";";
        }
    }
}
