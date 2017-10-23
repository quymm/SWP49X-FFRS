package com.capstone.ffrs.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.capstone.ffrs.FieldDetailActivity;
import com.capstone.ffrs.FieldSuggestActivity;
import com.capstone.ffrs.MainActivity;
import com.capstone.ffrs.NotificationActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.Notification;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Iterator;

/**
 * Created by HuanPMSE61860 on 10/23/2017.
 */

public class FirebaseNotificationServices extends Service {

    public FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    Context context;

    private int userId;
    private boolean firstCreated;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TEST", "SERVICE STARTED");
        context = this;

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        firstCreated = true;

        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (prefs.contains("user_id")) {
            userId = prefs.getInt("user_id", -1);
        }

        setupNotificationListener();
    }


    private void setupNotificationListener() {
//
//        mDatabase.getReference().child("notifications")
//                .orderByChild("status").equalTo(0)
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        if(dataSnapshot != null){
//                            Notification notification = dataSnapshot.getValue(Notification.class);
//
//                            showNotification(context,notification,dataSnapshot.getKey());
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Utilities.log("onChildChanged",dataSnapshot);
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                        Utilities.log("onChildRemoved",dataSnapshot);
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                        Utilities.log("onChildMoved",dataSnapshot);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Utilities.log("onCancelled",databaseError);
//                    }
//                });
//
        myRef.child("request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!firstCreated) {
                    if (userId != -1) {
                        // Find matching requests by ID
                        // Notification if matching requests has same userId

                        // Notification to device
                        showNotification(context);
                    }
                } else {
                    firstCreated = false;
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showNotification(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FFRS")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText("Có đối thủ yêu cầu đá chung.")
                .setAutoCancel(true);

        Intent backIntent = new Intent(context, NotificationActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

}