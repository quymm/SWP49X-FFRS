package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.Match;

import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/11/2017.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private List<Match> matchList;
    private Context context;
    private LayoutInflater inflater;

    @Override
    public MatchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.field_time_item, parent, false);
        return new MatchAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MatchAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.time_frame);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("custom-message");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    String[] hours = title.getText().toString().split(" - ");
                    intent.putExtra("from", hours[0]);
                    intent.putExtra("to", hours[1]);
                }
            });
        }
    }
}
