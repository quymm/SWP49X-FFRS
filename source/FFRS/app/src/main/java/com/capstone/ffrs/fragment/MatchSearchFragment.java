package com.capstone.ffrs.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.CreateRequestResultActivity;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.MatchResultActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.RechargeActivity;
import com.capstone.ffrs.adapter.PlacesAutoCompleteAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;
import com.capstone.ffrs.utils.TimePickerListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MatchSearchFragment extends Fragment {

    private String displayFormat = "dd/MM/yyyy";
    private Spinner fieldSpinner, durationSpinner, distanceSpinner;
    private LatLng currentPosition = null;
    private LatLng customPosition = null;
    private Button btFindRequest, btCreateRequest;
    private String currentAddress = "";

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
                if (startTime.compareTo(endTime) <= 0) {
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), R.style.DatepickerCalendarTheme, datePickerListener, dateSelected
                        .get(Calendar.YEAR), dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                LocalDate maxDate = new LocalDate(System.currentTimeMillis() - 1000);
                maxDate = maxDate.plusDays(7);
                dialog.getDatePicker().setMaxDate(maxDate.toDate().getTime());
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

        TimePickerListener startListener = new TimePickerListener(view.getContext(), from) {
            @Override
            public void onClick(View v) {
                LocalDate localDate = LocalDate.parse(mDate.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy"));
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
                    setDate(localDate.toDate());
                    setMinTime(calendar.getTime());
                } else {
                    setMinTime(null);
                }
                super.onClick(v);
            }
        };
        try {
            startListener.setMaxTime(new SimpleDateFormat("H:mm").parse("22:30"));
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
                        setDate(new SimpleDateFormat("dd/MM/yyyy").parse(mDate.getText().toString()));
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
//        txtAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
//                try {
//                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                            .setCountry("VN")
//                            .build();
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
//                                    .build(getActivity());
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO: Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                }
//            }
//        });
        txtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!v.getText().toString().isEmpty()) {
                        try {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getActivity(), Locale.forLanguageTag("vi-VN"));

                            addresses = geocoder.getFromLocationName(v.getText().toString(), 1);

                            if (!addresses.isEmpty()) {
                                customPosition = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                                currentAddress = v.getText().toString();
                            } else {
                                customPosition = null;
                                txtAddress.setText("");
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getContext());
                                }
                                builder.setTitle("Không tìm thấy địa chỉ phù hợp")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setCancelable(false).show();
                                //Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                            }

                            validate();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        customPosition = null;
                    }
                }
                return false;
            }
        });
        final PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.place_autocomplete_list_item);
        txtAddress.setAdapter(adapter);
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
                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        customPosition = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        currentAddress = str;
                    } else {
                        customPosition = null;
                        txtAddress.setText("");
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }
                        builder.setTitle("Không tìm thấy địa chỉ phù hợp")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setCancelable(false).show();
//                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(v.getContext(), MatchResultActivity.class);
                intent.putExtra("field_type_id", (fieldSpinner.getSelectedItemPosition() + 1));
                intent.putExtra("field_date", mDate.getText().toString());
                intent.putExtra("field_start_time", from.getText().toString());
                intent.putExtra("field_end_time", to.getText().toString());
                intent.putExtra("duration", 60 + durationSpinner.getSelectedItemPosition() * 30);
                intent.putExtra("distance", 4 + (distanceSpinner.getSelectedItemPosition() * 2));
                intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
                if (txtAddress.getText().toString().isEmpty() || customPosition == null) {
                    intent.putExtra("address", txtAddress.getHint().toString());
                    intent.putExtra("latitude", currentPosition.latitude);
                    intent.putExtra("longitude", currentPosition.longitude);
                } else {
                    intent.putExtra("address", txtAddress.getText().toString());
                    intent.putExtra("latitude", customPosition.latitude);
                    intent.putExtra("longitude", customPosition.longitude);
                }
                intent.putExtra("createMode", false);
                startActivity(intent);
            }
        });

        btCreateRequest = (Button) view.findViewById(R.id.btCreateRequest);
        btCreateRequest.setEnabled(false);
        btCreateRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        btCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                String url = HostURLUtils.getInstance(getContext()).getHostURL() + getResources().getString(R.string.url_create_matching_request);

                RequestQueue queue = NetworkController.getInstance(getActivity()).getRequestQueue();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = sdf.parse(mDate.getText().toString());
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate = sdf.format(date);

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("date", strDate);
                    params.put("userId", sharedPreferences.getInt("user_id", -1));
                    params.put("startTime", from.getText().toString());
                    params.put("endTime", to.getText().toString());
                    params.put("duration", 60 + durationSpinner.getSelectedItemPosition() * 30);
                    params.put("expectedDistance", 4 + distanceSpinner.getSelectedItemPosition() * 2);
                    params.put("priorityField", true);
                    params.put("fieldTypeId", (fieldSpinner.getSelectedItemPosition() + 1));
                    if (txtAddress.getText().toString().isEmpty() || customPosition == null) {
                        params.put("address", txtAddress.getHint().toString());
                        params.put("latitude", currentPosition.latitude);
                        params.put("longitude", currentPosition.longitude);
                    } else {
                        params.put("address", txtAddress.getText().toString());
                        params.put("latitude", customPosition.latitude);
                        params.put("longitude", customPosition.longitude);
                    }
                    JsonObjectRequest createRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (!response.isNull("body")) {
//                                            JSONObject body = response.getJSONObject("body");
//                                            final int matchingRequestId = body.getInt("matchingRequestId");
//                                            JSONArray list = body.getJSONArray("similarMatchingRequestList");
//                                            if (list.length() > 0) {
//                                                AlertDialog.Builder builder;
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
//                                                } else {
//                                                    builder = new AlertDialog.Builder(getContext());
//                                                }
//                                                builder.setTitle("Tìm thấy đối thủ")
//                                                        .setMessage("Chúng tôi đã tìm thấy đối thủ phù hợp với bạn. Bạn có muốn xem danh sách đối thủ không?")
//                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
//                                                                Intent intent = new Intent(v.getContext(), MatchResultActivity.class);
//                                                                intent.putExtra("field_type_id", (fieldSpinner.getSelectedItemPosition() + 1));
//                                                                intent.putExtra("field_date", mDate.getText().toString());
//                                                                intent.putExtra("field_start_time", from.getText().toString());
//                                                                intent.putExtra("field_end_time", to.getText().toString());
//                                                                intent.putExtra("duration", 60 + durationSpinner.getSelectedItemPosition() * 30);
//                                                                intent.putExtra("distance", 4 + (distanceSpinner.getSelectedItemPosition() * 2));
//                                                                intent.putExtra("priorityField", true);
//                                                                intent.putExtra("created_matching_request_id", matchingRequestId);
//                                                                intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
//                                                                if (txtAddress.getText().toString().isEmpty() || customPosition == null) {
//                                                                    intent.putExtra("address", txtAddress.getHint().toString());
//                                                                    intent.putExtra("latitude", currentPosition.latitude);
//                                                                    intent.putExtra("longitude", currentPosition.longitude);
//                                                                } else {
//                                                                    intent.putExtra("address", txtAddress.getText().toString());
//                                                                    intent.putExtra("latitude", customPosition.latitude);
//                                                                    intent.putExtra("longitude", customPosition.longitude);
//                                                                }
//                                                                intent.putExtra("createMode", true);
//                                                                startActivity(intent);
//                                                            }
//                                                        })
//                                                        .setNegativeButton("Không, tạo mới yêu cầu", new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                Intent intent = new Intent(getActivity(), CreateRequestResultActivity.class);
//                                                                intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
//                                                                intent.putExtra("message", "Bạn đã tạo yêu cầu tìm đối thủ thành công!");
//                                                                startActivity(intent);
//                                                            }
//                                                        }).setCancelable(false).show();
//                                            } else {
//                                                Intent intent = new Intent(getActivity(), CreateRequestResultActivity.class);
//                                                intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
//                                                intent.putExtra("message", "Bạn đã tạo yêu cầu tìm đối thủ thành công!");
//                                                startActivity(intent);
//                                            }
                                            JSONObject body = response.getJSONObject("body");
                                            JSONArray list = body.getJSONArray("similarMatchingRequestList");
                                            if (list.length() > 0) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference myRef = database.getReference();
                                                for (int i = 0; i < list.length(); i++) {
                                                    JSONObject item = list.getJSONObject(i);
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("status", 0);
                                                    map.put("numberOfMatches", list.length());
                                                    myRef.child("matchingRequest").child(item.getJSONObject("userId").getInt("id") + "").child(item.getInt("id") + "").setValue(map);
                                                }
                                                Map<String, Object> map = new HashMap<>();
                                                map.put("status", 0);
                                                map.put("numberOfMatches", list.length());
                                                myRef.child("matchingRequest").child(sharedPreferences.getInt("user_id", -1) + "").child(body.getInt("matchingRequestId") + "").setValue(map);
                                            }
                                            Intent intent = new Intent(getActivity(), CreateRequestResultActivity.class);
                                            intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
                                            intent.putExtra("message", "Bạn đã tạo yêu cầu tìm đối thủ thành công!");
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && (error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 400)) {
                                try {
                                    String strResponse = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject response = new JSONObject(strResponse);
                                    if (response.getString("message").equals("User not have enough money to create request!")) {
                                        AlertDialog.Builder builder;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(getContext());
                                        }
                                        builder.setTitle("Tài khoản không đủ tiền để tạo yêu cầu.")
                                                .setPositiveButton("Nạp tiền", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(getContext(), RechargeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                }).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");

                            return headers;
                        }
                    };
                    queue.add(createRequest);
                } catch (ParseException e) {
                    Log.d("Parse_Exception", e.getMessage());
                }
            }
        });

        addFieldSpinner(view);

        addDurationSpinner(view);

        addDistanceSpinner(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(locationReceiver,
                new IntentFilter("location-message"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(timepickerReceiver,
                new IntentFilter("time-picker-message"));
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
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.duration_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(dataAdapter);
    }

    public void addDistanceSpinner(View view) {
        distanceSpinner = (Spinner) view.findViewById(R.id.spDistance);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.distance_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(dataAdapter);
    }
}