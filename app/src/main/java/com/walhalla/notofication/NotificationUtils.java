package com.walhalla.notofication;

import static com.android.widget.Config.STOP_FOREGROUND_ACTION;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.activity.main.MainActivity;
import com.walhalla.ttvloader.receiver.MyBroadcastReceiver;
import com.walhalla.ui.DLog;

public class NotificationUtils {
    private final int NotifyID = 1001;


    public static Notification makeServiceNotification(Context context, Class<?> clazz) {
        String packageName = context.getPackageName();
        String appName = context.getString(R.string.app_name);

        //Android 12  [ api >= 23]
        final int flag0 = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M
                ? 0 | PendingIntent.FLAG_IMMUTABLE
                : 0;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, flag0);

        final String channelId = "ForegroundServiceChannel";
        String channelName = packageName + "-" + appName;


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Tiktok Auto Download");
            channel.setSound(null, null);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.autoDownloadService))
                .setContentText("Copy the link of video to start download")
                .setTicker("TICKER")
                .addAction(R.drawable.ic_notification,
                        context.getString(R.string.clipboard_service_action_stop), makePendingIntent(context, STOP_FOREGROUND_ACTION, clazz))
                .setContentIntent(pendingIntent)
                .setSound(null);
        return builder.build();
    }

    @SuppressLint("MissingPermission")
    private void setNotificationDM(Context context, boolean b) {
        String packageName = context.getPackageName();
        String appName = context.getString(R.string.app_name);
        //Android 12  [ api >= 23]
        final int flag0 = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M
                ? 0 | PendingIntent.FLAG_IMMUTABLE
                : 0;

        if (b) {
            String channelId = packageName + "-" + appName;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
            builder.setSmallIcon(R.drawable.ic_notification); // 3
            // setStyle(NotificationCompat.)
            builder.setLargeIcon(
                    BitmapFactory.decodeResource(
                            context.getResources(),
                            R.drawable.ic_notification
                    )
            );
            builder.setContentTitle(context.getString(R.string.autoDownloadService)); // 4
            builder.setContentText("Copy the link of video to start download"); // 5
            builder.setOngoing(true);
            builder.setPriority(NotificationCompat.PRIORITY_LOW); // 7
            builder.setSound(null);
            builder.setOnlyAlertOnce(true);
            builder.setAutoCancel(false);
            builder.addAction(R.drawable.ic_notification, "Stop", makePendingIntent(context, "quit_action", MyBroadcastReceiver.class));
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, flag0);
            builder.setContentIntent(pendingIntent);
            NotificationManagerCompat compat = NotificationManagerCompat.from(context);
            compat.notify(NotifyID, builder.build());
            DLog.d("testing notification notify!");
        } else {
            NotificationManagerCompat.from(context).cancel(NotifyID);
        }
    }

    public static PendingIntent makePendingIntent(Context context, String name, Class<?> clazz) {

        //Android 12  [ api >= 23]
        final int flag0 = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M
                ? 0 | PendingIntent.FLAG_IMMUTABLE
                : 0;
        Intent intent = new Intent(context, clazz);
        intent.setAction(name);
        return PendingIntent.getService(context, 0, intent, flag0);
    }

}
