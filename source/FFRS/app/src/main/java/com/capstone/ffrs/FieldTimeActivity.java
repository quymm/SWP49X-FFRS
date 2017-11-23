package com.capstone.ffrs;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.capstone.ffrs.adapter.FieldTimeAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldTime;
import com.capstone.ffrs.utils.HostURLUtils;
import com.capstone.ffrs.utils.TimePickerListener;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
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
import java.util.Locale;
import java.util.Map;

public class FieldTimeActivity extends AppCompatActivity {

    private String url;
    private String hostURL;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<FieldTime> fieldTimeList = new ArrayList<FieldTime>();
    private FieldTimeAdapter adapter;

    private String name, address;
    int id, favoriteFieldId;

    private String displayFormat = "dd/MM/yyyy";
    private String serverFormat = "dd-MM-yyyy";

    private ProgressBar progressBar;
    private TextView txtNotFound;
    private Spinner spinner;
    private Button date;
    private TimePickerListener startTimeListener, endTimeListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    boolean isEnable = false;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fromTime = intent.getStringExtra("from");
            String toTime = intent.getStringExtra("to");
            EditText from = (EditText) findViewById(R.id.text_from);
            EditText to = (EditText) findViewById(R.id.text_to);
            from.setText(fromTime);
            to.setText(toTime);

            Toast toast = Toast.makeText(FieldTimeActivity.this, "Khung giờ bạn chọn đã được tự động điền", Toast.LENGTH_LONG);
            toast.show();
            toggleButton();
        }
    };

    public BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            toggleButton();
        }
    };

    private void validateTime(EditText from, EditText to) {
        boolean flag = false;
        if (!from.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                Date startTime = sdf.parse(from.getText().toString());

                if (endTimeListener == null) {
                    endTimeListener = new TimePickerListener(FieldTimeActivity.this, to);
                    to.setOnClickListener(endTimeListener);
                }

                for (FieldTime fieldTime : fieldTimeList) {
                    Date fromFrame = sdf.parse(fieldTime.getFromTime());
                    Date toFrame = sdf.parse(fieldTime.getToTime());
                    boolean isOptimal = fieldTime.isOptimal();
                    if (isOptimal) {
                        if (fromFrame.compareTo(startTime) <= 0 && toFrame.compareTo(startTime) > 0) {
                            startTime = fromFrame;
                            from.setText(new SimpleDateFormat("H:mm").format(startTime));
                            to.setText(new SimpleDateFormat("H:mm").format(toFrame));
                            endTimeListener = null;
                            Toast.makeText(this, "Khung giờ cố định", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (fromFrame.compareTo(startTime) <= 0 && toFrame.compareTo(startTime) > 0) {
                            LocalDateTime time = new LocalDateTime(startTime);
                            time = time.plusMinutes(60);
                            startTime = time.toDate();
                            endTimeListener.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString()));
                            endTimeListener.setMinTime(new LocalDateTime(startTime).toDate());
                            endTimeListener.setMaxTime(toFrame);
                            flag = true;
                            break;
                        }
                    }
                }

                if (!flag) {
                    endTimeListener = null;
                    to.setOnClickListener(null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleButton() {
        EditText from = (EditText) findViewById(R.id.text_from);
        EditText to = (EditText) findViewById(R.id.text_to);
        validateTime(from, to);
        Button btReserve = (Button) findViewById(R.id.btReserve);
        if (!from.getText().toString().isEmpty() && !to.getText().toString().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            try {
                Date startTime = sdf.parse(from.getText().toString());
                Date endTime = sdf.parse(to.getText().toString());
                if (((endTime.getTime() - startTime.getTime()) / 60000) >= 60) {
                    boolean flag = false;
                    for (FieldTime time : fieldTimeList) {
                        Date startFrameTime = sdf.parse(time.getFromTime());
                        Date endFrameTime = sdf.parse(time.getToTime());
                        boolean isOptimal = time.isOptimal();
                        if (isOptimal) {
                            if (startTime.compareTo(startFrameTime) >= 0 && endTime.compareTo(endFrameTime) <= 0) {
                                flag = true;
                            }
                        } else if (startTime.compareTo(startFrameTime) >= 0 && endTime.compareTo(endFrameTime) <= 0) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        btReserve.setEnabled(true);
                        btReserve.setBackgroundColor(Color.parseColor("#009632"));
                    } else {
                        btReserve.setEnabled(false);
                        btReserve.setBackgroundColor(Color.parseColor("#dbdbdb"));
                        from.setText("");
                        to.setText("");
                    }
                } else {
                    btReserve.setEnabled(false);
                    btReserve.setBackgroundColor(Color.parseColor("#dbdbdb"));
                    to.setText("");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            btReserve.setEnabled(false);
            btReserve.setBackgroundColor(Color.parseColor("#dbdbdb"));
            to.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_time);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtNotFound = (TextView) findViewById(R.id.text_no_free_time);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addSpinner();

        if (savedInstanceState == null) {
            Bundle b = getIntent().getExtras();

            name = b.getString("field_name");
            TextView txtName = (TextView) findViewById(R.id.field_name);
            txtName.setText(name);

            address = b.getString("field_address");
            TextView txtAddress = (TextView) findViewById(R.id.field_address);
            txtAddress.setText(address);

            id = b.getInt("field_id");

        } else {
            name = savedInstanceState.getString("field_name");
            TextView txtName = (TextView) findViewById(R.id.field_name);
            txtName.setText(name);

            address = savedInstanceState.getString("field_address");
            TextView txtAddress = (TextView) findViewById(R.id.field_address);
            txtAddress.setText(address);

            id = savedInstanceState.getInt("field_id");
        }
        date = (Button) findViewById(R.id.date_picker);
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);

        long currentMillis = System.currentTimeMillis();
        String strCurrentDate = sdf.format(currentMillis);
        date.setText(strCurrentDate);

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

                url = hostURL + getResources().getString(R.string.url_get_free_time);
                url = String.format(url, id, (spinner.getSelectedItemPosition() + 1), sdf.format(dateSelected.getTime()));

                progressBar.setVisibility(View.VISIBLE);

                if (!fieldTimeList.isEmpty()) {
                    clearData();
                } else {
                    txtNotFound.setVisibility(View.GONE);
                }
                loadFieldTimes();
            }

        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FieldTimeActivity.this, R.style.DatepickerCalendarTheme, datePickerListener, dateSelected
                        .get(Calendar.YEAR), dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                LocalDate maxDate = new LocalDate(System.currentTimeMillis() - 1000);
                maxDate = maxDate.plusDays(7);
                dialog.getDatePicker().setMaxDate(maxDate.toDate().getTime());
                dialog.show();
            }
        });

        final EditText from = (EditText) findViewById(R.id.text_from);
        startTimeListener = new TimePickerListener(this, from) {
            @Override
            public void onClick(View v) {
                LocalDate localDate = LocalDate.parse(date.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy"));
                setDate(localDate.toDate());
                if (localDate.isEqual(LocalDate.now())) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    if (minutes > 0 && minutes < 30) {
                        minutes = 30;
                        calendar.set(Calendar.MINUTE, minutes);
                    } else if (minutes > 30 && minutes <= 59) {
                        minutes = 0;
                        calendar.set(Calendar.MINUTE, minutes);
                        if (hour < 23) {
                            hour += 1;
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                        } else {
                            hour = 0;
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                        }
                    }
                    setMinTime(calendar.getTime());
                }
                super.onClick(v);
            }
        };

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fieldTimeList.isEmpty()) {
                    txtNotFound.setVisibility(View.GONE);
                }
                loadFieldTimes();
            }
        });

        final ImageButton ButtonStar = (ImageButton) findViewById(R.id.favorite);
        ButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle b = getIntent().getExtras();
                if (isEnable) {
                    url = hostURL + getResources().getString(R.string.url_remove_favorite_field);
                    url = String.format(url, favoriteFieldId);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_off));
                            Toast.makeText(FieldTimeActivity.this, "Bạn đã xóa sân ra khỏi danh sách yêu thích", Toast.LENGTH_LONG).show();
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
                } else {
                    url = hostURL + getResources().getString(R.string.url_add_favorite_field);
                    url = String.format(url, b.getInt("user_id"), b.getInt("field_id"));
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                favoriteFieldId = response.getJSONObject("body").getInt("id");
                                ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_on));
                                Toast.makeText(FieldTimeActivity.this, "Bạn đã thêm sân vào danh sách yêu thích", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                isEnable = !isEnable;
            }
        });
        checkFavoriteField();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("field_name", name);
        outState.putString("field_address", address);
        outState.putInt("field_id", id);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText from = (EditText) findViewById(R.id.text_from);
        from.setText("");
        EditText to = (EditText) findViewById(R.id.text_to);
        to.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        LocalBroadcastManager.getInstance(this).registerReceiver(timeReceiver,
                new IntentFilter("time-picker-message"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(timeReceiver);
    }

    public void addSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.field_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long rowId) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
                    Date dateObj = sdf.parse(date.getText().toString());
                    sdf = new SimpleDateFormat(serverFormat);
                    url = hostURL + getResources().getString(R.string.url_get_free_time);
                    url = String.format(url, id, (position + 1), sdf.format(dateObj));
                    progressBar.setVisibility(View.VISIBLE);
                    if (!fieldTimeList.isEmpty()) {
                        clearData();
                    } else {
                        txtNotFound.setVisibility(View.GONE);
                    }
                    loadFieldTimes();
                } catch (ParseException e) {
                    Log.d("PARSE_EXCEPTION", e.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        try {
            final Date date = new SimpleDateFormat("dd/MM/yyyy").parse(btDate.getText().toString());
            final Date fromTime = sdf.parse(from.getText().toString());
            final Date toTime = sdf.parse(to.getText().toString());

            final Bundle b = getIntent().getExtras();

            String url = hostURL + getResources().getString(R.string.url_reserve_time_slot);
            queue = NetworkController.getInstance(this).getRequestQueue();
            Map<String, Object> params = new HashMap<>();
            params.put("date", new SimpleDateFormat("dd-MM-yyyy").format(date));
            params.put("endTime", to.getText().toString());
            params.put("fieldOwnerId", id);
            params.put("fieldTypeId", (spinner.getSelectedItemPosition() + 1));
            params.put("startTime", from.getText().toString());
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!response.isNull("body")) {
                                    JSONObject body = response.getJSONObject("body");
                                    if (body != null && body.length() > 0) {
                                        try {
                                            Intent intent = new Intent(FieldTimeActivity.this, FieldDetailActivity.class);
                                            intent.putExtra("field_id", id);
                                            intent.putExtra("field_name", name);
                                            intent.putExtra("field_address", address);
                                            intent.putExtra("field_type_id", spinner.getSelectedItemPosition() + 1);
                                            intent.putExtra("date", date);
                                            intent.putExtra("time_from", fromTime);
                                            intent.putExtra("time_to", toTime);
                                            intent.putExtra("price", body.getInt("price"));
                                            intent.putExtra("user_id", b.getInt("user_id"));
                                            intent.putExtra("tour_match_mode", false);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            Log.d("EXCEPTION", e.getMessage());
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(FieldTimeActivity.this, "Không thể đặt sân!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(FieldTimeActivity.this, "Không còn sân trống!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    loadFieldTimes();
                                }
                            } catch (JSONException e) {
                                Log.d("ParseException", e.getMessage());
                            } finally {
                                toggleButton();
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
            Toast.makeText(getApplicationContext(), "Hãy nhập giờ đá!", Toast.LENGTH_SHORT).show();
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
        if (fieldTimeList != null) {
            fieldTimeList.clear();
        }
        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FieldTimeAdapter(this, fieldTimeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();

        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

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

                                    Date startTime = sdf.parse(obj.getString("startTime"));
                                    LocalDate localDate = LocalDate.parse(date.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy"));
                                    if (localDate.isEqual(LocalDate.now())) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.SECOND, 0);
                                        calendar.set(Calendar.MILLISECOND, 0);
                                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                        int minutes = calendar.get(Calendar.MINUTE);
                                        if (minutes > 0 && minutes < 30) {
                                            minutes = 30;
                                            calendar.set(Calendar.MINUTE, minutes);
                                        } else if (minutes > 30 && minutes <= 59) {
                                            minutes = 0;
                                            calendar.set(Calendar.MINUTE, minutes);
                                            if (hour < 23) {
                                                hour += 1;
                                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                            } else {
                                                hour = 0;
                                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                                            }
                                        }
                                        LocalTime localStartTime = LocalTime.fromDateFields(startTime);
                                        LocalTime localEndTime = LocalTime.fromDateFields(sdf.parse(obj.getString("endTime")));
                                        LocalTime localCurrentTime = LocalTime.fromCalendarFields(calendar);
                                        if (localCurrentTime.isAfter(localStartTime)) {
                                            startTime = localCurrentTime.toDateTimeToday().toDate();
                                        }

                                        if (localEndTime.toDateTimeToday().getMillis() - localCurrentTime.toDateTimeToday().getMillis() >= (60 * 60 * 1000)) {
                                            FieldTime fieldTime = new FieldTime(sdf.format(startTime), sdf.format(localEndTime.toDateTimeToday().toDate()));
                                            fieldTime.setOptimal(obj.getBoolean("optimal"));
                                            // adding movie to movies array
                                            fieldTimeList.add(fieldTime);
                                        }
                                    } else {
                                        FieldTime fieldTime = new FieldTime(sdf.format(startTime), sdf.format(sdf.parse(obj.getString("endTime"))));
                                        fieldTime.setOptimal(obj.getBoolean("optimal"));
                                        // adding movie to movies array
                                        fieldTimeList.add(fieldTime);
                                    }

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
                            if (!fieldTimeList.isEmpty()) {
                                Date endFrame = sdf.parse(fieldTimeList.get(fieldTimeList.size() - 1).getToTime());
                                LocalDateTime time = new LocalDateTime(endFrame);
                                time = time.minusMinutes(60);
                                endFrame = time.toDate();
                                startTimeListener.setMinTime(sdf.parse(fieldTimeList.get(0).getFromTime()));
                                startTimeListener.setMaxTime(endFrame);
                                final EditText from = (EditText) findViewById(R.id.text_from);
                                from.setOnClickListener(startTimeListener);
                            } else {
                                txtNotFound.setVisibility(View.VISIBLE);
                                final EditText from = (EditText) findViewById(R.id.text_from);
                                final EditText to = (EditText) findViewById(R.id.text_to);
                                from.setOnClickListener(null);
                                to.setOnClickListener(null);
                            }
                        } else {
                            txtNotFound.setVisibility(View.VISIBLE);
                            final EditText from = (EditText) findViewById(R.id.text_from);
                            final EditText to = (EditText) findViewById(R.id.text_to);
                            from.setOnClickListener(null);
                            to.setOnClickListener(null);
                        }
                    }
                    toggleButton();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
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

    public void onClickGoBackToHome(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void checkFavoriteField() {
        final Bundle b = getIntent().getExtras();
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        final ImageButton ButtonStar = (ImageButton) findViewById(R.id.favorite);
        url = hostURL + getResources().getString(R.string.url_get_favorite_fields);
        url = String.format(url, b.getInt("user_id"));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        for (int i = 0; i < body.length(); i++) {
                            JSONObject item = body.getJSONObject(i);
                            int fieldId = item.getJSONObject("fieldOwnerId").getInt("id");
                            if (fieldId == b.getInt("field_id")) {
                                ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_on));
                                isEnable = true;
                                favoriteFieldId = item.getInt("id");
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
