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
import com.capstone.ffrs.entity.Notification;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/16/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Notification> notificationList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.MyViewHolder(rootView);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Notification item = notificationList.get(position);

        holder.itemView.setTag(R.id.card_view, item.getRequestId());
        holder.itemView.setTag(R.id.notification_text, item.getUserId());
        holder.notificationText.setText(item.getTeamName() + " đã yêu cầu chơi với bạn");

        holder.date.setText("Ngày: " + sdf.format(item.getDate()));

        sdf = new SimpleDateFormat("H:mm");
        holder.time.setText("Thời gian: " + sdf.format(item.getStartTime()) + " - " + sdf.format(item.getEndTime()));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView notificationText, time, date;

        public MyViewHolder(final View itemView) {
            super(itemView);
            notificationText = (TextView) itemView.findViewById(R.id.notification_text);
            time = (TextView) itemView.findViewById(R.id.time_view);
            date = (TextView) itemView.findViewById(R.id.date_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
