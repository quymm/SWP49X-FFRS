package com.capstone.ffrs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.ffrs.adapter.SearchPagerAdapter;
import com.capstone.ffrs.service.FirebaseNotificationServices;
import com.capstone.ffrs.utils.GPSLocationListener;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Boolean exit = false;

    //provides gps location updates
    private GPSLocationListener gpsLocationListener;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("balance", b.getInt("balance"));
            editor.commit();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerLayout = navigationView.getHeaderView(0);
            TextView txtBalance = (TextView) headerLayout.findViewById(R.id.text_balance);
            txtBalance.setText("Tiền còn lại: " + sharedPreferences.getInt("balance", 0) + "K đồng");
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView txtTeamName = (TextView) headerLayout.findViewById(R.id.text_name);
        txtTeamName.setText(preferences.getString("teamName", ""));

        TextView txtPoints = (TextView) headerLayout.findViewById(R.id.text_points);
        txtPoints.setText("Điểm đổi thưởng: " + preferences.getInt("points", 0));

        TextView txtBalance = (TextView) headerLayout.findViewById(R.id.text_balance);
        txtBalance.setText("Tiền còn lại: " + preferences.getInt("balance", 0) + "K đồng");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //call to location listener to start location updates when activity gets started
        if (gpsLocationListener != null && pm.isInteractive()) {
            gpsLocationListener.onStart();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("balance-message"));
    }

    @Override
    public void onPause() {
        super.onPause();
        //call to stop location updates when activity gets stopped
        if (gpsLocationListener != null) {
            gpsLocationListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(this, FirebaseNotificationServices.class);
        startService(intent);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SearchPagerAdapter pagerAdapter = new SearchPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        gpsLocationListener = new GPSLocationListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                super.onBackPressed(); // finish activity
            } else {
                Toast.makeText(this, "Bấm Back lần nữa để thoát ứng dụng.", 2 * 1000).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2 * 1000);

            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_recharge) {
            Intent intent = new Intent(this, RechargeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(this, RewardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_favorite_field) {
            Intent intent = new Intent(this, FavoriteFieldActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_blacsklist) {
            Intent intent = new Intent(this, BlacklistActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this, FirebaseNotificationServices.class);
            stopService(intent);

            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
