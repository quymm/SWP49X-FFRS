package com.capstone.ffrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import vn.demonganluong.ui.activity.*;

public class PaymentOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void onClickChoosePayment(View view) {
        RadioGroup rdGroup = (RadioGroup) findViewById(R.id.rdPaymentOptions);
        int radioBtnId = rdGroup.getCheckedRadioButtonId();
        Intent intent;
        Bundle b;
        SharedPreferences preferences;
        switch (radioBtnId) {
            case R.id.rbtPaypal:
                intent = new Intent(this, PayPalActivity.class);
                b = getIntent().getExtras();
                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                if (b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
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
                } else {
                    intent.putExtra("user_id", preferences.getInt("user_id", -1));
                }
                intent.putExtra("role", "user");
                intent.putExtra("money", b.getInt("money"));
                startActivity(intent);
                break;
            case R.id.rbtNganLuong:
                intent = new Intent(this, NganLuongActivity.class);
                b = getIntent().getExtras();
                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                if (b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
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
                } else {
                    intent.putExtra("user_id", preferences.getInt("user_id", -1));
                }
                intent.putExtra("role", "user");
                intent.putExtra("money", b.getInt("money"));
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Phương thức thanh toán đang trong quá trình thử nghiệm", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
