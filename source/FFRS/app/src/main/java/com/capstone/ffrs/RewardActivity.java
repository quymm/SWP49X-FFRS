package com.capstone.ffrs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayout grid = (GridLayout) findViewById(R.id.grid_layout);

        LinearLayout testLayout = new LinearLayout(this.getBaseContext());

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(5, 5, 5, 5); // llp.setMargins(left, top, right, bottom);

        TextView title = new TextView(this.getBaseContext());
        title.setWidth(width / 2 - 14);
        title.setText("What?");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setLayoutParams(llp);
        testLayout.addView(title);

        TextView title2 = new TextView(this.getBaseContext());
        title2.setWidth(width / 2 - 14);
        title2.setText("What?");
        title2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title2.setLayoutParams(llp);
        testLayout.addView(title2);

        grid.addView(testLayout);
    }

}
