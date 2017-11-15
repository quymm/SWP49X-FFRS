package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.capstone.ffrs.fragment.RechargeSucessFragment;
import com.capstone.ffrs.fragment.RechargeFailFragment;
import com.capstone.ffrs.fragment.ReserveNoMoneyFragment;
import com.capstone.ffrs.fragment.ReserveSuccessFragment;

public class ReservationResultActivity extends AppCompatActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        String result = b.getString("payment_result");
        switch (result) {
            case "Succeed":
                mFragment = new ReserveSuccessFragment();
                break;
            case "Cancelled":
                mFragment = new RechargeFailFragment();
                break;
            case "Invalid configuration":
                mFragment = new RechargeFailFragment();
                break;
            case "No Money":
                mFragment = new ReserveNoMoneyFragment();
                break;
            case "Recharged":
                mFragment = new RechargeSucessFragment();
                break;
            default:
                mFragment = new RechargeFailFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_layout, mFragment);
        fragmentTransaction.commit();
        setContentView(R.layout.activity_reservation_result);
    }

    @Override
    public void onBackPressed() {
        Bundle b = getIntent().getExtras();

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("user_id", b.getInt("user_id"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
