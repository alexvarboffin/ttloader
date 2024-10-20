package com.walhalla.ttvloader.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.walhalla.ttvloader.clipboard.ClipboardMonitorService;
import com.walhalla.ui.DLog;

import static com.android.widget.Config.START_FOREGROUND_ACTION;

public class Restarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.d("Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ClipboardMonitorService.class).setAction(START_FOREGROUND_ACTION));
        } else {
            context.startService(new Intent(context, ClipboardMonitorService.class));
        }
    }
}