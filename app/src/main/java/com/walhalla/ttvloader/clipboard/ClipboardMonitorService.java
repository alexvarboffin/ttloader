package com.walhalla.ttvloader.clipboard;

import android.app.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import android.text.TextUtils;
import android.util.Log;

import com.android.widget.Config;
import com.walhalla.notofication.NotificationUtils;
import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ttvloader.TTResponse;
import com.walhalla.extractors.presenters.RepositoryCallback;
import com.walhalla.extractors.presenters.VideoRepository;
import com.walhalla.ttvloader.utils.ServiceUtils;
import com.walhalla.ttvloader.utils.Utils;
import com.walhalla.ui.DLog;

import static com.android.widget.Config.KEY_TKT_LOADER;
import static com.android.widget.Config.START_FOREGROUND_ACTION;
import static com.android.widget.Config.STOP_FOREGROUND_ACTION;


/*
* LIMITATION
* //Android Q == Android 10 sdk29
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            }
*
* */
public class ClipboardMonitorService extends Service implements RepositoryCallback {


    private Handler handler;

    private ClipboardManager manager;
    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener;

    IBinder mBinder;

    SharedPreferences prefs;
    int mStartMode;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        handler = new Handler();
        VideoRepository repository = new VideoRepository(getApplicationContext(), ClipboardMonitorService.this, handler);


        mOnPrimaryClipChangedListener = () -> {
            ClipData mm0 = manager.getPrimaryClip();
            String newClip;
            if (mm0 != null) {
                newClip = mm0.getItemAt(0).getText().toString();
                //   Toast.makeText(getApplicationContext(), newClip, Toast.LENGTH_LONG).show();
                Log.i("LOGClipboard", newClip + "");
                repository.makeDownload(newClip, true, true);
            }
        };

        if (manager != null) {
            manager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = (intent.getAction() == null) ? "" : intent.getAction();
        switch (action) {

            case Config.START_FOREGROUND_ACTION:
                Notification notification = NotificationUtils.makeServiceNotification(getApplicationContext(), ClipboardMonitorService.class);
                prefs = getSharedPreferences(KEY_TKT_LOADER, Context.MODE_PRIVATE);

                //stopSelf();
                startForeground(1002, notification);
                break;

            case STOP_FOREGROUND_ACTION:
                stopForegroundService();
                break;
            default:
                DLog.d("Null pointer");
                break;
        }
        return START_STICKY;
    }

    private void stopForegroundService() {

        Log.d("Foreground", "Stop foreground service.");
        prefs = getSharedPreferences(KEY_TKT_LOADER, MODE_PRIVATE);
        prefs.edit().putBoolean(Config.KEY_CLIPBOARD_MONITOR, false).apply();

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
        manager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        //Android 12  [ api >= 23]
        final int flag0 = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M
                ? PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
                : PendingIntent.FLAG_ONE_SHOT;

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                restartServiceIntent, flag0);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
        // this.stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("destroyed", "123123");
        stopForeground(true);
        stopSelf();
        if (manager != null) {
            manager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);
        }
        prefs = getSharedPreferences(KEY_TKT_LOADER, MODE_PRIVATE);

        if (prefs.getBoolean(Config.KEY_CLIPBOARD_MONITOR, false)) {
//                Intent broadcastIntent = new Intent();
//                broadcastIntent.setAction("restartservice");
//                broadcastIntent.setClass(this, Restarter.class);
//                this.sendBroadcast(broadcastIntent);
        }
    }


    @Override
    public void successResult(TTResponse result) {
        if (BuildConfig.DEBUG) {
            String target = result.cleanVideo;
            if (TextUtils.isEmpty(target)) {
                target = result.contentURL;
            }
            if (!TextUtils.isEmpty(target)) {
                Utils.ShowToast0(getApplicationContext(), target);
            }
        }
    }


    @Override
    public void showProgressDialog() {
        //...
    }

    @Override
    public void hideProgressDialog() {
        //...
    }

    @Override
    public void errorResult(int err) {
        Utils.ShowErrorToast0(getApplicationContext(), err);
    }

    @Override
    public void errorResult(String error) {
        Utils.ShowToast0(getApplicationContext(), error);
    }

    //===========================================================================================
    public static void startClipboardMonitor(Context context) {
        if (!ServiceUtils.serviceIsRunningInForeground(context, ClipboardMonitorService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Toast.makeText(context, "startClipboardMonitor", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ClipboardMonitorService.class);
                intent.setAction(START_FOREGROUND_ACTION);
                ComponentName service = context.startForegroundService(intent);
            } else {
                ComponentName service = context.startService(new Intent(context, ClipboardMonitorService.class));
            }
        }
    }


}

