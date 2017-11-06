package com.capstone.ffrs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.ffrs.R;
import com.capstone.ffrs.RequestTimeActivity;
import com.capstone.ffrs.entity.FirebaseUserInfo;
import com.capstone.ffrs.entity.MatchRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

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

            holder.itemView.setTag(R.id.card_view, item);

            holder.teamName.setText(item.getTeamName());

            holder.date.setText(sdf.format(new Date(Long.parseLong(item.getDate()))));

            sdf = new SimpleDateFormat("HH:mm:ss");

            Date startTime = sdf.parse(item.getStartTime());
            Date endTime = sdf.parse(item.getEndTime());

            sdf = new SimpleDateFormat("H:mm");
            holder.time.setText(sdf.format(startTime) + " - " + sdf.format(endTime));

            holder.ratingScore.setText(item.getRatingScore() + "");

            Bundle b = ((Activity) context).getIntent().getExtras();
            int duration = b.getInt("duration");
            String strDuration = "";
            if (duration / 60 > 0) {
                strDuration += (duration / 60) + " tiếng";
            }
            if (duration % 60 == 30) {
                strDuration += " " + (duration % 60) + " phút";
            }
            holder.duration.setText(strDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView teamName, time, date, duration, ratingScore;

        public MyViewHolder(final View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.team_view);
            time = (TextView) itemView.findViewById(R.id.time_view);
            date = (TextView) itemView.findViewById(R.id.date_view);
            duration = (TextView) itemView.findViewById(R.id.duration_view);
            ratingScore = (TextView) itemView.findViewById(R.id.rating_score);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof Activity) {
                        final Bundle b = ((Activity) context).getIntent().getExtras();

                        MatchRequest request = (MatchRequest) v.getTag(R.id.card_view);

//                        // Firebase
//                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference myRef = database.getReference();
//
//                        Integer userId = b.getInt("user_id");
//
//                        FirebaseUserInfo info = new FirebaseUserInfo();
//                        info.setStatus(0);
//                        info.setLatitude(b.getDouble("latitude"));
//                        info.setLongitude(b.getDouble("longitude"));
//                        myRef.child("request").child(request.getUserId() + "").child(request.getId() + "").child(userId.toString()).setValue(info);
                        Intent intent = new Intent(context, RequestTimeActivity.class);
                        intent.putExtra("field_type_id", b.getInt("field_type_id"));
                        intent.putExtra("field_start_time", request.getStartTime());
                        intent.putExtra("field_end_time", request.getEndTime());
                        intent.putExtra("latitude", b.getDouble("latitude"));
                        intent.putExtra("longitude", b.getDouble("longitude"));
                        intent.putExtra("date", request.getDate());
                        intent.putExtra("matching_request_id", request.getId());
                        intent.putExtra("opponent_id", request.getUserId());
                        intent.putExtra("duration", b.getInt("duration"));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
