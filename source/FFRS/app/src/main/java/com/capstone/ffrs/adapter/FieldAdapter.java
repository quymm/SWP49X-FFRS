package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.capstone.ffrs.FieldSuggestActivity;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.entity.Field;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;

import java.util.List;

/**
 * Created by AndroidNovice on 6/5/2016.
 */
public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.MyViewHolder> {

    private List<Field> fieldList;
    private Context context;
    private LayoutInflater inflater;

    public FieldAdapter(Context context, List<Field> fieldList) {

        this.context = context;
        this.fieldList = fieldList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.field_item, parent, false);
        MyViewHolder holder = new MyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Field field = fieldList.get(position);

        holder.title.setText(field.getFieldName());
        holder.content.setText(field.getAddress());
        holder.setImageURL(field.getImgURL());
        holder.imageview.setImageUrl(field.getImgURL(), NetworkController.getInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView content, title;
        private NetworkImageView imageview;
        private String image_url;

        public void setImageURL(String url) {
            image_url = url;
        }

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            content = (TextView) itemView.findViewById(R.id.content_view);
            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FieldTimeActivity.class);
                    intent.putExtra("field_name", title.getText());
                    intent.putExtra("field_address", content.getText());
                    if (image_url.isEmpty()) {
                        image_url = "http://bongda.phanmemvang.com.vn/wp-content/uploads/2015/03/lan2chaoluanganhgnhe-1-e1426212803227.jpg";
                    }
                    intent.putExtra("image_url", image_url);
                    context.startActivity(intent);
                }
            });
        }
    }
}