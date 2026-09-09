package com.walhalla.ttvloader.utils

import android.app.ActivityManager
import android.content.Context

class ServiceUtils {
    companion object {
        @JvmStatic fun serviceIsRunningInForeground(context: Context, serviceClazz: Class<*>): Boolean {
            var manager: ActivityManager = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClazz.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
    }
}
