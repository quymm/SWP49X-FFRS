package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.ffrs.utils.HostURLUtils;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView txtPlaceholder = (TextView) findViewById(R.id.text_placeholder_ip);
        txtPlaceholder.setText("Hiện tại: " + HostURLUtils.getInstance(this).getHostURL());

        EditText txtIP = (EditText) findViewById(R.id.text_ip);
        txtIP.setText(HostURLUtils.getInstance(this).getHostURL());
    }

    public void onClickChangeIP(View view) {
        EditText txtIP = (EditText) findViewById(R.id.text_ip);
        String strHostURL = txtIP.getText().toString();
        if (!strHostURL.isEmpty()) {
            if (!strHostURL.startsWith("http://")) {
                strHostURL = "http://" + strHostURL;
            }
            HostURLUtils.getInstance(this).setHostURL(strHostURL);

            TextView txtPlaceholder = (TextView) findViewById(R.id.text_placeholder_ip);
            txtPlaceholder.setText("Hiện tại: " + strHostURL);

            Toast.makeText(this, "Địa chỉ đã được thay đổi", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Nhập địa chỉ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
