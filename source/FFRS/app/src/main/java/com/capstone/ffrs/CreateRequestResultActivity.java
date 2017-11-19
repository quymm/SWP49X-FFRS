package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreateRequestResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        String resultMsg = b.getString("message");

        TextView txtResult = (TextView) findViewById(R.id.text_result);
        txtResult.setText(resultMsg);
    }

    @Override
    public void onBackPressed() {
        Bundle b = getIntent().getExtras();

        Intent intent = new Intent(CreateRequestResultActivity.this, SearchActivity.class);
        intent.putExtra("user_id", b.getInt("user_id"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickHome(View view) {
        Bundle b = getIntent().getExtras();

        Intent intent = new Intent(CreateRequestResultActivity.this, SearchActivity.class);
        intent.putExtra("user_id", b.getInt("user_id"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
