package com.capstone.ffrs;

import android.app.DatePickerDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldTimeActivity extends AppCompatActivity {

    String url;
    String localhost;

    RecyclerView recyclerView;
    RequestQueue queue;
    List<FieldTime> fieldTimeList = new ArrayList<FieldTime>();
    FieldTimeAdapter adapter;

    String imageUrl, name, address;
    int id;
    int price = 0;

    String displayFormat = "dd/MM/yyyy";
    String serverFormat = "dd-MM-yyyy";

    private ProgressBar spinner;
    private TextView txtNotFound;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fromTime = intent.getStringExtra("from");
            String toTime = intent.getStringExtra("to");
            price = intent.getIntExtra("price", 0);
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

        txtNotFound = (TextView) findViewById(R.id.text_no_free_time);

        localhost = getResources().getString(R.string.local_host);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        Log.d("receivedId", id + "");

        final Button date = (Button) findViewById(R.id.date_picker);
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);

        long currentMillis = System.currentTimeMillis();
        String strCurrentDate = sdf.format(currentMillis);
        date.setText(strCurrentDate);

        sdf = new SimpleDateFormat(serverFormat);

        url = localhost + "/swp49x-ffrs/match/free-time?field-owner-id=" + id + "&field-type-id=" + 1 + "&date=" + sdf.format(currentMillis);

        final Calendar dateSelected = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateSelected.set(Calendar.YEAR, year);
                dateSelected.set(Calendar.MONTH, monthOfYear);
                dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);

                date.setText(sdf.format(dateSelected.getTime()));

                sdf = new SimpleDateFormat(serverFormat);

                url = localhost + "/swp49x-ffrs/match/free-time?field-owner-id=" + id + "&field-type-id=" + 1 + "&date=" + sdf.format(dateSelected.getTime());

                clearData();

                spinner.setVisibility(View.VISIBLE);
                txtNotFound.setVisibility(View.GONE);
                loadFieldTimes();
            }

        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog = new DatePickerDialog(FieldTimeActivity.this, datePickerListener, dateSelected
                        .get(Calendar.YEAR), dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

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

    public void onClickShowDetail(View view) {
        Button btDate = (Button) findViewById(R.id.date_picker);
        EditText from = (EditText) findViewById(R.id.text_from);
        EditText to = (EditText) findViewById(R.id.text_to);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        Log.d("FROM", from.getText().toString());
        try {
            final Date date = new SimpleDateFormat("dd/MM/yyyy").parse(btDate.getText().toString());
            final Date fromTime = sdf.parse(from.getText().toString());
            final Date toTime = sdf.parse(to.getText().toString());

            final Bundle b = getIntent().getExtras();

            String url = localhost + "/swp49x-ffrs/match/reserve-time-slot";
            queue = NetworkController.getInstance(this).getRequestQueue();
            Map<String, Object> params = new HashMap<>();
            params.put("date", new SimpleDateFormat("dd-MM-yyyy").format(date));
            params.put("endTime", to.getText().toString());
            params.put("fieldOwnerId", id);
            params.put("fieldTypeId", 1);
            params.put("startTime", from.getText().toString());
            params.put("userId", b.getInt("user_id"));
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response != null) {
                                try {
                                    Intent intent = new Intent(FieldTimeActivity.this, FieldDetailActivity.class);
                                    intent.putExtra("field_id", id);
                                    intent.putExtra("field_name", name);
                                    intent.putExtra("field_address", address);
                                    intent.putExtra("image_url", imageUrl);
                                    intent.putExtra("date", date);
                                    intent.putExtra("time_from", fromTime);
                                    intent.putExtra("time_to", toTime);
                                    intent.putExtra("price", response.getInt("price"));
                                    intent.putExtra("user_id", b.getInt("user_id"));
                                    intent.putExtra("time_slot_id", response.getInt("id"));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Log.d("EXCEPTION", e.getMessage());
                                }
                            } else {
                                Toast.makeText(FieldTimeActivity.this, "Không thể đặt sân!", Toast.LENGTH_SHORT).show();
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
        } catch (ParseException e) {
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "Hãy nhập giờ chơi!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickShowMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("field_id", id);
        startActivity(intent);
    }

    public void clearData() {
        fieldTimeList.clear();
        adapter.notifyDataSetChanged();
    }

    public void loadFieldTimes() {
        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FieldTimeAdapter(this, fieldTimeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();

        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            FieldTime fieldTime = new FieldTime(sdf.format(sdf.parse(obj.getString("startTime"))), sdf.format(sdf.parse(obj.getString("endTime"))));

                            // adding movie to movies array
                            fieldTimeList.add(fieldTime);

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.getMessage());
                        } finally {
                            //Notify adapter about data changes\
                            Collections.sort(fieldTimeList, new Comparator<FieldTime>() {
                                @Override
                                public int compare(FieldTime o1, FieldTime o2) {
                                    try {
                                        return sdf.parse(o1.getFromTime()).compareTo(sdf.parse(o2.getFromTime()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });
                            adapter.notifyItemChanged(i);
                        }
                    }
                } else {
                    txtNotFound.setVisibility(View.VISIBLE);
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
