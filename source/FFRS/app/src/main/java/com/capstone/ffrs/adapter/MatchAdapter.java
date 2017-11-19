package com.capstone.ffrs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.capstone.ffrs.R;
import com.capstone.ffrs.RequestTimeActivity;
import com.capstone.ffrs.entity.MatchRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HuanPMSE61860 on 10/11/2017.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<MatchRequest> matchList;
    private Context context;
    private LayoutInflater inflater;

    public MatchAdapter(Context context, List<MatchRequest> matchList) {
        this.matchList = matchList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.match_item, parent, false);
        return new MatchAdapter.MatchViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
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

            RequestBuilder<Drawable> drawable = Glide.with(context).load(item.getImageURL());
            drawable.error(Glide.with(context).load(context.getResources().getDrawable(R.drawable.people)));
            drawable.into(holder.imageView);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        private TextView teamName, time, date, duration, ratingScore;
        private CircleImageView imageView;

        public MatchViewHolder(final View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.team_view);
            time = (TextView) itemView.findViewById(R.id.time_view);
            date = (TextView) itemView.findViewById(R.id.date_view);
            duration = (TextView) itemView.findViewById(R.id.duration_view);
            ratingScore = (TextView) itemView.findViewById(R.id.rating_score);
            imageView = (CircleImageView) itemView.findViewById(R.id.thumbnail);
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

                        try {
                            Date requestStartTime = new SimpleDateFormat("H:mm:ss").parse(request.getStartTime());
                            Date requestEndTime = new SimpleDateFormat("H:mm:ss").parse(request.getEndTime());
                            Date sendStartTime = new SimpleDateFormat("H:mm").parse(b.getString("field_start_time"));
                            Date sendEndTime = new SimpleDateFormat("H:mm").parse(b.getString("field_end_time"));

                            String strStartTime = request.getStartTime();
                            String strEndTime = request.getEndTime();

                            if (sendStartTime.getTime() - requestStartTime.getTime() > 0) {
                                strStartTime = b.getString("field_start_time") + ":00";
                            }
                            if (sendEndTime.getTime() - requestEndTime.getTime() < 0) {
                                strEndTime = b.getString("field_end_time") + ":00";
                            }
                            Intent intent = new Intent(context, RequestTimeActivity.class);
                            intent.putExtra("field_type_id", b.getInt("field_type_id"));
                            intent.putExtra("field_start_time", strStartTime);
                            intent.putExtra("field_end_time", strEndTime);
                            intent.putExtra("latitude", b.getDouble("latitude"));
                            intent.putExtra("longitude", b.getDouble("longitude"));
                            intent.putExtra("date", request.getDate());
                            intent.putExtra("matching_request_id", request.getId());
                            intent.putExtra("opponent_id", request.getUserId());
                            intent.putExtra("duration", b.getInt("duration"));
                            intent.putExtra("distance", b.getInt("distance"));
                            intent.putExtra("priorityField", b.getBoolean("priorityField"));
                            context.startActivity(intent);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }
}
