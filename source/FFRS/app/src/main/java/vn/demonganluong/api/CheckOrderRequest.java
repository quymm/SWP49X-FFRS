package vn.demonganluong.api;

import android.content.Context;

import com.capstone.ffrs.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import vn.demonganluong.bean.CheckOrderBean;
import vn.demonganluong.utils.Constant;

/**
 * Created by DucChinh on 6/13/2016.
 */
public class CheckOrderRequest {

    private CheckOrderRequestOnResult checkOrderRequestOnResult;

    public void execute(final Context pContext, CheckOrderBean checkOrderBean) {
        RequestParams lvParams = new RequestParams();
        lvParams.put("func", checkOrderBean.getFunc());
        lvParams.put("version", checkOrderBean.getVersion());
        lvParams.put("merchant_id", checkOrderBean.getMerchantID());
        lvParams.put("token_code", checkOrderBean.getTokenCode());
        lvParams.put("checksum", checkOrderBean.getChecksum());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(Constant.MAIN_URL, lvParams, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (checkOrderRequestOnResult != null) {
                    String content = pContext.getString(R.string.session_timeout);
                    checkOrderRequestOnResult.onCheckOrderRequestOnResult(false, content);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = new String(responseBody);
                try {
                    if (checkOrderRequestOnResult != null) {
                        checkOrderRequestOnResult.onCheckOrderRequestOnResult(true, content);
                    }
                } catch (Exception e) {
                    checkOrderRequestOnResult.onCheckOrderRequestOnResult(false, content);
                }
            }

        });
    }

    public void getCheckOrderRequestOnResult(CheckOrderRequestOnResult checkOrderRequestOnResult) {
        this.checkOrderRequestOnResult = checkOrderRequestOnResult;
    }

    public interface CheckOrderRequestOnResult {
        void onCheckOrderRequestOnResult(boolean result, String data);
    }
}
