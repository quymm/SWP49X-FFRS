package com.capstone.ffrs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.FieldTime;

import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/2/2017.
 */

public class FieldTimeAdapter extends RecyclerView.Adapter<FieldTimeAdapter.MyViewHolder> {

    private List<FieldTime> timeList;
    private Context context;
    private LayoutInflater inflater;

    public FieldTimeAdapter(Context context, List<FieldTime> timeList) {

        this.context = context;
        this.timeList = timeList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public FieldTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.field_time_item, parent, false);
        return new FieldTimeAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FieldTimeAdapter.MyViewHolder holder, int position) {
        FieldTime item = timeList.get(position);

        holder.title.setText(item.getFromTime() + " - " + item.getToTime());
        holder.price.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView price, title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            price = (TextView) itemView.findViewById(R.id.content_view);
        }
    }

}
