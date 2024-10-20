package com.retrytech.medialoot.Activity;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    public static SessionManager mPreference;
    public static String UID = "";


    public SessionManager(Context context) {
        mSharedPref = context.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
    }


    public static void sharedInstance(Context context) {
        mPreference = new SessionManager(context);
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    public static String getFolderPath(Context context) {
        return getSharedPreferences(context).getString(Constant.FOLDER_PATH, "");
    }

    public static void storeFolderPath(Context context, String path) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constant.FOLDER_PATH, path);
        editor.commit();
    }

}
