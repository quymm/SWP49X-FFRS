package com.capstone.ffrs.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
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
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.PendingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            holder.txtTime.setText(sdf.format(startTime) + " - " + sdf.format(endTime));

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
                                                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               // continue with delete
                                                               RequestQueue queue = NetworkController.getInstance(context).getRequestQueue();
                                                               String url = context.getResources().getString(R.string.local_host) + context.getResources().getString(R.string.url_cancel_matching_request);
                                                               JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                                                                   @Override
                                                                   public void onResponse(JSONObject response) {
                                                                       Toast.makeText(context, "Bạn đã hủy yêu cầu đá chung", Toast.LENGTH_SHORT).show();
                                                                       // trigger reload BroadcastReceiver
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

                                                               };
                                                               queue.add(request);
                                                           }
                                                       })
                                                       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               // do nothing
                                                           }
                                                       })
                                                       .show();
                                           }
                                       }
            );
        }
    }
}

