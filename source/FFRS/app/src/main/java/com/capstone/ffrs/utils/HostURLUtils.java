package com.capstone.ffrs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HuanPMSE61860 on 11/14/2017.
 */

public class HostURLUtils {

    private static HostURLUtils mInstance;

    private Context mContext;

    public HostURLUtils(Context context) {
        mContext = context;
    }

    public static synchronized HostURLUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HostURLUtils(context);
        }
        return mInstance;
    }

    public String getHostURL() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString("host_url", "http://localhost:8080");
    }

    public void setHostURL(String hostURL) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("host_url", hostURL);
        editor.apply();
    }
}
