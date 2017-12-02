package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.TeamMemberInfo;

import java.util.List;

/**
 * Created by HuanPMSE61860 on 11/23/2017.
 */

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder> {

    private List<TeamMemberInfo> memberList;
    private Context context;
    private LayoutInflater inflater;

    public TeamMemberAdapter(Context context, List<TeamMemberInfo> memberList) {

        this.context = context;
        this.memberList = memberList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TeamMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.team_member_item, parent, false);
        return new TeamMemberViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TeamMemberViewHolder holder, int position) {
        TeamMemberInfo item = memberList.get(position);
        holder.txtName.setText(item.getName());
        holder.txtPhone.setText(item.getPhone());
        holder.txtAddress.setText(item.getAddress());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class TeamMemberViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtPhone, txtAddress;
        private Button btClose;

        public TeamMemberViewHolder(final View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.text_member_name);
            txtPhone = (TextView) itemView.findViewById(R.id.text_phone);
            txtAddress = (TextView) itemView.findViewById(R.id.text_address);

            btClose = (Button) itemView.findViewById(R.id.btClose);
            btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
