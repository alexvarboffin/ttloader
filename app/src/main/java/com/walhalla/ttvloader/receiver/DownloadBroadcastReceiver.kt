package com.walhalla.ttvloader.receiver

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.util.Log
import com.android.widget.Config
import com.walhalla.ttvloader.clipboard.ClipboardMonitorService
import com.walhalla.ttvloader.services.GalleryService
import com.walhalla.ui.DLog
import java.io.File

class DownloadBroadcastReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        DLog.d("mmmmmmmmmmmmmmmmmmmmmmm")
        if (intent == null) {
            return
        }
        if (intent.getExtras() != null) {
            var downloadId: Long = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            var query: DownloadManager.Query = DownloadManager.Query()
            query.setFilterById(downloadId)
            var manager: DownloadManager = (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
            var cursor: Cursor = manager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                var status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                var reason: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                var totalSize: Long = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                var bytesDownloaded: Long = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                var title: String = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                var description: String = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                var localUri: String = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                Log.d("@", "onReceive: " + totalSize + "::" + bytesDownloaded)
            }
            if (cursor != null) {
                cursor.close()
            }
        }
        var action: String = if (intent.getAction() == null) "" else intent.getAction()!!
        var prefs: SharedPreferences = context.getSharedPreferences(com.android.widget.Config.KEY_TKT_LOADER, Context.MODE_PRIVATE)
        //static FloatingViewService service;
        var editor: SharedPreferences.Editor = prefs.edit()
        when (action) {
            android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
            }
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                //api 19 fail
                try {
                    //File file = new File(SharedObjects.externalMemory().absolutePath + File.separator + Constants.DOWNLOAD_DIRECTORY);
                    var file: File = Config.videoFolder(context)
                    if (file.exists() && file.isDirectory()) {
                        //File[] mm = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).listFiles();
                        DLog.d("[download-complete]" + file.getAbsolutePath())
                    }
                    //                        File[] mm = file.listFiles();
                    //                        if (mm != null) {
                    //                            for (File value : mm) {
                    //                                DLog.d("***************" + value.getAbsolutePath());
                    //                            }
                    //                        }
                    refreshGallery(context, file)
                } catch (e: Exception) {
                    DLog.handleException(e)
                }
            }
            "quit_action" -> {
                Log.e("loged", "quite")
                editor.putBoolean(Config.KEY_CLIPBOARD_MONITOR, false)
                editor.apply()
                context.stopService(Intent(context, ClipboardMonitorService::class.java))
                return
            }
            else -> {
            }
        }
    }
    //ReceiverCallNotAllowedException @ MyBroadcastReceiver components are not allowed to bind to services
    private fun refreshGallery(context: Context, file: File) {
        //        try{
        //            MediaScannerConnection.scanFile(context,
        //                    new String[]{file.toString()}, null,
        //                    (path, uri) -> DLog.d("image is saved in gallery and gallery is refreshed.")
        //            );
        //        } catch (Exception e) {
        //            DLog.handleException(e);
        //        }
        // Запустите службу для обновления галереи
        var serviceIntent: Intent = Intent(context, GalleryService::class.java)
        serviceIntent.putExtra("file", file)
        context.startService(serviceIntent)
    }
}
