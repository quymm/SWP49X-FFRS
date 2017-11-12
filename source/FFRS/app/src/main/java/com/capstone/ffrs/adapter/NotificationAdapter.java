package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.FieldDetailActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.NotificationRequest;
import com.capstone.ffrs.entity.NotificationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 * Created by HuanPMSE61860 on 10/16/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Object> notificationList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;
    private String hostURL;
    private RequestQueue queue;

    public NotificationAdapter(Context context, List<Object> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        hostURL = context.getResources().getString(R.string.local_host);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.MyViewHolder(rootView);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Object item = notificationList.get(position);
        if (item instanceof NotificationRequest) {
            NotificationRequest request = (NotificationRequest) item;
            holder.itemView.setTag(R.id.card_view, item);
            holder.notificationText.setText(request.getTeamName() + " đã yêu cầu đá với bạn");

            holder.date.setText("Ngày: " + sdf.format(request.getDate()));

            sdf = new SimpleDateFormat("H:mm");
            holder.time.setText("Thời gian: " + sdf.format(request.getStartTime()) + " - " + sdf.format(request.getEndTime()));

        } else if (item instanceof NotificationResponse) {
            NotificationResponse response = (NotificationResponse) item;
            holder.itemView.setTag(R.id.card_view, item);
            holder.notificationText.setText(response.getTeamName() + " đã đồng ý.");
            holder.subText.setVisibility(View.VISIBLE);
            holder.subText.setText("Mời bạn thanh toán để tham gia trận đấu.");

            holder.date.setText("Ngày: " + sdf.format(response.getDate()));

            sdf = new SimpleDateFormat("H:mm");
            holder.time.setText("Thời gian: " + sdf.format(response.getStartTime()) + " - " + sdf.format(response.getEndTime()));
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView notificationText, subText, time, date;

        public MyViewHolder(final View itemView) {
            super(itemView);
            notificationText = (TextView) itemView.findViewById(R.id.notification_text);
            subText = (TextView) itemView.findViewById(R.id.notification_sub_text);
            time = (TextView) itemView.findViewById(R.id.time_view);
            date = (TextView) itemView.findViewById(R.id.date_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    final int userId = sharedPreferences.getInt("user_id", -1);
                    if (userId != -1) {
                        Object item = itemView.getTag(R.id.card_view);
                        if (item instanceof NotificationRequest) {
                            final NotificationRequest request = (NotificationRequest) item;

                            final int opponentId = request.getUserId();

                            String url = hostURL + context.getResources().getString(R.string.url_choose_field);
                            url = String.format(url, request.getRequestId(), 5);

                            queue = NetworkController.getInstance(context).getRequestQueue();

                            final Date requestDate = request.getDate();

                            final String strDate = new SimpleDateFormat("dd-MM-yyyy").format(requestDate);

                            Map<String, Object> params = new HashMap<>();
                            params.put("address", "ssss");
                            params.put("date", strDate);
                            params.put("userId", userId);
                            params.put("startTime", request.getStartTime());
                            params.put("endTime", request.getEndTime());
                            params.put("fieldTypeId", request.getFieldTypeId());
                            params.put("latitude", request.getLatitude());
                            params.put("longitude", request.getLongitude());
                            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                JSONObject body = response.getJSONObject("body");
                                                if (body != null && body.length() > 0) {
                                                    try {
                                                        final Date fromTime = new Date(body.getLong("startTime"));
                                                        final Date toTime = new Date(body.getLong("endTime"));
                                                        Intent intent = new Intent(context, FieldDetailActivity.class);
                                                        intent.putExtra("field_id", body.getJSONObject("fieldOwnerId").getInt("id"));
                                                        intent.putExtra("field_name", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("name"));
                                                        intent.putExtra("field_address", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("address"));
                                                        intent.putExtra("field_type_id", body.getJSONObject("fieldTypeId").getInt("id"));
                                                        intent.putExtra("image_url", body.getJSONObject("fieldOwnerId").getJSONObject("profileId").getString("avatarUrl"));
                                                        intent.putExtra("date", requestDate);
                                                        intent.putExtra("time_from", fromTime);
                                                        intent.putExtra("time_to", toTime);
                                                        intent.putExtra("price", body.getInt("price"));
                                                        intent.putExtra("user_id", userId);
                                                        intent.putExtra("time_slot_id", body.getInt("id"));
                                                        intent.putExtra("tour_match_mode", true);
                                                        intent.putExtra("matching_request_id", request.getRequestId());
                                                        intent.putExtra("opponent_id", opponentId);
                                                        context.startActivity(intent);
                                                    } catch (Exception e) {
                                                        Log.d("EXCEPTION", e.getMessage());
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Không thể đặt sân!", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Log.d("ParseException", e.getMessage());
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
                        } else if (item instanceof NotificationResponse) {
                            NotificationResponse response = (NotificationResponse) item;
                            Intent intent = new Intent(context, FieldDetailActivity.class);
                            intent.putExtra("field_id", response.getFieldId());
                            intent.putExtra("field_name", response.getFieldName());
                            intent.putExtra("field_address", response.getFieldAddress());
                            intent.putExtra("field_type_id", response.getFieldTypeId());
                            intent.putExtra("date", response.getDate());
                            intent.putExtra("time_from", response.getStartTime());
                            intent.putExtra("time_to", response.getEndTime());
                            intent.putExtra("price", response.getPrice());
                            intent.putExtra("user_id", response.getUserId());
                            intent.putExtra("time_slot_id", response.getTimeSlotId());
                            intent.putExtra("tour_match_id", response.getTourMatchId());
                            intent.putExtra("tour_match_mode", true);

                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}
