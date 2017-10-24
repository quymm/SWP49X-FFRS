package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.capstone.ffrs.controller.NetworkController;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FieldDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String imageUrl, name, address;
    Date from, to, date;
    int id, totalPrice, fieldTypeId;

    String localhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        localhost = getResources().getString(R.string.local_host);

        Bundle b = getIntent().getExtras();

        name = b.getString("field_name");
        TextView txtName = (TextView) findViewById(R.id.field_name);
        txtName.setText(name);

        address = b.getString("field_address");
        TextView txtAddress = (TextView) findViewById(R.id.field_address);
        txtAddress.setText(address);

//        imageUrl = b.getString("image_url");
//        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.field_image);
//        imageView.setImageUrl(imageUrl, NetworkController.getInstance(this.getBaseContext()).getImageLoader());

        date = (Date) b.getSerializable("date");

        from = (Date) b.getSerializable("time_from");
        to = (Date) b.getSerializable("time_to");
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);

        id = b.getInt("field_id");
        fieldTypeId = b.getInt("field_type_id");

        totalPrice = b.getInt("price");

        int hours = Double.valueOf(Math.abs(to.getTime() - from.getTime()) / 36e5).intValue();
        int minutes = Double.valueOf((Math.abs(to.getTime() - from.getTime()) / (60 * 1000)) % 60).intValue();

        String duration = (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");

        TextView txtDate = (TextView) findViewById(R.id.text_date);
        txtDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        TextView txtFrom = (TextView) findViewById(R.id.text_from);
        txtFrom.setText(sdf.format(from));
        TextView txtTo = (TextView) findViewById(R.id.text_to);
        txtTo.setText(sdf.format(to));
        TextView txtDuration = (TextView) findViewById(R.id.text_duration);
        txtDuration.setText(duration);

        String strFieldType = "";
        switch (fieldTypeId) {
            case 1:
                strFieldType += "5 vs 5";
                break;
            case 2:
                strFieldType += "7 vs 7";
                break;
            case 3:
                strFieldType += "11 vs 11";
                break;
            default:
                strFieldType += "Chưa xác định";
                break;
        }
        TextView txtFieldType = (TextView) findViewById(R.id.text_field_type);
        txtFieldType.setText(strFieldType);

        TextView txtPrice = (TextView) findViewById(R.id.text_total_price);
        txtPrice.setText(totalPrice + "K đồng");
    }

    @Override
    public void onBackPressed() {
        Bundle b = getIntent().getExtras();
        int timeSlotId = b.getInt("time_slot_id");
        String url = localhost + "/swp49x-ffrs/match/cancel-reservation?time-slot-id=" + timeSlotId;
        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_rewards) {
            Intent intent = new Intent(this, RewardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickGoBackToTime(View view) {
        onBackPressed();
    }

    public void onClickShowMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("field_id", id);
        startActivity(intent);
    }

    public void onClickReserve(View view) {
        Bundle b = getIntent().getExtras();
        Intent intent = new Intent(FieldDetailActivity.this, PayPalActivity.class);
        intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
        intent.putExtra("user_id", b.getInt("user_id"));
        intent.putExtra("field_id", id);
        intent.putExtra("field_name", name);
        intent.putExtra("field_address", address);
        intent.putExtra("image_url", imageUrl);
        intent.putExtra("price", totalPrice);

        boolean tourMatchMode = b.getBoolean("tour_match_mode");
        if (tourMatchMode) {
            intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
            intent.putExtra("tour_match_mode", tourMatchMode);
            if (b.containsKey("opponent_id")) {
                intent.putExtra("opponent_id", b.getInt("opponent_id"));
            }
        }

        startActivity(intent);
    }
}