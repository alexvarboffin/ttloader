package com.retrytech.medialoot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public abstract class MediaTools {

    public static Intent buildSharedIntent(Context context, File imageFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/mp4");
            shareIntent.putExtra(Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imageFile));
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, imageFile.getName());
            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            return chooserIntent;
        } else {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/mp4");
            Uri uri = Uri.parse("file://" + imageFile.getAbsolutePath());
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, imageFile.getName());
            return shareIntent;

        }
    }

    public static Intent buildOpenIntent(Context context, File imageFile) {

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imageFile);

        } else {
            contentUri = Uri.parse("file://" + imageFile.getAbsolutePath());
        }
        openIntent.setDataAndType(contentUri, "video/mp4");
        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return openIntent;
    }
}
