package com.capstone.ffrs.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
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
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FavoriteField;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by HuanPMSE61860 on 11/12/2017.
 */

public class FavoriteFieldAdapter extends RecyclerView.Adapter<FavoriteFieldAdapter.FavoriteFieldViewHolder> {

    private List<FavoriteField> favoriteFieldList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;

    public FavoriteFieldAdapter(Context context, List<FavoriteField> favoriteFieldList) {

        this.context = context;
        this.favoriteFieldList = favoriteFieldList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public FavoriteFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.favorite_field_item, parent, false);
        return new FavoriteFieldViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FavoriteFieldViewHolder holder, int position) {
        FavoriteField fieldOwner = favoriteFieldList.get(position);

        holder.itemView.setTag(R.id.card_view, fieldOwner);
        holder.title.setText(fieldOwner.getFieldName());
        holder.content.setText(fieldOwner.getAddress());
    }

    @Override
    public int getItemCount() {
        return favoriteFieldList.size();
    }

    public class FavoriteFieldViewHolder extends RecyclerView.ViewHolder {

        private TextView content, title;
        private Button btDelete;

        public FavoriteFieldViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            content = (TextView) itemView.findViewById(R.id.content_view);
            btDelete = (Button) itemView.findViewById(R.id.btDelete);

            btDelete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final FavoriteField field = (FavoriteField) itemView.getTag(R.id.card_view);
                                                final int favoriteId = field.getId();
                                                AlertDialog.Builder builder;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                                                } else {
                                                    builder = new AlertDialog.Builder(context);
                                                }
                                                builder.setTitle("Xóa danh sách")
                                                        .setMessage("Bạn có muốn xóa sân này ra khỏi danh sách không?")
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // continue with delete
                                                                RequestQueue queue = NetworkController.getInstance(context).getRequestQueue();
                                                                String url = HostURLUtils.getInstance(context).getHostURL() + context.getResources().getString(R.string.url_remove_favorite_field);
                                                                url = String.format(url, favoriteId);
                                                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        Toast.makeText(context, "Bạn đã xóa sân khỏi danh sách", Toast.LENGTH_SHORT).show();

                                                                        favoriteFieldList.remove(field);
                                                                        notifyDataSetChanged();
                                                                        if (favoriteFieldList.isEmpty()) {
                                                                            TextView txtNotFound = (TextView) ((Activity) context).findViewById(R.id.text_not_found);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FieldTimeActivity.class);
                    FavoriteField field = (FavoriteField) itemView.getTag(R.id.card_view);
                    intent.putExtra("field_id", field.getFieldId());
                    intent.putExtra("field_name", title.getText());
                    intent.putExtra("field_address", content.getText());
                    intent.putExtra("user_id", userId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
