package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.FieldTime;

import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/2/2017.
 */

public class FieldTimeAdapter extends RecyclerView.Adapter<FieldTimeAdapter.FieldTimeViewHolder> {

    private List<FieldTime> timeList;
    private Context context;
    private LayoutInflater inflater;

    public FieldTimeAdapter(Context context, List<FieldTime> timeList) {

        this.context = context;
        this.timeList = timeList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public FieldTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.field_time_item, parent, false);
        return new FieldTimeAdapter.FieldTimeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FieldTimeViewHolder holder, int position) {
        FieldTime item = timeList.get(position);

        holder.title.setText(item.getFromTime() + " - " + item.getToTime());
        Log.d("boolean", item.isOptimal()+"");
        if (item.isOptimal()) {
            holder.title.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            holder.view.setBackgroundColor(Color.parseColor("#00600d"));
        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class FieldTimeViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private LinearLayout view;

        public FieldTimeViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.time_frame);
            view = (LinearLayout) itemView.findViewById(R.id.field_time_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("custom-message");
                    String[] hours = title.getText().toString().split(" - ");
                    intent.putExtra("from", hours[0]);
                    intent.putExtra("to", hours[1]);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }
            });
        }
    }

}
