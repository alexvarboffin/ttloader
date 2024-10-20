package com.walhalla.ttvloader.utils;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtils {



    public static boolean serviceIsRunningInForeground(Context context, Class<?> serviceClazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClazz.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

}
