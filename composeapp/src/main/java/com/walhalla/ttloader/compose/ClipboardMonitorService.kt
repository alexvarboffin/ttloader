package com.walhalla.ttloader.compose

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.android.widget.Config
import com.walhalla.extractors.presenters.RepositoryCallback
import com.walhalla.extractors.presenters.VideoRepository
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ui.DLog

class ClipboardMonitorService : Service(), RepositoryCallback {
    private var manager: ClipboardManager? = null
    private var listener: ClipboardManager.OnPrimaryClipChangedListener? = null
    private var repository: VideoRepository? = null

    override fun onCreate() {
        super.onCreate()
        manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val handler = Handler(Looper.getMainLooper())
        repository = VideoRepository(applicationContext, this, handler)
        listener = ClipboardManager.OnPrimaryClipChangedListener {
            val clip = manager?.primaryClip ?: return@OnPrimaryClipChangedListener
            val text = clip.getItemAt(0).text?.toString() ?: return@OnPrimaryClipChangedListener
            if (text.isNotBlank()) {
                repository?.makeDownload(text, true, true)
            }
        }
        manager?.addPrimaryClipChangedListener(listener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ""
        if (action == Config.STOP_FOREGROUND_ACTION) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }
        startForeground(1002, notification())
        return START_STICKY
    }

    private fun notification(): Notification {
        val channelId = "clipboard_monitor"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Clipboard monitor", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        val pending = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Watching clipboard for video links")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentIntent(pending)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        listener?.let { manager?.removePrimaryClipChangedListener(it) }
        super.onDestroy()
    }

    override fun successResult(result: TTResponse) {
        DLog.d("clipboard success " + result.title)
    }

    override fun errorResult(error: String) {
        DLog.d(error)
    }

    override fun showProgressDialog() {}
    override fun hideProgressDialog() {}
    override fun errorResult(errWwwNotSupport: Int) {
        DLog.d("clipboard error " + errWwwNotSupport)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ClipboardMonitorService::class.java)
            intent.action = Config.START_FOREGROUND_ACTION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, ClipboardMonitorService::class.java)
            intent.action = Config.STOP_FOREGROUND_ACTION
            context.startService(intent)
        }
    }
}
