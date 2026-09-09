package com.walhalla.notofication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.activity.main.MainActivity
import com.walhalla.ttvloader.receiver.DownloadBroadcastReceiver
import com.walhalla.ui.DLog

class NotificationUtils {
    private val NotifyID: Int = 1001
    //Android 12  [ api >= 23]
    @SuppressLint("MissingPermission") private fun setNotificationDM(context: Context, b: Boolean) {
        var packageName: String = context.getPackageName()
        var appName: String = context.getString(R.string.app_name)
        //Android 12  [ api >= 23]
        val flag0: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 0 or PendingIntent.FLAG_IMMUTABLE else 0
        if (b) {
            var channelId: String = packageName + "-" + appName
            var builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            builder.setSmallIcon(R.drawable.ic_notification)
            // 3
            // setStyle(NotificationCompat.)
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification))
            builder.setContentTitle(context.getString(R.string.autoDownloadService))
            // 4
            builder.setContentText("Copy the link of video to start download")
            // 5
            builder.setOngoing(true)
            builder.setPriority(NotificationCompat.PRIORITY_LOW)
            // 7
            builder.setSound(null)
            builder.setOnlyAlertOnce(true)
            builder.setAutoCancel(false)
            builder.addAction(R.drawable.ic_notification, "Stop", makePendingIntent(context, "quit_action", DownloadBroadcastReceiver::class.java))
            var intent: Intent = Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            var pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag0)
            builder.setContentIntent(pendingIntent)
            var compat: NotificationManagerCompat = NotificationManagerCompat.from(context)
            compat.notify(NotifyID, builder.build())
            DLog.d("testing notification notify!")
        } else {
            NotificationManagerCompat.from(context).cancel(NotifyID)
        }
    }
    companion object {
        @JvmStatic fun makeServiceNotification(context: Context, clazz: Class<*>): Notification {
            var packageName: String = context.getPackageName()
            var appName: String = context.getString(R.string.app_name)
            val flag0: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 0 or PendingIntent.FLAG_IMMUTABLE else 0
            var notificationIntent: Intent = Intent(context, MainActivity::class.java)
            var pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, flag0)
            val channelId: String = "ForegroundServiceChannel"
            var channelName: String = packageName + "-" + appName
            if (Build.VERSION.SDK_INT >= 26) {
                var channel: NotificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                channel.setDescription("Tiktok Auto Download")
                channel.setSound(null, null)
                var notificationManager: NotificationManager = (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel)
                }
            }
            var builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId).setSmallIcon(R.drawable.ic_notification).setContentTitle(context.getString(R.string.autoDownloadService)).setContentText("Copy the link of video to start download").setTicker("TICKER").addAction(R.drawable.ic_notification, context.getString(R.string.clipboard_service_action_stop), makePendingIntent(context, com.android.widget.Config.STOP_FOREGROUND_ACTION, clazz)).setContentIntent(pendingIntent).setSound(null)
            return builder.build()
        }
        @JvmStatic fun makePendingIntent(context: Context, name: String, clazz: Class<*>): PendingIntent {
            //Android 12  [ api >= 23]
            val flag0: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 0 or PendingIntent.FLAG_IMMUTABLE else 0
            var intent: Intent = Intent(context, clazz)
            intent.setAction(name)
            return PendingIntent.getService(context, 0, intent, flag0)
        }
    }
}
