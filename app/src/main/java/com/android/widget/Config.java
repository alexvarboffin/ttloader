package com.android.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import com.walhalla.mvp.SharedObjects;

import java.io.File;

public class Config {

    public static final String KEY_CLIPBOARD_MONITOR = "cm_running_on";

    public static final boolean STEP_1_ENABLED = true;
    public static final String KEY_TKT_LOADER = "tkt__";

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

    private static final String DOWNLOAD_DIRECTORY = "TiktokDownload";
    public static final String PREF_APPNAME = "pref_ttk_loader";
    //public static final  String URL_NOT_SUPPORTED  = "This url not supported or no media found!";

    public static final String DOWNLOADING_MSG = "Generating download link";


    private static final String PACKAGE_BASE = "com.walhalla.ttloader";
    //public static final String FILE_PROVIDER = PACKAGE_BASE + ".provider";
    public static final String START_FOREGROUND_ACTION = PACKAGE_BASE + ".action.startforeground";
    public static final String STOP_FOREGROUND_ACTION = PACKAGE_BASE + ".action.stopforeground";
    private static final File KEY_DEFAULT_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);


    public static File videoFolder(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_APPNAME, Context.MODE_PRIVATE);
        String tmp = preferences.getString("path", KEY_DEFAULT_PATH.getAbsolutePath());
        if (!KEY_DEFAULT_PATH.getAbsolutePath().equals(tmp)) {
            tmp = preferences.getString("path", KEY_DEFAULT_PATH.getAbsolutePath());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tmp = SharedObjects.externalMemory() + File.separator + DOWNLOAD_DIRECTORY;
            } else {
                tmp = SharedObjects.externalMemory() + File.separator + DOWNLOAD_DIRECTORY;
            }
        }
        File file = KEY_DEFAULT_PATH;

        //file = SharedObjects.externalMemory() + File.separator + DOWNLOAD_DIRECTORY;
        //file = Environment.DIRECTORY_MOVIES;

        String[] bits = file.getAbsolutePath().split("/");
//        for (String bit : bits) {
//            DLog.d("@@@ --> " + bit);
//        }

//        File tmp00 = new File(file);
//        if (!tmp00.exists()) {
//            boolean res = tmp00.mkdirs();
//            DLog.d("\uD83D\uDE0D CREATE FOLDER -> " + res + tmp00.getAbsolutePath());
//            ///storage/sdcard/TTDwn
//        }
        return file;
    }
}
