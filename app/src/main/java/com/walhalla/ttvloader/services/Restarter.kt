package com.walhalla.ttvloader.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.walhalla.ttvloader.clipboard.ClipboardMonitorService
import com.walhalla.ui.DLog

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        DLog.d("Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, ClipboardMonitorService::class.java).setAction(com.android.widget.Config.START_FOREGROUND_ACTION))
        } else {
            context.startService(Intent(context, ClipboardMonitorService::class.java))
        }
    }
}
