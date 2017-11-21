package vn.demonganluong.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;

import org.json.JSONObject;

import com.capstone.ffrs.R;

import vn.demonganluong.api.SendOrderRequest;
import vn.demonganluong.bean.SendOrderBean;
import vn.demonganluong.ui.BaseActivity;
import vn.demonganluong.utils.Commons;
import vn.demonganluong.utils.Constant;

public class NganLuongActivity extends BaseActivity implements SendOrderRequest.SendOrderRequestOnResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        if (!Commons.checkInternetConnection(getApplicationContext())) {
            showErrorDialog(getString(R.string.error_disconnect), true);
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Bundle b = getIntent().getExtras();
            sendOrderObject(preferences.getString("teamName", "Capstone"), Integer.toString(b.getInt("money") * 1000), "buyerdefault@email.com", "0900000000", "demo");
        }
    }

    private void sendOrderObject(String fullName, String amount, String email, String phoneNumber, String address) {
        SendOrderBean sendOrderBean = new SendOrderBean();
        sendOrderBean.setFunc("sendOrder");
        sendOrderBean.setVersion("1.0");
        sendOrderBean.setMerchantID(Constant.MERCHANT_ID);
        sendOrderBean.setMerchantAccount("nhant@peacesoft.net");
        sendOrderBean.setOrderCode("CAPSTONE_DEMO");
        sendOrderBean.setTotalAmount(Integer.valueOf(amount));
        sendOrderBean.setCurrency("vnd");
        sendOrderBean.setLanguage("vi");
        sendOrderBean.setReturnUrl(Constant.RETURN_URL);
        sendOrderBean.setCancelUrl(Constant.CANCEL_URL);
        sendOrderBean.setNotifyUrl(Constant.NOTIFY_URL);
        sendOrderBean.setBuyerFullName(fullName);
        sendOrderBean.setBuyerEmail(email);
        sendOrderBean.setBuyerMobile(phoneNumber);
        sendOrderBean.setBuyerAddress(address);

        String checksum = getChecksum(sendOrderBean);
        sendOrderBean.setChecksum(checksum);

        SendOrderRequest sendOrderRequest = new SendOrderRequest();
        sendOrderRequest.execute(getApplicationContext(), sendOrderBean);
        sendOrderRequest.getSendOrderRequestOnResult(this);
    }

    private String getChecksum(SendOrderBean sendOrderBean) {
        String stringSendOrder = sendOrderBean.getFunc() + "|" +
                sendOrderBean.getVersion() + "|" +
                sendOrderBean.getMerchantID() + "|" +
                sendOrderBean.getMerchantAccount() + "|" +
                sendOrderBean.getOrderCode() + "|" +
                sendOrderBean.getTotalAmount() + "|" +
                sendOrderBean.getCurrency() + "|" +
                sendOrderBean.getLanguage() + "|" +
                sendOrderBean.getReturnUrl() + "|" +
                sendOrderBean.getCancelUrl() + "|" +
                sendOrderBean.getNotifyUrl() + "|" +
                sendOrderBean.getBuyerFullName() + "|" +
                sendOrderBean.getBuyerEmail() + "|" +
                sendOrderBean.getBuyerMobile() + "|" +
                sendOrderBean.getBuyerAddress() + "|" +
                Constant.MERCHANT_PASSWORD;
        String checksum = Commons.md5(stringSendOrder);
        return checksum;
    }

    @Override
    public void onSendOrderRequestOnResult(boolean result, String data) {
        if (result == true) {
            try {
                JSONObject objResult = new JSONObject(data);
                String responseCode = objResult.getString("response_code");
                if (responseCode.equalsIgnoreCase("00")) {
                    String tokenCode = objResult.getString("token_code");
                    String checkoutUrl = objResult.getString("checkout_url");

                    Intent intentCheckout = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intentCheckout.putExtra(CheckOutActivity.TOKEN_CODE, tokenCode);
                    intentCheckout.putExtra(CheckOutActivity.CHECKOUT_URL, checkoutUrl);
                    Bundle b = getIntent().getExtras();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                    overridePendingTransition(0, 0);
                    finish();
                } else {
                    showErrorDialog(Commons.getCodeError(getApplicationContext(), responseCode), false);
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    private void showErrorDialog(String message, final boolean isExit) {
        final Dialog mSuccessDialog = new Dialog(NganLuongActivity.this);
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
