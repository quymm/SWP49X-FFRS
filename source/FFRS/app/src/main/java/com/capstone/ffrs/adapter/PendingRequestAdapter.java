package com.capstone.ffrs.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.capstone.ffrs.R;
import com.capstone.ffrs.RequestInfoActivity;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.PendingRequest;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HuanPMSE61860 on 11/4/2017.
 */

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.PendingRequestViewHolder> {
    private List<PendingRequest> requestList;
    private Context context;
    private LayoutInflater inflater;

    public PendingRequestAdapter(Context context, List<PendingRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PendingRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.pending_request_item, parent, false);
        return new PendingRequestAdapter.PendingRequestViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PendingRequestViewHolder holder, int position) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            PendingRequest request = requestList.get(position);
            holder.itemView.setTag(R.id.card_view, request);

            String strDate = sdf.format(new Date(Long.parseLong(request.getDate())));
            holder.txtDate.setText(strDate);

            sdf = new SimpleDateFormat("HH:mm:ss");

            Date startTime = sdf.parse(request.getStartTime());
            Date endTime = sdf.parse(request.getEndTime());

            sdf = new SimpleDateFormat("H:mm");
            holder.txtTime.setText((position + 1) + ". " + sdf.format(startTime) + " - " + sdf.format(endTime));

            int duration = request.getDuration();
            String strDuration = "";
            if (duration / 60 > 0) {
                strDuration += (duration / 60) + " tiếng";
            }
            if (duration % 60 == 30) {
                strDuration += " " + (duration % 60) + " phút";
            }
            holder.txtDuration.setText(strDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class PendingRequestViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTime, txtDate, txtDuration;

        private Button btClose;

        public PendingRequestViewHolder(final View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.time_view);
            txtDate = (TextView) itemView.findViewById(R.id.date_view);
            txtDuration = (TextView) itemView.findViewById(R.id.duration_view);
            btClose = (Button) itemView.findViewById(R.id.btClose);

            btClose.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               AlertDialog.Builder builder;
                                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                   builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                                               } else {
                                                   builder = new AlertDialog.Builder(context);
                                               }
                                               builder.setTitle("Hủy yêu cầu")
                                                       .setMessage("Bạn có muốn hủy bỏ yêu cầu này không?")
                                                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               // continue with delete
                                                               final PendingRequest request = (PendingRequest) itemView.getTag(R.id.card_view);

                                                               RequestQueue queue = NetworkController.getInstance(context).getRequestQueue();
                                                               String url = HostURLUtils.getInstance(context).getHostURL() + context.getResources().getString(R.string.url_cancel_matching_request);
                                                               url = String.format(url, request.getMatchingRequestId());
                                                               JsonObjectRequest cancelRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                                                                   @Override
                                                                   public void onResponse(JSONObject response) {
                                                                       Toast.makeText(context, "Bạn đã hủy yêu cầu đá chung", Toast.LENGTH_SHORT).show();
                                                                       requestList.remove(request);
                                                                       notifyDataSetChanged();
                                                                       if (requestList.isEmpty()) {
                                                                           TextView txtNotFound = (TextView) ((Activity) context).findViewById(R.id.text_not_found_pending_request);
                                                                           txtNotFound.setVisibility(View.VISIBLE);
                                                                       }
                                                                   }
                                                               }, new Response.ErrorListener() {
                                                                   @Override
                                                                   public void onErrorResponse(VolleyError error) {
                                                                       if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                                           Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                                                       } else if (error instanceof AuthFailureError) {
                                                                           Toast.makeText(context, "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                                                                       } else if (error instanceof ServerError) {
                                                                           Toast.makeText(context, "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                                                                       } else if (error instanceof NetworkError) {
                                                                           Toast.makeText(context, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                                                                       } else if (error instanceof ParseError) {
                                                                           Toast.makeText(context, "Lỗi parse!", Toast.LENGTH_SHORT).show();
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
                                                               queue.add(cancelRequest);
                                                           }
                                                       })
                                                       .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               // do nothing
                                                           }
                                                       })
                                                       .show();
                                           }
                                       }
            );

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PendingRequest request = (PendingRequest) itemView.getTag(R.id.card_view);
                    Intent intent = new Intent(context, RequestInfoActivity.class);
                    intent.putExtra("id", request.getMatchingRequestId());
                    intent.putExtra("field_type_id", request.getFieldTypeId());
                    intent.putExtra("duration", request.getDuration());
                    intent.putExtra("date", request.getDate());
                    intent.putExtra("start_time", request.getStartTime());
                    intent.putExtra("end_time", request.getEndTime());
                    intent.putExtra("latitude", request.getLatitude());
                    intent.putExtra("longitude", request.getLongitude());
                    intent.putExtra("address", request.getAddress());
                    context.startActivity(intent);

                }
            });
        }
    }
}

