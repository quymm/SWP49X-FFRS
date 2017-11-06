package com.capstone.ffrs.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.capstone.ffrs.FieldSuggestActivity;

/**
 * Created by HuanPMSE61860 on 10/29/2017.
 */

public class TimerServices extends Service {

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "timer_countdown_message";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(1000 * 60 * 1, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                bi.putExtra("status", "running");
                sendBroadcast(bi);
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {
        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        bi.putExtra("status", "destroyed");
        sendBroadcast(bi);
        stopSelf();
    }
}
