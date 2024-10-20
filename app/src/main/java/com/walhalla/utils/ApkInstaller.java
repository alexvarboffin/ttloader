package com.walhalla.utils;

import static com.walhalla.intentresolver.utils.UriUtils.getUriFromFile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApkInstaller {

    private static final String TAG = "@@@";

    public static void downloadAndInstallApk(Context context, String apkUrl) {
        new DownloadApkTask(context).execute(apkUrl);
    }

    private static class DownloadApkTask extends AsyncTask<String, Void, File> {

        private Context context;

        DownloadApkTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String apkUrl = params[0];
            File apkFile = null;
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                File downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                if (downloadDir != null) {
                    apkFile = new File(downloadDir, "F-Droid.apk");
                    try (InputStream inputStream = connection.getInputStream();
                         FileOutputStream outputStream = new FileOutputStream(apkFile)) {

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, len);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Download error: " + e.getMessage(), e);
            }
            return apkFile;
        }

        @Override
        protected void onPostExecute(File apkFile) {
            if (apkFile != null && apkFile.exists()) {
                installApk(context, apkFile);
            } else {
                Log.e(TAG, "Failed to download APK.");
            }
        }

        private void installApk(Context context, File apkFile) {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setDataAndType(getUriFromFile(context, apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        }
    }
}
