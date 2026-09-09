package com.walhalla.ttvloader.receiver

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.android.widget.Config
import android.widget.Toast
import com.walhalla.ui.DLog
import java.io.File
import java.util.HashSet

class DownloadFile {
    private var longHashSet: HashSet<Long> = HashSet()
    private constructor() {
    }
    /**
         *
         * P
         * Permission must be granted
         * P
         * P
         */
    fun makeLoad77(context: Context, url: String, fileName: String, fileExt: String) {
        try {
            var fullFileName: String = makeFileName(fileName, fileExt)
            DLog.d("[+][+] DownloadFileName: " + fullFileName + "\t\t" + url)
            var manager: DownloadManager = (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
            var request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(fileName)
            request.setDescription("Downloading")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            }
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //request.setDestinationUri(Uri.fromFile(new File(mBaseFolderPath)));
            //ERROR >> request.setDestinationInExternalPublicDir(dir, fullFileName);
            //ERROR >> request.setDestinationInExternalPublicDir(mBaseFolderPath, fullFileName);
            //request.setDestinationInExternalFilesDir(context, mBaseFolderPath, fullFileName);
            //new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            var file: File = File(Config.videoFolder(context), fullFileName)
            var uri: Uri = Uri.fromFile(file)
            //Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            //Work in old --> request.setDestinationUri(uri);
            //aka Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            // Config.videoFolder(context).getAbsolutePath()
            //request.setDestinationUri(uri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, fullFileName)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                request.allowScanningByMediaScanner()
                request.setVisibleInDownloadsUi(true)
            }
            if (manager != null) {
                longHashSet.add(manager.enqueue(request))
            }
            //DLog.d("[+] " + dir + " :: " + mBaseFolderPath);
            Toast.makeText(context, "Downloading Start!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            DLog.handleException(e)
        }
    }
    private fun makeFileName(fileName: String, fileExt: String): String {
        //        String fullFileName = fileName.trim();
        //        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        //        fullFileName = fullFileName.replace(characterFilter, "");
        //        fullFileName = fullFileName.replace("['+.^:,#\"]", "");
        //        fullFileName = fullFileName.replace(" ", "_")
        //                .replace("!", "")
        //                .replace("@", "_")
        //                .replace(":", "") + fileExt;
        //
        //
        //        //Remove Emoji
        //        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        //        fullFileName = fullFileName.replace(regex, "");
        //
        //        if (fullFileName.length > 100) {
        //            fullFileName = fullFileName.substring(0, 100) + fileExt;
        //        } else {
        //            fullFileName = fullFileName + fileExt;
        //        }
        //        DLog.d(fileName);
        //        return fileName;
        return "" + System.currentTimeMillis() + fileExt
    }
    companion object {
        private var instance: DownloadFile? = null
        @JvmStatic fun newInstance(): DownloadFile {
            if (instance == null) {
                instance = DownloadFile()
            }
            return instance!!
        }
    }
}
