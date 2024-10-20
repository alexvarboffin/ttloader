package com.retrytech.medialoot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.retrytech.medialoot.network.ApiClient;
import com.retrytech.medialoot.network.ApiService;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class MyApplication extends MultiDexApplication {
    public static final String TAG = "MyApplication";
    @SuppressLint({"StaticFieldLeak"})
    static MyApplication appInstance;
    public static HashMap<String, Typeface> mTypefaceMap;
    private String token;
    private ApiService apiService;
    Scheduler scheduler;

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            myApplication = appInstance;
        }

        Crashlytics.setInt("current_level", 3);
        Crashlytics.setString("last_UI_action", "logged_in");
        Crashlytics.log(Log.DEBUG, "tag", "message");
        Crashlytics.log("message");

        Fabric.with(myApplication, new Crashlytics());


        return myApplication;
    }

    public void onCreate() {
        super.onCreate();
        appInstance = this;
        mTypefaceMap = new HashMap<>();


//        getTypeface(ConstantStore.FONT_1);
    }



    public Typeface getTypeface(String str) {
        Typeface typeface = (Typeface) mTypefaceMap.get(str);
        if (typeface != null) {
            return typeface;
        }
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), str);
        mTypefaceMap.put(str, createFromAsset);
        return createFromAsset;
    }




    private static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
    public static MyApplication create(Context context) {
        return MyApplication.get(context);
    }


    public ApiService getApiService() {
        if (apiService == null) {
            apiService = ApiClient.create();
        }
        return apiService;
    }
    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }
}
