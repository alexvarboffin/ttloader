package com.walhalla.ttvloader.clipboard;

import static com.android.widget.Config.KEY_TKT_LOADER;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.android.widget.Config;

public class MyPref {
    private static MyPref instance;


    private final SharedPreferences settings;


    public static synchronized MyPref getInstance(Context context) {
        if (instance == null) {
            instance = new MyPref(context);
        }
        return instance;
    }

    private MyPref(Context activity) {
        //this.settings = PreferenceManager.getDefaultSharedPreferences(activity);
        this.settings = activity.getSharedPreferences(KEY_TKT_LOADER, Context.MODE_PRIVATE); // 0 - for private mode

    }

    public boolean getKeyBoardMonitor() {
        //Android Q == Android 10 sdk29
        boolean defValue = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            defValue = false;
        }
        return settings.getBoolean(Config.KEY_CLIPBOARD_MONITOR, defValue);
    }

    public void setKeyBoardMonitor(boolean b) {
        settings.edit().putBoolean(Config.KEY_CLIPBOARD_MONITOR, b).apply();
    }
}
