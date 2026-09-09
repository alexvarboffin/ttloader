package com.walhalla.mvp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.walhalla.abcsharedlib.SharedNetwork
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ui.DLog
import java.io.File
import java.io.IOException
import java.util.List

class SharedObjects {
    companion object {
        private const val KEY_IMAGES = "images"
        @JvmStatic fun shareeeeeeeeeef(context: Context, file: File, text: String, network: String) {
            when (network) {
                SharedNetwork.Package.TWITTER_LITE, SharedNetwork.Package.TWITTER -> {
                    SharedObjects.shareScreenshotToTwitter(context, text, file)
                }
                else -> {
                    SharedObjects.shareFile(context, text, file)
                }
            }
        }
        @JvmStatic fun imageCacheDir(context: Context): File {
            var ff: File = File(context.getCacheDir(), KEY_IMAGES)
            var mkdirs: Boolean = ff.mkdirs()
            return ff
        }
        @JvmStatic fun externalMemory(): File {
            var file: File = Environment.getExternalStorageDirectory()
            DLog.d("EXTERNAL_MEMORY: " + file.getAbsolutePath() + " " + file.isDirectory() + " " + file.canWrite())
            return file
        }
        @JvmStatic private fun shareFile(context: Context, message: String?, file: File) {
            //        File file1 = new File("/storage/emulated/0/");
            //        File[] aa = file1.listFiles();
            //        for (File file2 : aa) {
            //            DLog.d("#############" + file2);
            //        }
            //        try {
            //            boolean mm = file.createNewFile();
            //        } catch (IOException e) {
            //            DLog.handleException(e);
            //        }
            try {
                var contentUri: Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
                if (contentUri != null) {
                    //OR
                    //Intent shareIntent = new Intent();
                    //shareIntent.setAction(Intent.ACTION_SEND);
                    //OR
                    var intent: Intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/plain")
                    var msg = message
                    if (msg == null || msg.trim().isEmpty()) {
                        msg = ""
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, msg + " \n")
                    //        intent.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
                    //                Uri.parse("https://play.google.com/store/apps/details?id="
                    //                        + context.getPackageName()))
                    //        );
                    // temp permission for receiving app to read this file
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    var type: String = context.getContentResolver().getType(contentUri)!!
                    DLog.d("@: " + type)
                    intent.setDataAndType(contentUri, type)
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    //OR
                    //intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    //intent.setType("image/jpeg");
                    //                if (DEBUG) {
                    //                    DLog.d( "shareFile: " + intent.toString());
                    //                }
                    context.startActivity(Intent.createChooser(intent, "Choose an app"))
                }
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
        /**
             * Share image from cash folder to twitter
             */
        @JvmStatic private fun shareScreenshotToTwitter(context: Context, message: String?, file: File) {
            try {
                var contentUri: Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
                if (contentUri != null) {
                    var www: Intent = Intent(Intent.ACTION_SEND)
                    www.setType("text/plain")
                    var msg = message
                    if (msg == null || msg.trim().isEmpty()) {
                        msg = ""
                    }
                    //"Hey my friend check out this app\n https://play.google.com/store/apps/details?id="
                    www.putExtra(Intent.EXTRA_TEXT, msg + " \n")
                    //        www.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
                    //                Uri.parse("https://play.google.com/store/apps/details?id="
                    //                        + context.getPackageName()))
                    //        );
                    //            www.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
                    //                    Uri.parse("https://play.google.com/store/apps/details?id="
                    //                            + context.getPackageName()))
                    //            );
                    // temp permission for receiving app to read this file
                    www.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    //        switch (memoryType) {
                    //            case INTERNAL:
                    //
                    //                break;
                    //
                    //            case EXTERNAL:
                    //                break;
                    //
                    //            default:
                    //                break;
                    //        }
                    //        File imagePath = SharedObjects.imageCacheDir(context);
                    //        File file = new File(imagePath, imageName);
                    //From card
                    //File file = new File(ssssdddddd, imageName);
                    //contentUri = Uri.fromFile(file);
                    www.putExtra(Intent.EXTRA_STREAM, contentUri)
                    www.setType("image/jpeg")
                    var packManager: PackageManager = context.getPackageManager()
                    var resolvedInfoList = packManager.queryIntentActivities(www, PackageManager.MATCH_DEFAULT_ONLY)
                    var resolved: Boolean = false
                    for (resolveInfo in resolvedInfoList) {
                        if (resolveInfo.activityInfo.packageName.startsWith(SharedNetwork.Package.TWITTER) || resolveInfo.activityInfo.packageName.startsWith(SharedNetwork.Package.TWITTER_LITE)) {
                            www.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
                            resolved = true
                            break
                        }
                    }
                    if (resolved) {
                        context.startActivity(www)
                    } else {
                        //            Intent i = new Intent();
                        //            i.putExtra(Intent.EXTRA_TEXT, message);
                        //            i.setAction(Intent.ACTION_VIEW);
                        //            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
                        //            context.startActivity(i);
                        Toast.makeText(context, "Twitter app isn't found", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
            }
        }
        //            if(BuildConfig.DEBUG){
        //                DLog.d( "@: " + file.getAbsolutePath());
        //            }
        @JvmStatic fun clearImageCacheDir(context: Context) {
            var cache: File = SharedObjects.imageCacheDir(context)
            var files: Array<File> = cache.listFiles()
            if (files != null) {
                for (file in files) {
                    var delete: Boolean = file.delete()
                }
            }
        }
    }
}
