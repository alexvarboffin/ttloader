package com.walhalla

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.walhalla.ttvloader.R
import com.walhalla.ui.DLog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class Tracker {
    companion object {
        @JvmStatic fun log(context: Context, url: String) {
            try {
                var var0: FirebaseAnalytics = bundle(context)
                var bundle: Bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "tiktok")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, url)
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "DOWNLOAD_VIDEO")
                var0.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
        @JvmStatic private fun bundle(context: Context): FirebaseAnalytics {
            var var0: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            //var0.setUserId("5107611364");
            var0.setUserProperty("p_lang", context.getString(R.string.default_location))
            var0.setUserProperty("p_time_zone", "" + TimeUnit.HOURS.convert(TimeZone.getDefault().getRawOffset().toLong(), TimeUnit.MILLISECONDS))
            var0.setUserProperty("p_fingerprint", Build.FINGERPRINT)
            var0.setUserProperty("p_model", Build.MODEL)
            var0.setUserProperty("p_board", Build.BOARD)
            var0.setUserProperty("p_manufacturer", Build.MANUFACTURER)
            var0.setUserProperty("p_host", Build.HOST)
            var0.setUserProperty("p_brand", Build.BRAND)
            var0.setUserProperty("p_product", Build.PRODUCT)
            var0.setUserProperty("p_device", Build.DEVICE)
            return var0
        }
        @JvmStatic fun resume(context: Context) {
            try {
                var var0: FirebaseAnalytics = bundle(context)
                var bundle: Bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "onResume")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "onResume")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "onResume")
                var0.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
        @JvmStatic fun writeToFile(data: String, context: Context) {
            try {
                // FileOutputStream file = context.openFileOutput("config.html", Context.MODE_PRIVATE);
                var file: FileOutputStream = FileOutputStream(File("/sdcard/index.html"))
                var outputStreamWriter: OutputStreamWriter = OutputStreamWriter(file)
                outputStreamWriter.write(data)
                outputStreamWriter.close()
            } catch (e: Exception) {
                Log.e("Exception", "File write failed: " + e.toString())
            }
        }
    }
}
