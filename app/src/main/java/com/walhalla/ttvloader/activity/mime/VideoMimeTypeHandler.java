package com.walhalla.ttvloader.activity.mime;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public class VideoMimeTypeHandler {


    public static List<ResolveInfo> getVideoHandlers(Context context, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(mimeType);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        List<ResolveInfo> videoHandlers = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (isVideoHandler(resolveInfo)) {
                videoHandlers.add(resolveInfo);
            }
        }

        return videoHandlers;
    }

    private static boolean isVideoHandler(ResolveInfo resolveInfo) {
        // Здесь можно добавить дополнительную логику для определения того, является ли активность обработчиком видео
        // Например, можно проверить, имеет ли активность соответствующие разрешения или функции для обработки видео
        return true; // Пока просто возвращаем true
    }
}
