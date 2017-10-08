package com.capstone.ffrs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.capstone.ffrs.adapter.FieldTimeAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldTime;
import com.capstone.ffrs.utils.TimePickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FieldTimeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String url = "https://api.myjson.com/bins/unkad";

    RecyclerView recyclerView;
    RequestQueue queue;
    List<FieldTime> fieldTimeList = new ArrayList<FieldTime>();
    FieldTimeAdapter adapter;

    String imageUrl, name, address;
    int id;

    private ProgressBar spinner;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fromTime = intent.getStringExtra("from");
            String toTime = intent.getStringExtra("to");
            EditText from = (EditText) findViewById(R.id.text_from);
            EditText to = (EditText) findViewById(R.id.text_to);
            from.setText(fromTime);
            to.setText(toTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_time);

        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        Bundle b = getIntent().getExtras();

        name = b.getString("field_name");
        TextView txtName = (TextView) findViewById(R.id.field_name);
        txtName.setText(name);

        address = b.getString("field_address");
        TextView txtAddress = (TextView) findViewById(R.id.field_address);
        txtAddress.setText(address);

        imageUrl = b.getString("image_url");
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.field_image);
        imageView.setImageUrl(imageUrl, NetworkController.getInstance(this.getBaseContext()).getImageLoader());

        id = b.getInt("field_id");

        //url = "http://10.0.2.2:8080/swp49x-ffrs/time-enable/managed-time-enable?field-owner-id"+id;

        final EditText from = (EditText) findViewById(R.id.text_from);
        final EditText to = (EditText) findViewById(R.id.text_to);
        from.setOnClickListener(new TimePickerListener(this, from));
        to.setOnClickListener(new TimePickerListener(this, to));


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        loadFieldTimes();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rewards) {
            Intent intent = new Intent(this, RewardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickShowDetail(View view) {
        EditText from = (EditText) findViewById(R.id.text_from);
        EditText to = (EditText) findViewById(R.id.text_to);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
        Log.d("FROM", from.getText().toString());
        try {
            Date fromTime = sdf.parse(from.getText().toString());
            Date toTime = sdf.parse(to.getText().toString());

            Intent intent = new Intent(this, FieldDetailActivity.class);
            intent.putExtra("field_id", id);
            intent.putExtra("field_name", name);
            intent.putExtra("field_address", address);
            intent.putExtra("image_url", imageUrl);
            intent.putExtra("time_from", fromTime);
            intent.putExtra("time_to", toTime);
            startActivity(intent);
        } catch (ParseException e) {
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "Hãy nhập giờ chơi!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFieldTimes() {
        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FieldTimeAdapter(this, fieldTimeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        FieldTime fieldTime = new FieldTime(obj.getString("from"), obj.getString("to"), obj.getInt("price"));

                        // adding movie to movies array
                        fieldTimeList.add(fieldTime);

                    } catch (Exception e) {
                        Log.d("EXCEPTION", e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
                }
                spinner.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONArray(utf8String), HttpHeaderParser.parseCacheHeaders(response));
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
