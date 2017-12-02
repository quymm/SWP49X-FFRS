package vn.demonganluong.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.capstone.ffrs.ReservationResultActivity;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import com.capstone.ffrs.R;

import java.util.HashMap;
import java.util.Map;

import vn.demonganluong.api.CheckOrderRequest;
import vn.demonganluong.bean.CheckOrderBean;
import vn.demonganluong.ui.BaseActivity;
import vn.demonganluong.utils.Commons;
import vn.demonganluong.utils.Constant;

/**
 * Created by DucChinh on 6/14/2016.
 */
public class CheckOrderActivity extends BaseActivity implements CheckOrderRequest.CheckOrderRequestOnResult {

    public static final String TOKEN_CODE = "token_code";

    private TextView txtData;
    private ProgressView mProgressView;

    private String mTokenCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nganluong_checkorder);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTokenCode = extras.getString(TOKEN_CODE, "");
        }

        initView();
    }

    private void initView() {
        txtData = (TextView) findViewById(R.id.activity_checkorder_txtData);
        txtData.setMovementMethod(new ScrollingMovementMethod());

        mProgressView = (ProgressView) findViewById(R.id.activity_checkorder_progressView);

        checkOrderObject();
    }

    private void checkOrderObject() {
        CheckOrderBean checkOrderBean = new CheckOrderBean();
        checkOrderBean.setFunc("checkOrder");
        checkOrderBean.setVersion("1.0");
        checkOrderBean.setMerchantID(Constant.MERCHANT_ID);
        checkOrderBean.setTokenCode(mTokenCode);

        String checksum = getChecksum(checkOrderBean);
        checkOrderBean.setChecksum(checksum);

        CheckOrderRequest checkOrderRequest = new CheckOrderRequest();
        checkOrderRequest.execute(getApplicationContext(), checkOrderBean);
        checkOrderRequest.getCheckOrderRequestOnResult(this);
        txtData.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    private String getChecksum(CheckOrderBean checkOrderBean) {
        String stringSendOrder = checkOrderBean.getFunc() + "|" +
                checkOrderBean.getVersion() + "|" +
                checkOrderBean.getMerchantID() + "|" +
                checkOrderBean.getTokenCode() + "|" +
                Constant.MERCHANT_PASSWORD;
        String checksum = Commons.md5(stringSendOrder);

        return checksum;
    }

    @Override
    public void onBackPressed() {
        Intent intentMain = new Intent(getApplicationContext(), NganLuongActivity.class);
        startActivity(intentMain);
        finish();
    }

    @Override
    public void onCheckOrderRequestOnResult(boolean result, String data) {
        if (result == true) {
            try {
                JSONObject objResult = new JSONObject(data);
                String responseCode = objResult.getString("response_code");
                if (responseCode.equalsIgnoreCase("00")) {
                    final Bundle b = getIntent().getExtras();
                    String hostURL = HostURLUtils.getInstance(this).getHostURL();
                    String url = hostURL + getResources().getString(R.string.url_add_to_balance);

                    RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
                    Map<String, Object> params = new HashMap<>();
                    params.put("accountId", b.getInt("user_id"));
                    params.put("role", "user");
                    params.put("information", "Ngan Luong");
                    params.put("balance", b.getInt("money"));
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (!response.isNull("body")) {
                                try {
                                    JSONObject body = response.getJSONObject("body");
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CheckOrderActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    Intent intent = new Intent(CheckOrderActivity.this, ReservationResultActivity.class);
                                    if (b.containsKey("recharge_for_reservation") && b.getBoolean("recharge_for_reservation")) {
                                        intent.putExtra("field_id", b.getInt("field_id"));
                                        intent.putExtra("field_name", b.getString("field_name"));
                                        intent.putExtra("field_address", b.getString("field_address"));
                                        intent.putExtra("field_type_id", b.getInt("field_type_id"));
                                        intent.putExtra("date", b.getSerializable("date"));
                                        intent.putExtra("time_from", b.getSerializable("time_from"));
                                        intent.putExtra("time_to", b.getSerializable("time_to"));
                                        intent.putExtra("price", b.getInt("price"));
                                        intent.putExtra("user_id", b.getInt("user_id"));
                                        intent.putExtra("tour_match_mode", b.getBoolean("tour_match_mode"));
                                        if (b.getBoolean("tour_match_mode")) {
                                            intent.putExtra("matching_request_id", b.getInt("matching_request_id"));
                                            intent.putExtra("opponent_id", b.getInt("opponent_id"));
                                        }
                                        intent.putExtra("recharge_for_reservation", true);
                                    }
                                    intent.putExtra("payment_result", "Recharged");
                                    editor.putInt("balance", body.getJSONObject("userId").getJSONObject("profileId").getInt("balance"));
                                    editor.apply();
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");

                            return headers;
                        }
                    };
                    queue.add(request);
                } else {
                    mProgressView.setVisibility(View.GONE);
                    showErrorDialog(Commons.getCodeError(getApplicationContext(), responseCode), false);
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    private void showErrorDialog(String message, final boolean isExit) {
        final Dialog mSuccessDialog = new Dialog(CheckOrderActivity.this);
        mSuccessDialog.setContentView(R.layout.dialog_success);
        mSuccessDialog.setCancelable(false);
        mSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mSuccessDialog.getWindow().setGravity(Gravity.CENTER);

        TextView txtContent = (TextView) mSuccessDialog.findViewById(R.id.dialog_success_txtContent);
        txtContent.setText(message);
        Button btnClose = (Button) mSuccessDialog.findViewById(R.id.dialog_success_btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuccessDialog.dismiss();
                if (isExit) {
                    finish();
                }
            }
        });

        mSuccessDialog.show();
    }
}
