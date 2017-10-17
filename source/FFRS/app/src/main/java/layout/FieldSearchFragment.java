package layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.R;
import com.capstone.ffrs.adapter.FieldAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.Field;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FieldSearchFragment extends Fragment {

    private String url;
    private String localhost;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<Field> fieldList = new ArrayList<Field>();
    private FieldAdapter adapter;
    private TextView txtNotFound;
    private EditText edit_text;

    private LatLng currentPosition = null;

    public FieldSearchFragment() {
        // Required empty public constructor
    }

    public BroadcastReceiver filterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean flag = intent.getBooleanExtra("empty_list", false);
            if (flag) {
                txtNotFound.setText("Không có sân nào chứa từ '" + edit_text.getText().toString() + "'");
                txtNotFound.setVisibility(View.VISIBLE);
            } else {
                txtNotFound.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        localhost = getResources().getString(R.string.local_host);
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_field_search, container, false);

        edit_text = (EditText) view.findViewById(R.id.edit_text);
        edit_text.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!v.getText().toString().isEmpty()) {
                        searchFieldByName(view, v.getText().toString());
                    }
                    else {
                        loadFields(view);
                    }
//                    adapter.getFilter().filter(v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                if (currentPosition == null) {
                    Log.d("LOCATION", location.getLatitude() + ";" + location.getLongitude());
                    currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    loadFields(view);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {

        } else {

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 3, locationListener);
            }

            if (isGPSEnabled) {
                if (currentPosition == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 3, locationListener);
                }
            }
        }

//        TextWatcher watcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
//        edit_text.addTextChangedListener(watcher);

        txtNotFound = (TextView) view.findViewById(R.id.text_not_found);
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(filterReceiver,
                new IntentFilter("search-message"));

        return view;
    }

    public void clearData() {
        fieldList.clear();
        adapter.notifyDataSetChanged();
    }

    public void loadFields(View view) {
        if (!fieldList.isEmpty()) {
            clearData();
        }
        Bundle b = getActivity().getIntent().getExtras();
        //Initialize RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new FieldAdapter(this.getContext(), fieldList);
        adapter.setUserId(b.getInt("user_id"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        url = localhost + "/swp49x-ffrs/account/top-10-field-owner?longitude=%f&latitude=%f";
        url = String.format(url, currentPosition.longitude, currentPosition.latitude);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray body = response.getJSONArray("body");
                    if (body != null && body.length() > 0) {
                        for (int i = 0; i < body.length(); i++) {
                            try {
                                JSONObject obj = body.getJSONObject(i);
                                JSONObject profile = obj.getJSONObject("profileId");
                                Field field = new Field(profile.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));

                                // adding movie to movies array
                                fieldList.add(field);

                            } catch (Exception e) {
                                Log.d("EXCEPTION", e.getMessage());
                            } finally {
                                //Notify adapter about data changes
                                adapter.notifyItemChanged(i);
                            }
                        }
                    } else {
                        txtNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

        };
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    public void searchFieldByName(View view, String searchValue) {
        if (!fieldList.isEmpty()) {
            clearData();
        }
        Bundle b = getActivity().getIntent().getExtras();
        //Initialize RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new FieldAdapter(this.getContext(), fieldList);
        adapter.setUserId(b.getInt("user_id"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        url = localhost + "/swp49x-ffrs/account/name?name=%s&role=%s";
        url = String.format(url, searchValue, "owner");

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray body = response.getJSONArray("body");
                    if (body != null && body.length() > 0) {
                        for (int i = 0; i < body.length(); i++) {
                            try {
                                JSONObject obj = body.getJSONObject(i);
                                JSONObject profile = obj.getJSONObject("profileId");
                                Field field = new Field(profile.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));

                                // adding movie to movies array
                                fieldList.add(field);

                            } catch (Exception e) {
                                Log.d("EXCEPTION", e.getMessage());
                            } finally {
                                //Notify adapter about data changes
                                adapter.notifyItemChanged(i);
                            }
                        }
                    } else {
                        txtNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

        };
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }
}
