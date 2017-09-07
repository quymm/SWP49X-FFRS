package com.molaappdemo3;

import com.facebook.react.ReactActivity;
import android.content.Intent;
import com.facebook.reactnative.androidsdk.FBSDKPackage;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "molaappdemo3";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // MainApplication.getCallbackManager().onActivityResult(requestCode, resultCode, data);
      if (requestCode == MainApplication.PAY_PAL_REQUEST_ID) {
        ((MainApplication) getApplication()).payPalPackage.handleActivityResult(requestCode, resultCode, data);
      }
      
    }
    
}
