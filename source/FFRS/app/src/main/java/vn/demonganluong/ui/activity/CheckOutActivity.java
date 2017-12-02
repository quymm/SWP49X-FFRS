package vn.demonganluong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.capstone.ffrs.R;

import vn.demonganluong.utils.Constant;

/**
 * Created by DucChinh on 6/13/2016.
 */
public class CheckOutActivity extends Activity {

    public static final String TOKEN_CODE = "token_code";
    public static final String CHECKOUT_URL = "checkout_url";

    private WebView webData;

    private String mTokenCode = "";
    private String mCheckoutUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nganluong_checkout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTokenCode = extras.getString(TOKEN_CODE, "");
            mCheckoutUrl = extras.getString(CHECKOUT_URL, "");
        }

        initView();
    }

    private void initView() {
        webData = (WebView) findViewById(R.id.activity_checkout_webView);
        webData.getSettings().setJavaScriptEnabled(true);
        webData.setWebChromeClient(new WebChromeClient());
        webData.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("nganluong", "url: " + url);
                if (url.equalsIgnoreCase(Constant.RETURN_URL)) {
                    Intent intentCheckout = new Intent(getApplicationContext(), CheckOrderActivity.class);
                    intentCheckout.putExtra(CheckOrderActivity.TOKEN_CODE, mTokenCode);
                    Bundle b = getIntent().getExtras();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CheckOutActivity.this);
                    if (b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
                        intentCheckout.putExtra("field_id", b.getInt("field_id"));
                        intentCheckout.putExtra("field_name", b.getString("field_name"));
                        intentCheckout.putExtra("field_address", b.getString("field_address"));
                        intentCheckout.putExtra("field_type_id", b.getInt("field_type_id"));
                        intentCheckout.putExtra("date", b.getSerializable("date"));
                        intentCheckout.putExtra("time_from", b.getSerializable("time_from"));
                        intentCheckout.putExtra("time_to", b.getSerializable("time_to"));
                        intentCheckout.putExtra("price", b.getInt("price"));
                        intentCheckout.putExtra("user_id", b.getInt("user_id"));
                        intentCheckout.putExtra("tour_match_mode", b.getBoolean("tour_match_mode"));
                        if (b.getBoolean("tour_match_mode")) {
                            intentCheckout.putExtra("matching_request_id", b.getInt("matching_request_id"));
                            intentCheckout.putExtra("opponent_id", b.getInt("opponent_id"));
                        }
                        intentCheckout.putExtra("recharge_for_reservation", true);
                    } else {
                        intentCheckout.putExtra("user_id", preferences.getInt("user_id", -1));
                    }
                    intentCheckout.putExtra("role", "user");
                    intentCheckout.putExtra("money", b.getInt("money"));
                    startActivity(intentCheckout);
                    finish();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        if (!mCheckoutUrl.equalsIgnoreCase("")) {
            webData.loadUrl(mCheckoutUrl);
        }
    }
}
