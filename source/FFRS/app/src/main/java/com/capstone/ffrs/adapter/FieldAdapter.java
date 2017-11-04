package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.entity.FieldOwner;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;

import java.util.List;

/**
 * Created by AndroidNovice on 6/5/2016.
 */
public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.MyViewHolder> {

    private List<FieldOwner> fieldOwnerList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;

    public FieldAdapter(Context context, List<FieldOwner> fieldOwnerList) {

        this.context = context;
        this.fieldOwnerList = fieldOwnerList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.field_item, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FieldOwner fieldOwner = fieldOwnerList.get(position);

        holder.itemView.setTag(R.id.card_view, fieldOwner.getId());
        holder.title.setText(fieldOwner.getFieldName());
        holder.content.setText(fieldOwner.getAddress());
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.imageview.setVisibility(View.GONE);
        holder.imageview.setDefaultImageResId(R.drawable.img_placeholder);
        holder.imageview.setErrorImageResId(R.drawable.img_broken);
        String url = fieldOwner.getImgURL();
        ImageLoader imageLoader = NetworkController.getInstance(context).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                holder.progressBar.setVisibility(View.GONE);
                holder.imageview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageview.setImageBitmap(response.getBitmap());
                    holder.imageview.setVisibility(View.VISIBLE);
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageview.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.imageview.setImageUrl(url, imageLoader);

    }

    @Override
    public int getItemCount() {
        return fieldOwnerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView content, title;
        private NetworkImageView imageview;
        private ProgressBar progressBar;

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            content = (TextView) itemView.findViewById(R.id.content_view);
            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FieldTimeActivity.class);
                    int id = (int) itemView.getTag(R.id.card_view);
                    intent.putExtra("field_id", id);
                    intent.putExtra("field_name", title.getText());
                    intent.putExtra("field_address", content.getText());
                    intent.putExtra("user_id", userId);
                    context.startActivity(intent);
                }
            });
        }
    }
}