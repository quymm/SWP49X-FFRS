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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FieldDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String imageUrl, name, address;
    Date from, to, date;
    int id, price;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();

        name = b.getString("field_name");
        TextView txtName = (TextView) findViewById(R.id.field_name);
        txtName.setText(name);

        address = b.getString("field_address");
        TextView txtAddress = (TextView) findViewById(R.id.field_address);
        txtAddress.setText(address);

        imageUrl = b.getString("image_url");
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.field_image);
        imageView.setImageUrl(imageUrl, NetworkController.getInstance(this.getBaseContext()).getImageLoader());

        date = (Date) b.getSerializable("date");

        from = (Date) b.getSerializable("time_from");
        to = (Date) b.getSerializable("time_to");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        id = b.getInt("field_id");

        price = b.getInt("price");

        double baseDuration = Math.abs(to.getTime() - from.getTime()) / 36e5;

        int totalPrice = Double.valueOf(baseDuration * price).intValue();

        long hours = Integer.valueOf(Double.valueOf(Math.abs(to.getTime() - from.getTime()) / 36e5).intValue());
        long minutes = Integer.valueOf(Double.valueOf((Math.abs(to.getTime() - from.getTime()) / (60 * 1000)) % 60).intValue());

        String duration = "Thời lượng chơi: " + (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");

        TextView txtDate = (TextView) findViewById(R.id.text_date);
        txtDate.setText("Ngày: " + new SimpleDateFormat("dd/MM/yyyy").format(date));
        TextView txtFrom = (TextView) findViewById(R.id.text_from);
        txtFrom.setText("Từ: " + sdf.format(from));
        TextView txtTo = (TextView) findViewById(R.id.text_to);
        txtTo.setText("Đến: " + sdf.format(to));
        TextView txtDuration = (TextView) findViewById(R.id.text_duration);
        txtDuration.setText(duration);
        TextView txtPrice = (TextView) findViewById(R.id.text_total_price);
        txtPrice.setText("Tổng giá: " + (totalPrice / 1000) + " ngàn đồng");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        finish();
    }

    public void onClickShowMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onClickReserve(View view) {
        String url = "http://10.0.2.2:8080/swp49x-ffrs/match/friendly-match";
        queue = NetworkController.getInstance(this).getRequestQueue();
        Map<String, Object> params = new HashMap<>();
        params.put("date", new SimpleDateFormat("dd/MM/yyyy").format(date));
        params.put("duration", (Math.abs(to.getTime() - from.getTime()) / 36e5) * 60);
        params.put("fieldOwnerId", id);
        params.put("fieldTypeId", 1);
        params.put("startTime", new SimpleDateFormat("H:mm").format(from));
        params.put("userId", 1);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Intent intent = new Intent(FieldDetailActivity.this, ReservationResultActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(FieldDetailActivity.this, "Cannot reserve!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(postRequest);

    }
}
