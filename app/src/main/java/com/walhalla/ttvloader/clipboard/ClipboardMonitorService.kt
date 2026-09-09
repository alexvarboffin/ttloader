package com.walhalla.ttvloader.clipboard

import android.app.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import com.android.widget.Config
import com.walhalla.notofication.NotificationUtils
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ttvloader.TTResponse
import com.walhalla.extractors.presenters.RepositoryCallback
import com.walhalla.extractors.presenters.VideoRepository
import com.walhalla.ttvloader.utils.ServiceUtils
import com.walhalla.ttvloader.utils.Utils
import com.walhalla.ui.DLog

/*
* LIMITATION
* //Android Q == Android 10 sdk29
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            }
*
* */
class ClipboardMonitorService : Service(), RepositoryCallback {
    private var handler: Handler? = null
    private var manager: ClipboardManager? = null
    private var mOnPrimaryClipChangedListener: ClipboardManager.OnPrimaryClipChangedListener? = null
    var mBinder: IBinder? = null
    var prefs: SharedPreferences? = null
    var mStartMode: Int = 0
    override fun onCreate() {
        super.onCreate()
        manager = (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager)
        handler = Handler()
        var repository: VideoRepository = VideoRepository(getApplicationContext(), this, handler!!)
        mOnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
                var mm0 = manager!!.getPrimaryClip()
                var newClip: String? = null
                if (mm0 != null) {
                    newClip = mm0.getItemAt(0).getText().toString()
                    //   Toast.makeText(getApplicationContext(), newClip, Toast.LENGTH_LONG).show();
                    Log.i("LOGClipboard", newClip!! + "")
                    repository.makeDownload(newClip!!, true, true)
                }
                }
        if (manager!! != null) {
            manager!!.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener!!)
        }
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var action: String = if (intent.getAction() == null) "" else intent.getAction()!!
        when (action) {
            Config.START_FOREGROUND_ACTION -> {
                var notification: Notification = NotificationUtils.makeServiceNotification(getApplicationContext(), ClipboardMonitorService::class.java)
                prefs = getSharedPreferences(com.android.widget.Config.KEY_TKT_LOADER, Context.MODE_PRIVATE)
                //stopSelf();
                startForeground(1002, notification)
            }
            com.android.widget.Config.STOP_FOREGROUND_ACTION -> {
                stopForegroundService()
            }
            else -> {
                DLog.d("Null pointer")
            }
        }
        return START_STICKY
    }
    private fun stopForegroundService() {
        Log.d("Foreground", "Stop foreground service.")
        prefs = getSharedPreferences(com.android.widget.Config.KEY_TKT_LOADER, MODE_PRIVATE)
        prefs!!.edit().putBoolean(Config.KEY_CLIPBOARD_MONITOR, false).apply()
        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()
        manager!!.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener!!)
    }
    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }
    override fun onTaskRemoved(rootIntent: Intent) {
        var restartServiceIntent: Intent = Intent(getApplicationContext(), this.javaClass)
        restartServiceIntent.setPackage(getPackageName())
        //Android 12  [ api >= 23]
        val flag0: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_ONE_SHOT
        var restartServicePendingIntent: PendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, flag0)
        var alarmService: AlarmManager = (getApplicationContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmService.`set`(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent)
        super.onTaskRemoved(rootIntent)
    }
    // this.stopSelf();
    override fun onDestroy() {
        super.onDestroy()
        Log.i("destroyed", "123123")
        stopForeground(true)
        stopSelf()
        if (manager!! != null) {
            manager!!.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener!!)
        }
        prefs = getSharedPreferences(com.android.widget.Config.KEY_TKT_LOADER, MODE_PRIVATE)
        if (prefs!!.getBoolean(Config.KEY_CLIPBOARD_MONITOR, false)) {
        }
    }
    //                Intent broadcastIntent = new Intent();
    //                broadcastIntent.setAction("restartservice");
    //                broadcastIntent.setClass(this, Restarter.class);
    //                this.sendBroadcast(broadcastIntent);
    override fun successResult(result: TTResponse) {
        if (BuildConfig.DEBUG) {
            var target: String = result.cleanVideo!!
            if (TextUtils.isEmpty(target)) {
                target = result.contentURL ?: ""
            }
            if (TextUtils.isEmpty(target)) {
                Utils.ShowToast0(getApplicationContext(), target)
            }
        }
    }
    override fun showProgressDialog() {
    }
    //...
    override fun hideProgressDialog() {
    }
    //...
    override fun errorResult(err: Int) {
        Utils.ShowErrorToast0(getApplicationContext(), err)
    }
    override fun errorResult(error: String) {
        Utils.ShowToast0(getApplicationContext(), error)
    }
    companion object {
        //===========================================================================================
        @JvmStatic fun startClipboardMonitor(context: Context) {
            if (!ServiceUtils.serviceIsRunningInForeground(context, ClipboardMonitorService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Toast.makeText(context, "startClipboardMonitor", Toast.LENGTH_SHORT).show();
                    var intent: Intent = Intent(context, ClipboardMonitorService::class.java)
                    intent.setAction(com.android.widget.Config.START_FOREGROUND_ACTION)
                    var service = context.startForegroundService(intent)
                } else {
                    var service = context.startService(Intent(context, ClipboardMonitorService::class.java))
                }
            }
        }
    }
}
