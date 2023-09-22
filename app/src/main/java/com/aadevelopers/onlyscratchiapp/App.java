package com.aadevelopers.onlyscratchiapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.aadevelopers.onlyscratchiapp.utils.Constant;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;


public class App extends Application {
    private static App mInstance;
    public static final String TAG = App.class.getSimpleName();
    private static final String ONESIGNAL_APP_ID = "xxxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxx";

    private RequestQueue mRequestQueue;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        /*
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);


         */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Log.e("TAG", "onInitializationComplete: Initialize Successfully...");
//            }
//        });
    }

    public static void initAds() {
        String adType = Constant.getString(getInstance(), Constant.AD_TYPE);
        if (adType.equalsIgnoreCase("admob")) {
            Log.e(TAG, "initAds: admob init");
            MobileAds.initialize(getInstance(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });
        } else if (adType.equalsIgnoreCase("startapp")) {
            StartAppSDK.setUserConsent(getInstance(),
                    "pas",
                    System.currentTimeMillis(),
                    false);
            StartAppSDK.init(mInstance, Constant.getString(getInstance(), Constant.STARTAPP_APP_ID), false);

            StartAppAd.disableSplash();

        } else if (adType.equalsIgnoreCase("unity")) {
            String GAME_ID = Constant.getString(getInstance(), Constant.UNITY_GAME_ID);
            boolean TEST_MODE = mInstance.getResources().getBoolean(R.bool.test_mode);
            //UnityAds.initialize(mInstance, GAME_ID, TEST_MODE);
        }else if(adType.equalsIgnoreCase("applovin")){
            AppLovinSdk.getInstance(getInstance()).setMediationProvider( "max" );
            AppLovinSdk.initializeSdk(getInstance(), new AppLovinSdk.SdkInitializationListener() {
                @Override
                public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
                {
                    // AppLovin SDK is initialized, start loading ads
                }
            } );
        }

    }



    public static Context getContext() {
        return mInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

}
