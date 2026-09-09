package com.android.widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Environment
import java.io.File

class Config {
    companion object {
        const val KEY_CLIPBOARD_MONITOR = "cm_running_on"
        const val STEP_1_ENABLED = true
        const val KEY_TKT_LOADER = "tkt__"
        //com.usmans.tiktokvideodownloader
        //                DOMAIN,api-h2.tiktokv.com,PROXY
        //                DOMAIN,api2-16-h2.musical.ly,PROXY
        //                DOMAIN,api2-19-h2.musical.ly,PROXY
        //    curl -X GET -H "Cookie: tt_webid=<COOKIE>;" -H "User-Agent: x"
        //    "https://api2-19-h2.musical.ly/aweme/v1/comment/list/?aweme_id=<VIDEO ID>&cursor=0&count=50
        //    &device_type=x&app_name=musical_ly&channel=x&device_platform=android&version_code=x&os_version=x"
        //const val QBASE  = "api2-16-h2.musical.ly";
        //public static final  String QBASE= "api2-19-h2.musical.ly";
        //public static final  String TIKTOKAPI  = "https://" + QBASE + "/aweme/v1/aweme/detail/";
        //public static final  String FacebookApi  = "https://m.facebook.com/watch/";
        private const val DOWNLOAD_DIRECTORY = "TiktokDownload"
        const val PREF_APPNAME = "pref_ttk_loader"
        //public static final  String URL_NOT_SUPPORTED  = "This url not supported or no media found!";
        const val DOWNLOADING_MSG = "Generating download link"
        private const val PACKAGE_BASE = "com.walhalla.ttloader"
        //public static final String FILE_PROVIDER = PACKAGE_BASE + ".provider";
        @JvmField val START_FOREGROUND_ACTION: String = PACKAGE_BASE + ".action.startforeground"
        @JvmField val STOP_FOREGROUND_ACTION: String = PACKAGE_BASE + ".action.stopforeground"
        private val KEY_DEFAULT_PATH: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        @JvmStatic fun videoFolder(context: Context): File {
            var preferences: SharedPreferences = context.getSharedPreferences(PREF_APPNAME, Context.MODE_PRIVATE)
            var tmp: String = preferences.getString("path", KEY_DEFAULT_PATH.absolutePath) ?: ""
            if (KEY_DEFAULT_PATH.absolutePath.equals(tmp)) {
                tmp = preferences.getString("path", KEY_DEFAULT_PATH.absolutePath) ?: ""
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tmp = Environment.getExternalStorageDirectory().absolutePath + File.separator + DOWNLOAD_DIRECTORY
                } else {
                    tmp = Environment.getExternalStorageDirectory().absolutePath + File.separator + DOWNLOAD_DIRECTORY
                }
            }
            var file: File = KEY_DEFAULT_PATH
            //file = Environment.getExternalStorageDirectory().absolutePath + File.separator + DOWNLOAD_DIRECTORY;
            //file = Environment.DIRECTORY_MOVIES;
            var bits: Array<String> = file.getAbsolutePath().split("/").toTypedArray()
            //        for (String bit : bits) {
            //            DLog.d("@ --> " + bit);
            //        }
            //        File tmp00 = new File(file);
            //        if (!tmp00.exists()) {
            //            boolean res = tmp00.mkdirs();
            //            DLog.d("\uD83D\uDE0D CREATE FOLDER -> " + res + tmp00.getAbsolutePath());
            //            ///storage/sdcard/TTDwn
            //        }
            return file
        }
    }
}
