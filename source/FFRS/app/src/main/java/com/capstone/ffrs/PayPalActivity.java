package com.capstone.ffrs;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.entity.FieldOwnerFriendlyNotification;
import com.capstone.ffrs.entity.FieldOwnerTourNotification;
import com.capstone.ffrs.service.TimerServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayPalActivity extends AppCompatActivity {

    // private static final String TAG = "paymentdemoblog";
    /**
     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     * <p>
     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    // private static final String CONFIG_ENVIRONMENT =
    // PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox
    // environments.
    private static final String CONFIG_CLIENT_ID = "AWeOKgwletEfUjXkyrMrY5djqXE3GZVkdD_ZSTdSdiD61I0faoHyPfbNdzhSFzz6YzeSK7xtc8P1S3HE";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID)
            // the following are only used in PayPalFuturePaymentActivity.
            .merchantName("FFRS")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;

    RequestQueue queue;
    String hostURL;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            stopService(new Intent(context, TimerServices.class));
            Toast.makeText(context, "Hết thời gian đặt sân", Toast.LENGTH_LONG).show();
            Bundle b = getIntent().getExtras();
            int timeSlotId = b.getInt("time_slot_id");
            String url = hostURL + getResources().getString(R.string.url_cancel_reservation);
            url = String.format(url, timeSlotId);
            RequestQueue queue = NetworkController.getInstance(context).getRequestQueue();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Intent intent = new Intent(PayPalActivity.this, FieldSuggestActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Intent intent = new Intent(PayPalActivity.this, FieldSuggestActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            queue.add(request);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        stopService(new Intent(this, TimerServices.class));

        Bundle b = getIntent().getExtras();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        thingToBuy = new PayPalPayment(new BigDecimal(b.getInt("price") / 23.0), "USD",
                "Đặt sân FFRS", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent paymentIntent = new Intent(PayPalActivity.this,
                PaymentActivity.class);

        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(paymentIntent, REQUEST_CODE_PAYMENT);

    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PayPalActivity.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {

                    hostURL = getResources().getString(R.string.local_host);
                    Bundle b = getIntent().getExtras();
                    final boolean tourMatchMode = b.getBoolean("tour_match_mode");
                    String url;
                    Map<String, Object> params = null;
                    if (!tourMatchMode) {
                        url = hostURL + getResources().getString(R.string.url_reserve_friendly_match);
                        url = String.format(url, b.getInt("time_slot_id"), b.getInt("user_id"), 0);
                    } else {
                        url = hostURL + getResources().getString(R.string.url_reserve_tour_match);
                        url = String.format(url, b.getInt("time_slot_id"), b.getInt("matching_request_id"), b.getInt("user_id"), 0);

//                        else {
//                            Integer tourMatchId = b.getInt("tour_match_id");
//                            url = hostURL + getResources().getString(R.string.url_add_bill);
//                            params = new HashMap<>();
//                            params.put("opponentPayment", true);
//                            params.put("tourMatchId", tourMatchId);
//                            params.put("voucherId", 0);
//                        }
                    }

                    queue = NetworkController.getInstance(this).getRequestQueue();

                    JSONObject jsonParams = null;
                    if (params != null) {
                        jsonParams = new JSONObject(params);
                    }

                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject body = response.getJSONObject("body");
                                        if (body != null && body.length() > 0) {
                                            try {
                                                Bundle b = getIntent().getExtras();
                                                Intent intent = new Intent(PayPalActivity.this, ReservationResultActivity.class);
                                                intent.putExtra("user_id", b.getInt("user_id"));
                                                intent.putExtra("field_id", b.getInt("field_id"));
                                                intent.putExtra("field_name", b.getString("field_name"));
                                                intent.putExtra("field_address", b.getString("field_address"));
                                                intent.putExtra("image_url", b.getString("image_url"));
                                                intent.putExtra("payment_result", "Succeed");
                                                if (!tourMatchMode) {
                                                    intent.putExtra("reserve_id", body.getJSONObject("friendlyMatchId").getInt("id"));

                                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PayPalActivity.this);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                                                    editor.commit();

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference ref = database.getReference();
                                                    DatabaseReference friendlyRef = ref.child("fieldOwner").child(b.getInt("field_id") + "")
                                                            .child("friendlyMatch").child(body.getJSONObject("friendlyMatchId").getInt("id") + "");
                                                    FieldOwnerFriendlyNotification notification = new FieldOwnerFriendlyNotification();
                                                    notification.setIsRead(0);
                                                    notification.setIsShowed(0);
                                                    notification.setTime(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
                                                    notification.setUsername(body.getJSONObject("userId").getJSONObject("profileId").getString("name"));
                                                    friendlyRef.setValue(notification);
                                                } else {
                                                    intent.putExtra("reserve_id", body.getJSONObject("tourMatchId").getInt("id"));

                                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PayPalActivity.this);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                                                    editor.commit();

                                                    if (!b.containsKey("tour_match_id")) {
                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                        DatabaseReference ref = database.getReference();
                                                        ref.child("tourMatch").child(b.getInt("opponent_id") + "").child(body.getJSONObject("tourMatchId").getInt("id") + "").setValue(0);
                                                        DatabaseReference tourRef = ref.child("fieldOwner").child(b.getInt("field_id") + "")
                                                                .child("tourMatch").child(body.getJSONObject("tourMatchId").getInt("id") + "");
                                                        FieldOwnerTourNotification notification = new FieldOwnerTourNotification();
                                                        notification.setIsRead(0);
                                                        notification.setIsShowed(0);
                                                        notification.setTime(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
                                                        tourRef.setValue(notification);
                                                    }
//                                                    else {
//                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                                        DatabaseReference ref = database.getReference();
//                                                        ref.child("request").child(b.getInt("opponent_id") + "").child(b.getInt("matching_request_id") + "").removeValue();
//                                                        ref.child("response").child(b.getInt("user_id") + "").child(body.getJSONObject("tourMatchId").getInt("id") + "").removeValue();
//                                                    }

                                                }
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                Log.d("EXCEPTION", e.getMessage());
                                            }
                                        } else {
                                            Toast.makeText(PayPalActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error.Response", error.toString());
                                }
                            });
                    queue.add(postRequest);

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Bundle b = getIntent().getExtras();
                Intent intent = new Intent(PayPalActivity.this, ReservationResultActivity.class);
                intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
                intent.putExtra("user_id", b.getInt("user_id"));
                intent.putExtra("field_id", b.getInt("field_id"));
                intent.putExtra("field_name", b.getString("field_name"));
                intent.putExtra("field_address", b.getString("field_address"));
                intent.putExtra("image_url", b.getString("image_url"));
                intent.putExtra("price", b.getInt("price"));
                intent.putExtra("payment_result", "Cancelled");
                startActivity(intent);
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Bundle b = getIntent().getExtras();
                Intent intent = new Intent(PayPalActivity.this, ReservationResultActivity.class);
                intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
                intent.putExtra("user_id", b.getInt("user_id"));
                intent.putExtra("field_id", b.getInt("field_id"));
                intent.putExtra("field_name", b.getString("field_name"));
                intent.putExtra("field_address", b.getString("field_address"));
                intent.putExtra("image_url", b.getString("image_url"));
                intent.putExtra("price", b.getInt("price"));
                intent.putExtra("payment_result", "Invalid configuration");
                startActivity(intent);
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(TimerServices.COUNTDOWN_BR));
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
