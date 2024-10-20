package com.walhalla.ttvloader.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.walhalla.ui.DLog;

import java.io.File;

public class GalleryService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Получите файл из интента
        File file = (File) intent.getSerializableExtra("file");

        // Обновите галерею
        refreshGallery(getApplicationContext(), file);

        // Верните значение, указывающее на то, что служба должна оставаться работающей после завершения onStartCommand
        return START_NOT_STICKY;
    }

    private void refreshGallery(Context context, File file) {
        try {
            MediaScannerConnection.scanFile(context,
                    new String[]{file.toString()}, null,
                    (path, uri) -> DLog.d("image is saved in gallery and gallery is refreshed.")
            );
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
