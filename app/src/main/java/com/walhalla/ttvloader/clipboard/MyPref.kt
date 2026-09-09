package com.walhalla.ttvloader.clipboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.android.widget.Config

class MyPref {
    private lateinit var settings: SharedPreferences
    private constructor(activity: Context) {
        //this.settings = PreferenceManager.getDefaultSharedPreferences(activity);
        this.settings = activity.getSharedPreferences(com.android.widget.Config.KEY_TKT_LOADER, Context.MODE_PRIVATE)
    }
    // 0 - for private mode
    fun getKeyBoardMonitor(): Boolean {
        //Android Q == Android 10 sdk29
        var defValue: Boolean = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            defValue = false
        }
        return settings.getBoolean(Config.KEY_CLIPBOARD_MONITOR, defValue)
    }
    fun setKeyBoardMonitor(b: Boolean) {
        settings.edit().putBoolean(Config.KEY_CLIPBOARD_MONITOR, b).apply()
    }
    companion object {
        private var instance: MyPref? = null
        @Synchronized @JvmStatic fun getInstance(context: Context): MyPref {
            if (instance == null) {
                instance = MyPref(context)
            }
            return instance!!
        }
    }
}
