package com.walhalla.ttvloader.clipboard

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
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
import com.walhalla.extractors.presenters.RepositoryCallback
import com.walhalla.extractors.presenters.VideoRepository
import com.walhalla.notofication.NotificationUtils
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ttvloader.utils.ServiceUtils.serviceIsRunningInForeground
import com.walhalla.ttvloader.utils.Utils
import com.walhalla.ui.DLog.d
import androidx.core.content.edit

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
    private var mOnPrimaryClipChangedListener: OnPrimaryClipChangedListener? = null

    var mBinder: IBinder? = null

    var prefs: SharedPreferences? = null
    var mStartMode: Int = 0

    override fun onCreate() {
        super.onCreate()
        manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        handler = Handler()
        val repository =
            VideoRepository(applicationContext, this@ClipboardMonitorService, handler)


        mOnPrimaryClipChangedListener = OnPrimaryClipChangedListener {
            val mm0 = manager!!.getPrimaryClip()
            val newClip: String?
            if (mm0 != null) {
                newClip = mm0.getItemAt(0).getText().toString()
                //   Toast.makeText(getApplicationContext(), newClip, Toast.LENGTH_LONG).show();
                Log.i("LOGClipboard", newClip + "")
                repository.makeDownload(newClip, true, true)
            }
        }

        if (manager != null) {
            manager!!.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener)
        }
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = if (intent.action == null) "" else intent.action
        when (action) {
            Config.START_FOREGROUND_ACTION -> {
                val notification = NotificationUtils.makeServiceNotification(
                    applicationContext,
                    ClipboardMonitorService::class.java
                )
                prefs = getSharedPreferences(Config.KEY_TKT_LOADER, MODE_PRIVATE)

                //stopSelf();
                startForeground(1002, notification)
            }

            Config.STOP_FOREGROUND_ACTION -> stopForegroundService()
            else -> d("Null pointer")
        }
        return START_STICKY
    }

    private fun stopForegroundService() {
        Log.d("Foreground", "Stop foreground service.")
        prefs = getSharedPreferences(Config.KEY_TKT_LOADER, MODE_PRIVATE)
        prefs!!.edit { putBoolean(Config.KEY_CLIPBOARD_MONITOR, false) }

        // Stop foreground service and remove the notification.
        stopForeground(true)

        // Stop the foreground service.
        stopSelf()
        manager!!.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener)
    }


    override fun onBind(intent: Intent?): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(getApplicationContext(), this.javaClass)
        restartServiceIntent.setPackage(getPackageName())

        //Android 12  [ api >= 23]
        val flag0 = if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.M
        )
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        else
            PendingIntent.FLAG_ONE_SHOT

        val restartServicePendingIntent = PendingIntent.getService(
            getApplicationContext(), 1,
            restartServiceIntent, flag0
        )
        val alarmService = getApplicationContext().getSystemService(ALARM_SERVICE) as AlarmManager
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        )

        super.onTaskRemoved(rootIntent)
        // this.stopSelf();
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("destroyed", "123123")
        stopForeground(true)
        stopSelf()
        if (manager != null) {
            manager!!.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener)
        }
        prefs = getSharedPreferences(Config.KEY_TKT_LOADER, MODE_PRIVATE)

        if (prefs!!.getBoolean(Config.KEY_CLIPBOARD_MONITOR, false)) {
//                Intent broadcastIntent = new Intent();
//                broadcastIntent.setAction("restartservice");
//                broadcastIntent.setClass(this, Restarter.class);
//                this.sendBroadcast(broadcastIntent);
        }
    }


    override fun successResult(result: TTResponse) {
        if (BuildConfig.DEBUG) {
            var target = result.cleanVideo
            if (TextUtils.isEmpty(target)) {
                target = result.contentURL
            }
            if (!TextUtils.isEmpty(target)) {
                Utils.ShowToast0(applicationContext, target)
            }
        }
    }


    override fun showProgressDialog() {
        //...
    }

    override fun hideProgressDialog() {
        //...
    }

    override fun errorResult(err: Int) {
        Utils.ShowErrorToast0(applicationContext, err)
    }

    override fun errorResult(error: String?) {
        Utils.ShowToast0(applicationContext, error)
    }

    companion object {
        //===========================================================================================
        @JvmStatic
        fun startClipboardMonitor(context: Context) {
            if (!serviceIsRunningInForeground(context, ClipboardMonitorService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Toast.makeText(context, "startClipboardMonitor", Toast.LENGTH_SHORT).show();
                    val intent = Intent(context, ClipboardMonitorService::class.java)
                    intent.setAction(Config.START_FOREGROUND_ACTION)
                    val service = context.startForegroundService(intent)
                } else {
                    val service =
                        context.startService(Intent(context, ClipboardMonitorService::class.java))
                }
            }
        }
    }
}

