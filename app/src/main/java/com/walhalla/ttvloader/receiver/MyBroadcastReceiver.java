package com.walhalla.ttvloader.receiver;

import android.app.DownloadManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.android.widget.Config;
import com.walhalla.ttvloader.clipboard.ClipboardMonitorService;
import com.walhalla.ttvloader.services.GalleryService;
import com.walhalla.ui.DLog;

import java.io.File;

import static com.android.widget.Config.KEY_TKT_LOADER;

public class MyBroadcastReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        if (intent.getExtras() != null) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                long bytesDownloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                Log.d("@@@", "onReceive: "+totalSize+"::"+bytesDownloaded);
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        String action = (intent.getAction() == null) ? "" : intent.getAction();
        SharedPreferences prefs = context.getSharedPreferences(KEY_TKT_LOADER, Context.MODE_PRIVATE);
        //static FloatingViewService service;
        SharedPreferences.Editor editor = prefs.edit();
        switch (action) {
            case android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED:
                break;

            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                //api 19 fail
                try {
                    //File file = new File(SharedObjects.externalMemory() + File.separator + Constants.DOWNLOAD_DIRECTORY);
                    File file = Config.videoFolder(context);
                    if (file.exists() && file.isDirectory()) {
                        //File[] mm = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).listFiles();
                        DLog.d("[download-complete]" + file.getAbsolutePath());

//                        File[] mm = file.listFiles();
//                        if (mm != null) {
//                            for (File value : mm) {
//                                DLog.d("***************" + value.getAbsolutePath());
//                            }
//                        }
                    }
                    refreshGallery(context, file);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
                break;

            case "quit_action":
                Log.e("loged", "quite");
                editor.putBoolean(Config.KEY_CLIPBOARD_MONITOR, false);
                editor.apply();
                context.stopService(new Intent(context,
                        ClipboardMonitorService.class));
                return;
            default:
        }
    }
    //ReceiverCallNotAllowedException @@@ MyBroadcastReceiver components are not allowed to bind to services
    private void refreshGallery(Context context, File file) {
//        try{
//            MediaScannerConnection.scanFile(context,
//                    new String[]{file.toString()}, null,
//                    (path, uri) -> DLog.d("image is saved in gallery and gallery is refreshed.")
//            );
//        } catch (Exception e) {
//            DLog.handleException(e);
//        }

        // Запустите службу для обновления галереи
        Intent serviceIntent = new Intent(context, GalleryService.class);
        serviceIntent.putExtra("file", file);
        context.startService(serviceIntent);
    }
}