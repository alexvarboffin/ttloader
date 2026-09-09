package com.walhalla.ttvloader.ui.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.walhalla.ui.DLog
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class Troubleshooting {
    companion object {
        @JvmStatic fun streamLoader(context: Context, file: File): InputStream {
            DLog.d("###" + " --> " + file.getAbsolutePath())
            var inputStream: InputStream? = null
            if (Build.VERSION.SDK_INT >= 29) {
                //Android 10 level 29 (Android 10).
                //Uri fileUri = Uri.parse(file.getAbsolutePath());
                var fileUri: Uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file)
                //            final ParcelFileDescriptor pfd;
                //
                //            pfd = context.getContentResolver().openFileDescriptor(fileUri, "r", null);
                //            inputStream = new FileInputStream(pfd.getFileDescriptor());
                inputStream = context.getContentResolver().openInputStream(fileUri)
            } else {
                inputStream = FileInputStream(file)
            }
            return inputStream!!
        }
        @SuppressLint("ObsoleteSdkInt") @JvmStatic fun defLocation(): File {
            var file: File? = null
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                file = Environment.getExternalStorageDirectory()
            } else {
                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            }
            //file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            DLog.d("Out: " + file!!.getAbsolutePath() + " " + file!!.exists())
            return file!!
        }
    }
}
