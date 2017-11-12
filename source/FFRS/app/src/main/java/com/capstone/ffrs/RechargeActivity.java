package com.capstone.ffrs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;

import org.json.JSONObject;

public class RechargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText txtMoney = (EditText) findViewById(R.id.txtMoney);
        txtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                if (!value.isEmpty() && !value.equals("0")) {
                    Button btRecharge = (Button) findViewById(R.id.btRecharge);
                    btRecharge.setEnabled(true);
                    btRecharge.setBackgroundColor(ContextCompat.getColor(RechargeActivity.this, R.color.green));
                    if (Integer.valueOf(value) > 5000) {
                        txtMoney.setText("5000");
                    } else if (Integer.valueOf(value) < 50) {
                        btRecharge.setEnabled(false);
                        btRecharge.setBackgroundColor(ContextCompat.getColor(RechargeActivity.this, R.color.gray));
                    }
                } else {
                    if (value.length() > 0 && value.charAt(0) == '0') {
                        txtMoney.setText("");
                    }
                    Button btRecharge = (Button) findViewById(R.id.btRecharge);
                    btRecharge.setEnabled(false);
                    btRecharge.setBackgroundColor(ContextCompat.getColor(RechargeActivity.this, R.color.gray));
                }
            }
        });
        Bundle b = getIntent().getExtras();

        Button btRecharge = (Button) findViewById(R.id.btRecharge);
        if (b != null && b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
            txtMoney.setText(Integer.toString(b.getInt("price")));
        } else {
            btRecharge.setEnabled(false);
            btRecharge.setBackgroundColor(ContextCompat.getColor(RechargeActivity.this, R.color.gray));
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView txtBalance = (TextView) findViewById(R.id.text_money);
        txtBalance.setText(preferences.getInt("balance", 0) + " nghìn đồng");
    }

    @Override
    protected void onPause() {
        InputMethodManager inputManager = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void onClickRecharge(View view) {
        EditText txtMoney = (EditText) findViewById(R.id.txtMoney);
        Intent intent = new Intent(RechargeActivity.this, PaymentOptionActivity.class);
        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
            intent.putExtra("field_id", b.getInt("field_id"));
            intent.putExtra("field_name", b.getString("field_name"));
            intent.putExtra("field_address", b.getString("field_address"));
            intent.putExtra("field_type_id", b.getInt("field_type_id"));
            intent.putExtra("date", b.getSerializable("date"));
            intent.putExtra("time_from", b.getSerializable("time_from"));
            intent.putExtra("time_to", b.getSerializable("time_to"));
            intent.putExtra("price", b.getInt("price"));
            intent.putExtra("user_id", b.getInt("user_id"));
            intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
            intent.putExtra("tour_match_mode", b.getBoolean("tour_match_mode"));
            if (b.getBoolean("tour_match_mode")) {
                intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                intent.putExtra("opponent_id", b.getInt("opponent_id"));
            }
            intent.putExtra("recharge_for_reservation", true);
        }
        intent.putExtra("money", Integer.valueOf(txtMoney.getText().toString()));
        startActivity(intent);
    }
}
