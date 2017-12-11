package com.capstone.ffrs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.R;
import com.capstone.ffrs.RechargeActivity;
import com.capstone.ffrs.SearchActivity;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONObject;

/**
 * Created by HuanPMSE61860 on 11/7/2017.
 */

public class ReserveNoMoneyFragment extends Fragment {

    private Button btHome, btRecharge;

    public ReserveNoMoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserve_no_money, container, false);
        btHome = (Button) view.findViewById(R.id.btHome);
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btRecharge = (Button) view.findViewById(R.id.btRecharge);
        btRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getActivity().getIntent().getExtras();
                Intent intent = new Intent(getContext(), RechargeActivity.class);
                intent.putExtra("field_id", b.getInt("field_id"));
                intent.putExtra("field_name", b.getString("field_name"));
                intent.putExtra("field_address", b.getString("field_address"));
                intent.putExtra("field_type_id", b.getInt("field_type_id"));
                intent.putExtra("date", b.getSerializable("date"));
                intent.putExtra("time_from", b.getSerializable("time_from"));
                intent.putExtra("time_to", b.getSerializable("time_to"));
                intent.putExtra("price", b.getInt("price"));
                intent.putExtra("user_id", b.getInt("user_id"));
                intent.putExtra("tour_match_mode", b.getBoolean("tour_match_mode"));
                if (b.getBoolean("tour_match_mode")) {
                    intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                    intent.putExtra("opponent_id", b.getInt("opponent_id"));
                }
                intent.putExtra("recharge_for_reservation", true);
                startActivity(intent);
            }
        });
        return view;
    }
}
