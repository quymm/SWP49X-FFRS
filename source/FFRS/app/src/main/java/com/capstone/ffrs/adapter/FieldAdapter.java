package com.capstone.ffrs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
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

        View rootView = inflater.inflate(R.layout.fields_item, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Field field = fieldList.get(position);
//Pass the values of feeds object to Views
        holder.title.setText(field.getFieldName());
        holder.content.setText(field.getAddress());
        holder.imageview.setImageUrl(field.getImgURL(), NetworkController.getInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView content, title;
        private NetworkImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            content = (TextView) itemView.findViewById(R.id.content_view);
// Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnail);

        }
    }

}