package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.capstone.ffrs.controller.NetworkController;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String imageUrl, name, address;
    Date from, to;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

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

        from = (Date) b.getSerializable("time_from");
        to = (Date) b.getSerializable("time_to");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        id = b.getInt("field_id");

        long hours = Integer.valueOf(Double.valueOf(Math.abs(to.getTime() - from.getTime()) / 36e5).intValue());
        long minutes = Integer.valueOf(Double.valueOf((Math.abs(to.getTime() - from.getTime()) / (60 * 1000)) % 60).intValue());

        String duration = "Thời lượng chơi: " + (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");

        TextView txtFrom = (TextView) findViewById(R.id.text_from);
        txtFrom.setText("Từ: " + sdf.format(from));
        TextView txtTo = (TextView) findViewById(R.id.text_to);
        txtTo.setText("Đến: " + sdf.format(to));
        TextView txtDuration = (TextView) findViewById(R.id.text_duration);
        txtDuration.setText(duration);

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
}
