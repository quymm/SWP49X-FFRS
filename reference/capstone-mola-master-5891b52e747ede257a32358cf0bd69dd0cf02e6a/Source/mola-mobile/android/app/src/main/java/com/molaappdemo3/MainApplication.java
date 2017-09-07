package com.molaappdemo3;

import android.app.Application;

import com.facebook.react.ReactApplication;
import br.com.vizir.rn.paypal.PayPalPackage;
import com.facebook.reactnative.androidsdk.FBSDKPackage;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import co.apptailor.googlesignin.RNGoogleSigninPackage;
import com.avishayil.rnrestart.ReactNativeRestartPackage;
import com.evollu.react.fcm.FIRMessagingPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.imagepicker.ImagePickerPackage;
import com.i18n.reactnativei18n.ReactNativeI18n;
import com.RNFetchBlob.RNFetchBlobPackage;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import java.util.Arrays;
import java.util.List;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.oney.WebRTCModule.WebRTCModulePackage;
import com.zxcpoiu.incallmanager.InCallManagerPackage;
import com.brentvatne.react.ReactVideoPackage;



public class MainApplication extends Application implements ReactApplication {

  private static CallbackManager mCallbackManager = CallbackManager.Factory.create();
  public static final int PAY_PAL_REQUEST_ID = 91;
  public PayPalPackage payPalPackage;
  
  protected static CallbackManager getCallbackManager() {
    return mCallbackManager;
  }

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      payPalPackage = new PayPalPackage(PAY_PAL_REQUEST_ID);  // <--

      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
            new RNDeviceInfo(),
            new WebRTCModulePackage(),
            new RNGoogleSigninPackage(),
            new VectorIconsPackage(),
            new ReactNativeRestartPackage(),
            new InCallManagerPackage(),
            new ImagePickerPackage(),
            new RNFetchBlobPackage(),
            new FIRMessagingPackage(),
            new ReactNativeI18n(),    
            new ReactVideoPackage(),        
            new FBSDKPackage(mCallbackManager),
            payPalPackage
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);
    SoLoader.init(this, /* native exopackage */ false);
  }
}
