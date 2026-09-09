package com.walhalla.ttvloader.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.IBinder
import androidx.annotation.Nullable
import com.walhalla.ui.DLog
import java.io.File

class GalleryService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Получите файл из интента
        var file: File = (intent.getSerializableExtra("file") as File)
        // Обновите галерею
        refreshGallery(getApplicationContext(), file)
        // Верните значение, указывающее на то, что служба должна оставаться работающей после завершения onStartCommand
        return START_NOT_STICKY
    }
    private fun refreshGallery(context: Context, file: File) {
        try {
            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, {
                        path, uri -> DLog.d("image is saved in gallery and gallery is refreshed.")
                        })
        } catch (e: Exception) {
            DLog.handleException(e)
        }
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
