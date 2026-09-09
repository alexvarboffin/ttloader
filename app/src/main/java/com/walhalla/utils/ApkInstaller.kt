package com.walhalla.utils

import com.walhalla.intentresolver.utils.UriUtils.getUriFromFile
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ApkInstaller {
    private class DownloadApkTask : AsyncTask<String, Void, File> {
        private var context: Context? = null
        constructor(context: Context) {
            this.context = context
        }
        override fun doInBackground(vararg params: String): File? {
            var apkUrl: String = params[0]
            var apkFile: File? = null
            try {
                var url: URL = URL(apkUrl)
                var connection: HttpURLConnection = (url.openConnection() as HttpURLConnection)
                connection.setRequestMethod("GET")
                connection.connect()
                var downloadDir = context!!.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                if (downloadDir != null) {
                    apkFile = File(downloadDir, "F-Droid.apk")
                    connection.getInputStream().use { inputStream ->
                        FileOutputStream(apkFile).use { outputStream ->
                            var buffer: ByteArray = ByteArray(1024)
                            var len = inputStream.read(buffer)
                            while (len != -1) {
                                outputStream.write(buffer, 0, len)
                                len = inputStream.read(buffer)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Download error: " + e.message, e)
            }
            return apkFile
        }
        override fun onPostExecute(apkFile: File?) {
            if (apkFile != null && apkFile.exists()) {
                installApk(context!!, apkFile)
            } else {
                Log.e(TAG, "Failed to download APK.")
            }
        }
        private fun installApk(context: Context, apkFile: File) {
            var intent: Intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
            intent.setDataAndType(getUriFromFile(context, apkFile), "application/vnd.android.package-archive")
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        }
    }
    companion object {
        private const val TAG = "@"
        @JvmStatic fun downloadAndInstallApk(context: Context, apkUrl: String) {
            DownloadApkTask(context).execute(apkUrl)
        }
    }
}
