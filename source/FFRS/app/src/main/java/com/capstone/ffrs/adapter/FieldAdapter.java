package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.FieldOwner;

import java.util.List;

/**
 * Created by AndroidNovice on 6/5/2016.
 */
public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

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
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.field_item, parent, false);
        return new FieldViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {
        FieldOwner fieldOwner = fieldOwnerList.get(position);

        holder.itemView.setTag(R.id.card_view, fieldOwner.getId());
        holder.title.setText(fieldOwner.getFieldName());
        holder.content.setText(fieldOwner.getAddress());
        String url = fieldOwner.getImgURL();
        loadImageWithGlide(holder.imageview, url);
    }

    public void loadImageWithGlide(ImageView imageView, String url) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url);
        requestBuilder.error(Glide.with(context).load(context.getResources().getDrawable(R.drawable.img_placeholder)));
        requestBuilder.into(imageView);
    }

    @Override
    public int getItemCount() {
        return fieldOwnerList.size();
    }

    public class FieldViewHolder extends RecyclerView.ViewHolder {

        private TextView content, title;
        private ImageView imageview;

        public FieldViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            content = (TextView) itemView.findViewById(R.id.content_view);
            imageview = (ImageView) itemView.findViewById(R.id.thumbnail);

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