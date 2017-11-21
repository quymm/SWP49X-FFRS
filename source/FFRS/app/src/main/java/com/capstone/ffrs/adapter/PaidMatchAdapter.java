package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.capstone.ffrs.R;
import com.capstone.ffrs.ReservationInfoActivity;
import com.capstone.ffrs.entity.PaidFriendlyMatch;
import com.capstone.ffrs.entity.PaidTourMatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/30/2017.
 */

public class PaidMatchAdapter extends RecyclerView.Adapter<PaidMatchAdapter.PaidRequestViewHolder> {

    private List<Object> requestList;
    private Context context;
    private LayoutInflater inflater;

    public PaidMatchAdapter(Context context, List<Object> requestList) {

        this.context = context;
        this.requestList = requestList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PaidRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.paid_match_item, parent, false);
        return new PaidMatchAdapter.PaidRequestViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PaidRequestViewHolder holder, int position) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Object request = requestList.get(position);
            holder.itemView.setTag(R.id.card_view, request);
            if (request instanceof PaidFriendlyMatch) {
                PaidFriendlyMatch friendlyMatchRequest = (PaidFriendlyMatch) request;
                holder.txtContent.setText("Đặt sân đá riêng");
                holder.txtField.setText(friendlyMatchRequest.getFieldName());

                String strDate = sdf.format(new Date(Long.parseLong(friendlyMatchRequest.getDate())));
                holder.txtDate.setText(strDate);

                sdf = new SimpleDateFormat("HH:mm:ss");

                Date startTime = sdf.parse(friendlyMatchRequest.getStartTime());
                Date endTime = sdf.parse(friendlyMatchRequest.getEndTime());

                sdf = new SimpleDateFormat("H:mm");
                holder.txtTime.setText(sdf.format(startTime) + " - " + sdf.format(endTime));

                String strStartDateTime = strDate + " " + sdf.format(startTime);
                String strEndDateTime = strDate + " " + sdf.format(endTime);
                sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");
                Date startDate = sdf.parse(strStartDateTime);
                Date endDate = sdf.parse(strEndDateTime);
                Date currentDate = new Date();

                if (currentDate.compareTo(startDate) < 0) {
                    holder.txtStatus.setText("Sắp tới");
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
                } else if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) < 0) {
                    holder.txtStatus.setText("Đang diễn ra");
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                } else if (currentDate.compareTo(endDate) >= 0) {
                    holder.txtStatus.setText("Đã xong");
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
            } else if (request instanceof PaidTourMatch) {
                PaidTourMatch tourMatchRequest = (PaidTourMatch) request;
                holder.txtContent.setText("Đá với đội " + tourMatchRequest.getTeamName());
                holder.txtField.setText(tourMatchRequest.getFieldName());

                String strDate = sdf.format(new Date(Long.parseLong(tourMatchRequest.getDate())));
                holder.txtDate.setText(strDate);

                sdf = new SimpleDateFormat("HH:mm:ss");

                Date startTime = sdf.parse(tourMatchRequest.getStartTime());
                Date endTime = sdf.parse(tourMatchRequest.getEndTime());

                sdf = new SimpleDateFormat("H:mm");
                holder.txtTime.setText(sdf.format(startTime) + " - " + sdf.format(endTime));

                String strStartDateTime = strDate + " " + sdf.format(startTime);
                String strEndDateTime = strDate + " " + sdf.format(endTime);
                sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");
                Date startDate = sdf.parse(strStartDateTime);
                Date endDate = sdf.parse(strEndDateTime);
                Date currentDate = new Date();

                if (currentDate.compareTo(startDate) < 0) {
                    holder.txtStatus.setText("Sắp tới");
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
                } else if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) < 0) {
                    holder.txtStatus.setText("Đang diễn ra");
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                } else if (currentDate.compareTo(endDate) >= 0) {
                    holder.txtStatus.setText("Đã xong");
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class PaidRequestViewHolder extends RecyclerView.ViewHolder {

        private TextView txtContent, txtField, txtTime, txtDate, txtStatus;
        private RelativeLayout background;

        public PaidRequestViewHolder(final View itemView) {
            super(itemView);
            txtField = (TextView) itemView.findViewById(R.id.field_view);
            txtContent = (TextView) itemView.findViewById(R.id.content_view);
            txtTime = (TextView) itemView.findViewById(R.id.time_view);
            txtDate = (TextView) itemView.findViewById(R.id.date_view);
            txtStatus = (TextView) itemView.findViewById(R.id.status_view);
            background = (RelativeLayout) itemView.findViewById(R.id.content_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object request = itemView.getTag(R.id.card_view);
                    if (request instanceof PaidFriendlyMatch) {
                        Intent intent = new Intent(context, ReservationInfoActivity.class);
                        PaidFriendlyMatch friendlyMatchRequest = (PaidFriendlyMatch) request;
                        intent.putExtra("id", friendlyMatchRequest.getId());
                        intent.putExtra("user_id", friendlyMatchRequest.getUserId());
                        intent.putExtra("field_id", friendlyMatchRequest.getFieldId());
                        intent.putExtra("field_type_id", friendlyMatchRequest.getFieldTypeId());
                        intent.putExtra("date", friendlyMatchRequest.getDate());
                        intent.putExtra("start_time", friendlyMatchRequest.getStartTime());
                        intent.putExtra("end_time", friendlyMatchRequest.getEndTime());
                        intent.putExtra("field_name", friendlyMatchRequest.getFieldName());
                        intent.putExtra("tour_match_mode", false);
                        intent.putExtra("status", txtStatus.getText().toString());
                        context.startActivity(intent);
                    } else if (request instanceof PaidTourMatch) {
                        Intent intent = new Intent(context, ReservationInfoActivity.class);
                        PaidTourMatch tourMatchRequest = (PaidTourMatch) request;
                        intent.putExtra("id", tourMatchRequest.getId());
                        intent.putExtra("user_id", tourMatchRequest.getUserId());
                        intent.putExtra("opponent_id", tourMatchRequest.getOpponentId());
                        intent.putExtra("field_id", tourMatchRequest.getFieldId());
                        intent.putExtra("field_type_id", tourMatchRequest.getFieldTypeId());
                        intent.putExtra("tour_match_id", tourMatchRequest.getTourMatchId());
                        intent.putExtra("date", tourMatchRequest.getDate());
                        intent.putExtra("start_time", tourMatchRequest.getStartTime());
                        intent.putExtra("end_time", tourMatchRequest.getEndTime());
                        intent.putExtra("opponent_team_name", tourMatchRequest.getTeamName());
                        intent.putExtra("field_name", tourMatchRequest.getFieldName());
                        intent.putExtra("tour_match_mode", true);
                        intent.putExtra("status", txtStatus.getText().toString());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}