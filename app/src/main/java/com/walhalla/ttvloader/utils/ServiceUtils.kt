package com.walhalla.ttvloader.utils

import android.app.ActivityManager
import android.content.Context

object ServiceUtils {
    fun serviceIsRunningInForeground(context: Context, serviceClazz: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClazz.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }
}
