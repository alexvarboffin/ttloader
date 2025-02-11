package com.walhalla.intentresolver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object UriUtils {
    const val KEYFILEPROVIDER: String = ".fileprovider"

    @JvmStatic
    @SuppressLint("ObsoleteSdkInt")
    @Throws(IllegalArgumentException::class)
    fun getUriFromFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || file.isDirectory) {
            Uri.fromFile(file)
        } else {
            FileProvider.getUriForFile(
                context,
                context.packageName + KEYFILEPROVIDER,
                file
            )
        }
    }
    //    public static Uri makeURI(Context context, @NonNull File file) throws IllegalArgumentException {
    //
    //        if (file.isDirectory()) {
    //            return Uri.fromFile(file);//Not use FileProvider is Directory
    //        }
    //        if (Build.VERSION.SDK_INT >= 23) {
    //            return FileProvider.getUriForFile(context, context.getPackageName() + KEYFILEPROVIDER, file);
    //
    //            //java.lang.IllegalArgumentException: Failed to find configured root that contains
    ////            try {
    ////                return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
    ////            } catch (java.lang.IllegalArgumentException e) {
    ////                DLog.handleException(e);
    ////                return Uri.fromFile(file);
    ////            }
    //        } else {
    //            return Uri.fromFile(file);
    //        }
    //    }
}
