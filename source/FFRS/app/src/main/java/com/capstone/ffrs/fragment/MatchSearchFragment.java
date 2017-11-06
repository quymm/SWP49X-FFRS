package com.capstone.ffrs.fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.CreateMatchingRequestActivity;
import com.capstone.ffrs.MatchActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.adapter.PlacesAutoCompleteAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.TimePickerListener;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MatchSearchFragment extends Fragment {

    private String displayFormat = "dd/MM/yyyy";
    private Spinner fieldSpinner, durationSpinner;
    private LatLng currentPosition = null;
    private LatLng customPosition = null;
    private Button btFindRequest, btCreateRequest;

    public MatchSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AutoCompleteTextView txtAddress = (AutoCompleteTextView) getView().findViewById(R.id.input_address);
            if (txtAddress.getText().toString().isEmpty()) {
                double latitude = intent.getDoubleExtra("latitude", -1);
                double longitude = intent.getDoubleExtra("longitude", -1);
                if (latitude != -1 && longitude != -1) {
                    currentPosition = new LatLng(latitude, longitude);
                } else {
                    currentPosition = null;
                }
                if (currentPosition != null) {
                    btFindRequest.setText("Tìm đối thủ");
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(context, Locale.forLanguageTag("vi_VN"));
                    try {
                        addresses = geocoder.getFromLocation(currentPosition.latitude, currentPosition.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        txtAddress.setHint(address);
                        validate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public BroadcastReceiver timepickerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            validate();
        }
    };

    public void validate() {
        EditText from = (EditText) getActivity().findViewById(R.id.input_start_time);
        EditText to = (EditText) getActivity().findViewById(R.id.input_end_time);
        if (currentPosition == null && customPosition == null) {
            btFindRequest.setEnabled(false);
            btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
            btCreateRequest.setEnabled(false);
            btCreateRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        } else if (!from.getText().toString().isEmpty() && !to.getText().toString().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            try {
                Date startTime = sdf.parse(from.getText().toString());
                Date endTime = sdf.parse(to.getText().toString());
                if (startTime.compareTo(endTime) < 0) {
                    long duration = (endTime.getTime() - startTime.getTime()) / (1000 * 60);
                    if (duration >= (60 + durationSpinner.getSelectedItemPosition() * 30)) {
                        btFindRequest.setEnabled(true);
                        btFindRequest.setBackgroundColor(Color.parseColor("#009632"));
                        btCreateRequest.setEnabled(true);
                        btCreateRequest.setBackgroundColor(Color.parseColor("#009632"));
                    } else {
                        to.setText("");
                        btFindRequest.setEnabled(false);
                        btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
                        btCreateRequest.setEnabled(false);
                        btCreateRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
                    }
                } else {
                    btFindRequest.setEnabled(false);
                    btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
                    btCreateRequest.setEnabled(false);
                    btCreateRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            btCreateRequest.setEnabled(false);
            btCreateRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_search, container, false);

        final EditText mDate = (EditText) view.findViewById(R.id.input_date);
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);

        long currentMillis = System.currentTimeMillis();
        String strCurrentDate = sdf.format(currentMillis);
        mDate.setText(strCurrentDate);

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

                mDate.setText(sdf.format(dateSelected.getTime()));
            }

        };

        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), R.style.DatepickerCalendarTheme, datePickerListener, dateSelected
                        .get(Calendar.YEAR), dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        final EditText from = (EditText) view.findViewById(R.id.input_start_time);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes > 0 && minutes < 30) {
            minutes = 30;
        } else if (minutes > 30 && minutes <= 59) {
            minutes = 0;
            if (hour < 23) {
                hour += 1;
            } else {
                hour = 0;
                LocalDateTime dateTime = LocalDateTime.now();
                dateTime = dateTime.plusDays(1);
                mDate.setText(sdf.format(dateTime.toDate()));
            }
        }

        TimePickerListener startListener = new TimePickerListener(view.getContext(), from);
        try {
            startListener.setMaxTime(new SimpleDateFormat("H:mm").parse("23:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        from.setOnClickListener(startListener);
        from.setText(hour + ":" + (minutes == 0 ? "00" : "30"));

        final EditText to = (EditText) view.findViewById(R.id.input_end_time);
        to.setOnClickListener(new TimePickerListener(view.getContext(), to) {
            @Override
            public void onClick(View v) {

                if (!from.getText().toString().isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        setMinTime(new LocalDateTime(sdf.parse(from.getText().toString())).plusMinutes(60 + durationSpinner.getSelectedItemPosition() * 30).toDate());

                        super.onClick(v);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    to.setText("");
                }
            }
        });

        final AutoCompleteTextView txtAddress = (AutoCompleteTextView) view.findViewById(R.id.input_address);
        txtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.forLanguageTag("vi-VN"));

                        addresses = geocoder.getFromLocationName(v.getText().toString(), 1);

                        if (!addresses.isEmpty()) {
                            customPosition = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            Toast.makeText(getActivity(), "Tọa độ: " + addresses.get(0).getLatitude() + ":" + addresses.get(0).getLongitude(), Toast.LENGTH_LONG).show();
                        } else {
                            customPosition = null;
                            Toast.makeText(getActivity(), "Không thế lấy tọa độ từ địa chỉ này", Toast.LENGTH_LONG).show();
                        }

                        validate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        txtAddress.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.place_autocomplete_list_item));
        txtAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                txtAddress.setText(str);
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getActivity(), Locale.forLanguageTag("vi-VN"));

                    addresses = geocoder.getFromLocationName(str, 1);
                    if (!addresses.isEmpty()) {
                        customPosition = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        Toast.makeText(getActivity(), "Tọa độ: " + addresses.get(0).getLatitude() + ":" + addresses.get(0).getLongitude(), Toast.LENGTH_LONG).show();
                    } else {
                        customPosition = null;
                        Toast.makeText(getActivity(), "Không thế lấy tọa độ từ địa chỉ này", Toast.LENGTH_LONG).show();
                    }

                    validate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btFindRequest = (Button) view.findViewById(R.id.btFindRequest);
        btFindRequest.setEnabled(false);
        btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        btFindRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                Intent intent = new Intent(v.getContext(), MatchActivity.class);
                intent.putExtra("field_type_id", (fieldSpinner.getSelectedItemPosition() + 1));
                intent.putExtra("field_date", mDate.getText().toString());
                intent.putExtra("field_start_time", from.getText().toString());
                intent.putExtra("field_end_time", to.getText().toString());
                intent.putExtra("duration", 60 + durationSpinner.getSelectedItemPosition() * 30);
                intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
                if (txtAddress.getText().toString().isEmpty() || customPosition == null) {
                    intent.putExtra("latitude", currentPosition.latitude);
                    intent.putExtra("longitude", currentPosition.longitude);
                } else {
                    intent.putExtra("latitude", customPosition.latitude);
                    intent.putExtra("longitude", customPosition.longitude);
                }
                startActivity(intent);
            }
        });

        btCreateRequest = (Button) view.findViewById(R.id.btCreateRequest);
        btCreateRequest.setEnabled(false);
        btCreateRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        btCreateRequest.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                String url = getResources().getString(R.string.local_host) + getResources().getString(R.string.url_create_matching_request);

                RequestQueue queue = NetworkController.getInstance(getActivity()).getRequestQueue();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = sdf.parse(mDate.getText().toString());
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate = sdf.format(date);

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("address", "ssss");
                    params.put("date", strDate);
                    params.put("userId", sharedPreferences.getInt("user_id", -1));
                    params.put("startTime", from.getText().toString());
                    params.put("endTime", to.getText().toString());
                    params.put("duration", 60 + durationSpinner.getSelectedItemPosition() * 30);
                    params.put("fieldTypeId", (fieldSpinner.getSelectedItemPosition() + 1));
                    if (txtAddress.getText().toString().isEmpty() || customPosition == null) {
                        params.put("latitude", currentPosition.latitude);
                        params.put("longitude", currentPosition.longitude);
                    } else {
                        params.put("latitude", customPosition.latitude);
                        params.put("longitude", customPosition.longitude);
                    }
                    JsonObjectRequest createRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(getActivity(), CreateMatchingRequestActivity.class);
                                    intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.getMessage());
                        }
                    });
                    queue.add(createRequest);
                } catch (ParseException e) {
                    Log.d("Parse_Exception", e.getMessage());
                }
            }
        });

        addFieldSpinner(view);

        durationSpinner = (Spinner) view.findViewById(R.id.spDuration);
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addDurationSpinner(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(locationReceiver,
                new IntentFilter("location-message"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(timepickerReceiver,
                new IntentFilter("timepicker-message"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationReceiver);

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(timepickerReceiver);
    }

    public void addFieldSpinner(View view) {
        fieldSpinner = (Spinner) view.findViewById(R.id.spField);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.field_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(dataAdapter);
    }

    public void addDurationSpinner(View view) {
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.duration_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(dataAdapter);
    }
}