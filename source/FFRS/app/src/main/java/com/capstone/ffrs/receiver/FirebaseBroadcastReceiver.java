package com.capstone.ffrs.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuanPMSE61860 on 11/30/2017.
 */

public class FirebaseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null && b.containsKey("notification_tour_match_id")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/tourMatch/" + b.getInt("notification_user_id") + "/" + b.getInt("notification_tour_match_id"), 1);
            ref.updateChildren(childUpdates);
        }
    }

}