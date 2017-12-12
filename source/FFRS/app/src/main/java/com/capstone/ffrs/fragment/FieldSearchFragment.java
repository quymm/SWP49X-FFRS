package com.capstone.ffrs.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.R;
import com.capstone.ffrs.adapter.FieldAdapter;
import com.capstone.ffrs.adapter.PromotionAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwner;
import com.capstone.ffrs.entity.Promotion;
import com.capstone.ffrs.utils.HostURLUtils;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldSearchFragment extends Fragment {

    private String url;
    private String hostURL;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<FieldOwner> fieldOwnerList = new ArrayList<FieldOwner>();
    private List<Promotion> promotionList = new ArrayList<Promotion>();
    private FieldAdapter fieldAdapter;
    private PromotionAdapter promotionAdapter;
    private TextView txtNotFound;
    private EditText searchText;
    private Activity mActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView btnClose;
    private ImageButton btMode;

    private Location currentLocation;

    private int currentSpinnerPosition = 0;
    private boolean searchMode = false;
    private boolean viewMode = true;

    private SharedPreferences sharedPreferences;

    public BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentLocation = new Location("dummy");
            currentLocation.setLatitude(intent.getDoubleExtra("latitude", -1));
            currentLocation.setLongitude(intent.getDoubleExtra("longitude", -1));

            if (getView() != null && currentLocation.getLatitude() != -1 && currentLocation.getLongitude() != -1
                    && !searchMode) {
                loadFields();
            }
        }
    };

    public FieldSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hostURL = HostURLUtils.getInstance(getContext()).getHostURL();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_field_search, container, false);

        btnClose = (ImageView) view.findViewById(R.id.btSearchClose);
        btnClose.setVisibility(View.GONE);

        searchText = (EditText) view.findViewById(R.id.edit_text);
        searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchText.setCursorVisible(false);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setCursorVisible(true);
                if (!searchText.getText().toString().isEmpty()) {
                    btnClose.setVisibility(View.VISIBLE);
                }
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    InputMethodManager inputManager = (InputMethodManager)
                            mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    txtNotFound.setVisibility(View.GONE);
                    if (!v.getText().toString().isEmpty()) {
                        searchMode = true;
                        swipeRefreshLayout.setRefreshing(true);
                        searchFieldByName(v.getText().toString());
                    } else {
                        searchMode = false;
                        swipeRefreshLayout.setRefreshing(true);
                        loadFields();
                    }
                    btnClose.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchText.getText().toString().isEmpty()) {
                    btnClose.setVisibility(View.GONE);
                    txtNotFound.setVisibility(View.GONE);
                    searchMode = false;
                } else {
                    btnClose.setVisibility(View.VISIBLE);
                    searchMode = true;
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.getText().clear();
                swipeRefreshLayout.setRefreshing(true);
                loadFields();
            }
        });

        txtNotFound = (TextView) view.findViewById(R.id.text_not_found);

        btMode = (ImageButton) view.findViewById(R.id.btMode);
        btMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Tìm sân trong vòng 5 km");
                list.add("Tìm khuyến mãi");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                final View view = getLayoutInflater().inflate(R.layout.field_list_spinner, null);

                final Spinner mSpinner = (Spinner) view
                        .findViewById(R.id.field_spinner);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
                mSpinner.setAdapter(adapter);
                mSpinner.setSelection(currentSpinnerPosition);

                alertDialogBuilder.setView(view);

                alertDialogBuilder.setTitle("Chọn chế độ tìm kiếm");

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentSpinnerPosition = mSpinner.getSelectedItemPosition();
                        if (currentSpinnerPosition == 0){
                            viewMode = true;
                            txtNotFound.setText("Không có sân gần vị trí của bạn");
                        } else {
                            viewMode = false;
                            txtNotFound.setText("Không có khuyến mãi nào đang diễn ra");
                        }
                        if (!searchMode) {
                            swipeRefreshLayout.setRefreshing(true);
                            loadFields();
                        }
                    }
                });

                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!searchText.getText().toString().isEmpty()) {
                    searchMode = true;
                    searchFieldByName(searchText.getText().toString());
                } else {
                    searchMode = false;
                    loadFields();
                }
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        mActivity = getActivity();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        //Initialize RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        fieldAdapter = new FieldAdapter(view.getContext(), fieldOwnerList);
        fieldAdapter.setUserId(sharedPreferences.getInt("user_id", -1));

        promotionAdapter = new PromotionAdapter(getContext(), promotionList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(fieldAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(locationReceiver,
                new IntentFilter("location-message"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(locationReceiver);
    }

    public void loadFields() {
        if (currentLocation == null) {
            return;
        }

        if (!fieldOwnerList.isEmpty()) {
            fieldOwnerList.clear();
            fieldAdapter.notifyDataSetChanged();
        }

        if (!promotionList.isEmpty()) {
            promotionList.clear();
            promotionAdapter.notifyDataSetChanged();
        }

        if (viewMode) {
            url = hostURL + getResources().getString(R.string.url_get_nearby_field);
            url = String.format(url, currentLocation.getLongitude(), currentLocation.getLatitude());
            //Initialize RecyclerView
            recyclerView.setAdapter(fieldAdapter);

            //Getting Instance of Volley Request Queue
            queue = NetworkController.getInstance(getContext()).getRequestQueue();
            //Volley's inbuilt class to make Json array request
            JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (!response.isNull("body")) {
                            JSONArray body = response.getJSONArray("body");
                            if (body != null && body.length() > 0) {
                                if (!fieldOwnerList.isEmpty()) {
                                    fieldOwnerList.clear();
                                    fieldAdapter.notifyDataSetChanged();
                                }

                                for (int i = 0; i < body.length(); i++) {
                                    try {
                                        JSONObject obj = body.getJSONObject(i);
                                        JSONObject profile = obj.getJSONObject("profileId");
                                        FieldOwner fieldOwner = new FieldOwner(obj.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));

                                        // adding movie to movies array
                                        fieldOwnerList.add(fieldOwner);

                                    } catch (Exception e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    } finally {
                                        //Notify adapter about data changes
                                        fieldAdapter.notifyItemChanged(i);
                                    }
                                }
                            } else {
                                txtNotFound.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (getContext() != null) {
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
                        swipeRefreshLayout.setRefreshing(false);
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
            //Adding JsonArrayRequest to Request Queue
            queue.add(newsReq);
        } else {
            url = hostURL + getResources().getString(R.string.url_get_promotions);
            //Initialize RecyclerView
            recyclerView.setAdapter(promotionAdapter);

            //Getting Instance of Volley Request Queue
            queue = NetworkController.getInstance(getContext()).getRequestQueue();
            //Volley's inbuilt class to make Json array request
            JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (!response.isNull("body")) {
                            JSONArray body = response.getJSONArray("body");
                            if (body != null && body.length() > 0) {
                                if (!promotionList.isEmpty()) {
                                    promotionList.clear();
                                    promotionAdapter.notifyDataSetChanged();
                                }

                                for (int i = 0; i < body.length(); i++) {
                                    try {
                                        JSONObject obj = body.getJSONObject(i);
                                        JSONObject field = obj.getJSONObject("fieldOwnerId");
                                        JSONObject profile = field.getJSONObject("profileId");
                                        Promotion promotion = new Promotion();
                                        promotion.setFreeServices(obj.getString("freeServices"));
                                        promotion.setSaleOff(obj.getInt("saleOff"));
                                        promotion.setStartDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(obj.getLong("dateFrom"))));
                                        promotion.setStartTime(new SimpleDateFormat("H:mm").format(new SimpleDateFormat("H:mm:ss").parse(obj.getString("startTime"))));
                                        promotion.setEndDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(obj.getLong("dateTo"))));
                                        promotion.setEndTime(new SimpleDateFormat("H:mm").format(new SimpleDateFormat("H:mm:ss").parse(obj.getString("endTime"))));
                                        FieldOwner fieldOwner = new FieldOwner(field.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));
                                        promotion.setField(fieldOwner);
                                        // adding movie to movies array
                                        promotionList.add(promotion);
                                    } catch (Exception e) {
                                        Log.d("EXCEPTION", e.getMessage());
                                    } finally {
                                        //Notify adapter about data changes
                                        promotionAdapter.notifyItemChanged(i);
                                    }
                                }
                            } else {
                                txtNotFound.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (getContext() != null) {
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
                        swipeRefreshLayout.setRefreshing(false);
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
            //Adding JsonArrayRequest to Request Queue
            queue.add(newsReq);
        }
    }

    public void searchFieldByName(String searchValue) {
        if (!fieldOwnerList.isEmpty()) {
            fieldOwnerList.clear();
            fieldAdapter.notifyDataSetChanged();
        }

        if (!promotionList.isEmpty()) {
            promotionList.clear();
            promotionAdapter.notifyDataSetChanged();
        }
        url = hostURL + getResources().getString(R.string.url_get_field_by_name);
        try {
            url = String.format(url, URLEncoder.encode(searchValue, "utf-8"), "owner");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(fieldAdapter);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonObjectRequest newsReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!fieldOwnerList.isEmpty()) {
                        fieldOwnerList.clear();
                        fieldAdapter.notifyDataSetChanged();
                    }

                    if (!promotionList.isEmpty()) {
                        promotionList.clear();
                        promotionAdapter.notifyDataSetChanged();
                    }
                    if (!response.isNull("body")) {
                        JSONArray body = response.getJSONArray("body");
                        if (body != null && body.length() > 0) {
                            txtNotFound.setVisibility(View.GONE);
                            for (int i = 0; i < body.length(); i++) {
                                try {
                                    JSONObject obj = body.getJSONObject(i);
                                    JSONObject profile = obj.getJSONObject("profileId");
                                    FieldOwner fieldOwner = new FieldOwner(obj.getInt("id"), profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));

                                    // adding movie to movies array
                                    fieldOwnerList.add(fieldOwner);

                                } catch (Exception e) {
                                    Log.d("EXCEPTION", e.getMessage());
                                } finally {
                                    //Notify adapter about data changes
                                    fieldAdapter.notifyItemChanged(i);
                                }
                            }
                        } else {
                            txtNotFound.setText("Không tìm thấy sân phù hợp");
                            txtNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
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
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

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
