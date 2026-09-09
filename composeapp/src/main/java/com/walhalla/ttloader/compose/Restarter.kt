package com.walhalla.ttloader.compose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.android.widget.Config

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val start = Intent(context, ClipboardMonitorService::class.java)
        start.action = Config.START_FOREGROUND_ACTION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(start)
        } else {
            context.startService(start)
        }
    }
}
