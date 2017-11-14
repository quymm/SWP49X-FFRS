package com.capstone.ffrs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.HistoryActivity;
import com.capstone.ffrs.R;

public class ReserveSuccessFragment extends Fragment {

    private Button btHome;

    public ReserveSuccessFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reserve_success, container, false);
        btHome = (Button) view.findViewById(R.id.btHome);
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getActivity().getIntent().getExtras();
                boolean tourMatchMode = b.getBoolean("tour_match_mode");
                if (!tourMatchMode) {
                    Intent intent = new Intent(getContext(), FieldTimeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), HistoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        Bundle b = getActivity().getIntent().getExtras();
        TextView code = (TextView) view.findViewById(R.id.text_code);
        code.setText("Mã đặt sân: " + b.getInt("reserve_id"));
        return view;
    }
}
