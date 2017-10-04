package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.capstone.ffrs.adapter.FieldAdapter;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.Field;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

public class FieldSuggestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //  String url = "https://api.myjson.com/bins/1fscel";
    //  String url = "http://10.0.2.2:8080/account/getFieldOwners";
    String url = "http://10.0.2.2:8080/account/getAccountByRole?role=owner";

    RecyclerView recyclerView;
    RequestQueue queue;
    List<Field> fieldList = new ArrayList<Field>();
    FieldAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_suggest);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        EditText edit_text = (EditText) findViewById(R.id.edit_text);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edit_text.addTextChangedListener(watcher);

        loadFields();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

//    private void search(SearchView searchView) {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//
//        MenuItem search = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//        search(searchView);
//        return true;
//    }


    public void loadFields() {
        //Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new FieldAdapter(this, fieldList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        JSONObject profile = obj.getJSONObject("profileId");
                        Field field = new Field(profile.getString("name"), profile.getString("address"), profile.getString("avatarUrl"));

                        // adding movie to movies array
                        fieldList.add(field);

                    } catch (Exception e) {
                        Log.d("EXCEPTION", e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("EXCEPTION", error.getMessage());
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONArray(utf8String), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

        };
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }
}
