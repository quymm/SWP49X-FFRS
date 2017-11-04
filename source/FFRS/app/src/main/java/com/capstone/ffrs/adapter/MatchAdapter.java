package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.MatchRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/11/2017.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private List<MatchRequest> matchList;
    private Context context;
    private LayoutInflater inflater;

    public MatchAdapter(Context context, List<MatchRequest> matchList) {
        this.matchList = matchList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MatchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.match_item, parent, false);
        return new MatchAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MatchAdapter.MyViewHolder holder, int position) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            MatchRequest item = matchList.get(position);

            holder.teamName.setText(item.getTeamName());

            holder.date.setText(sdf.format(new Date(Long.parseLong(item.getDate()))));

            sdf = new SimpleDateFormat("HH:mm:ss");

            Date startTime = sdf.parse(item.getStartTime());
            Date endTime = sdf.parse(item.getEndTime());

            sdf = new SimpleDateFormat("H:mm");
            holder.time.setText(sdf.format(startTime) + " - " + sdf.format(endTime));

            holder.ratingScore.setText(item.getRatingScore() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView teamName, time, date, ratingScore;

        public MyViewHolder(final View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.team_view);
            time = (TextView) itemView.findViewById(R.id.time_view);
            date = (TextView) itemView.findViewById(R.id.date_view);
            ratingScore = (TextView) itemView.findViewById(R.id.rating_score);
        }
    }
}
