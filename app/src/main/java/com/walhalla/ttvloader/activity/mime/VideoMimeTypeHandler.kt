package com.walhalla.ttvloader.activity.mime

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import java.util.ArrayList
import java.util.List

class VideoMimeTypeHandler {
    companion object {
        @JvmStatic fun getVideoHandlers(context: Context, mimeType: String): MutableList<ResolveInfo> {
            var intent: Intent = Intent(Intent.ACTION_VIEW)
            intent.setType(mimeType)
            var packageManager: PackageManager = context.getPackageManager()
            var resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            var videoHandlers: MutableList<ResolveInfo> = ArrayList()
            for (resolveInfo in resolveInfos) {
                if (isVideoHandler(resolveInfo)) {
                    videoHandlers.add(resolveInfo)
                }
            }
            return videoHandlers
        }
        @JvmStatic private fun isVideoHandler(resolveInfo: ResolveInfo): Boolean {
            // Здесь можно добавить дополнительную логику для определения того, является ли активность обработчиком видео
            // Например, можно проверить, имеет ли активность соответствующие разрешения или функции для обработки видео
            return true
        }
    }
}

// Пока просто возвращаем true
