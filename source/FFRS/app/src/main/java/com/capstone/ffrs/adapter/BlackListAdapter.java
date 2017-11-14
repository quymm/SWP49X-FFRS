package com.capstone.ffrs.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwner;
import com.capstone.ffrs.entity.OpponentInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by HuanPMSE61860 on 11/12/2017.
 */

public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.BlackListViewHolder> {

    private List<OpponentInfo> blackList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;

    public BlackListAdapter(Context context, List<OpponentInfo> blackList) {

        this.context = context;
        this.blackList = blackList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public BlackListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.black_list_item, parent, false);
        return new BlackListViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BlackListViewHolder holder, int position) {
        OpponentInfo info = blackList.get(position);
        holder.itemView.setTag(R.id.card_view, info.getId());
        holder.txtTeamName.setText(info.getName());
        holder.txtRatingScore.setText("Điểm xếp hạng: " + info.getRatingScore());
    }

    @Override
    public int getItemCount() {
        return blackList.size();
    }

    public class BlackListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtRatingScore, txtTeamName;
        private ImageView imageview;
        private Button btDelete;

        public BlackListViewHolder(final View itemView) {
            super(itemView);
            txtTeamName = (TextView) itemView.findViewById(R.id.team_view);
            txtRatingScore = (TextView) itemView.findViewById(R.id.rating_score);
            btDelete = (Button) itemView.findViewById(R.id.btDelete);

            btDelete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final int blackListId = (int) itemView.getTag(R.id.card_view);
                                                AlertDialog.Builder builder;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                                                } else {
                                                    builder = new AlertDialog.Builder(context);
                                                }
                                                builder.setTitle("Xóa danh sách")
                                                        .setMessage("Bạn có muốn xóa đối thủ này ra khỏi danh sách không?")
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // continue with delete
                                                                RequestQueue queue = NetworkController.getInstance(context).getRequestQueue();
                                                                String url = context.getResources().getString(R.string.local_host) + context.getResources().getString(R.string.url_remove_black_list);
                                                                url = String.format(url, blackListId);
                                                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        Toast.makeText(context, "Bạn đã xóa đối thủ khỏi danh sách", Toast.LENGTH_SHORT).show();
                                                                        // trigger reload BroadcastReceiver
                                                                        Intent intent = new Intent("remove-message");
                                                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
