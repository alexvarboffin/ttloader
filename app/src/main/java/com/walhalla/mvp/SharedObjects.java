package com.walhalla.mvp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.walhalla.abcsharedlib.SharedNetwork;

import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ui.DLog;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SharedObjects {

    private static final String KEY_IMAGES = "images";

    public static void shareeeeeeeeeef(Context context, File file, String text, String network) {

        switch (network) {

            case SharedNetwork.Package.TWITTER_LITE:
            case SharedNetwork.Package.TWITTER:
                SharedObjects.shareScreenshotToTwitter(context, text, file);
                break;

            default:
                SharedObjects.shareFile(context, text, file);
                break;
        }
    }

    public static File imageCacheDir(Context context) {
        File ff = new File(context.getCacheDir(), KEY_IMAGES);
        boolean mkdirs = ff.mkdirs();
        return ff;
    }

    public static File externalMemory() {
        File file = Environment.getExternalStorageDirectory();
        DLog.d("EXTERNAL_MEMORY: " + file.getAbsolutePath() + " " + file.isDirectory() + " " + file.canWrite());
        return file;
    }


    private static void shareFile(Context context, String message, File file) {
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

            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);

            if (contentUri != null) {

                //OR
                //Intent shareIntent = new Intent();
                //shareIntent.setAction(Intent.ACTION_SEND);
                //OR

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                if (message == null || message.trim().isEmpty()) {
                    message = "";
                }
                intent.putExtra(Intent.EXTRA_TEXT, message + " \n");
                //        intent.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
                //                Uri.parse("https://play.google.com/store/apps/details?id="
                //                        + context.getPackageName()))
                //        );


                // temp permission for receiving app to read this file
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                String type = context.getContentResolver().getType(contentUri);
                DLog.d("@@@@@@@@@: " + type);
                intent.setDataAndType(contentUri, type);
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);

                //OR
                //intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                //intent.setType("image/jpeg");

//                if (DEBUG) {
//                    DLog.d( "shareFile: " + intent.toString());
//                }
                context.startActivity(Intent.createChooser(intent, "Choose an app"));
            }
        } catch (StringIndexOutOfBoundsException e) {
            DLog.handleException(e);
        }
    }

    /**
     * Share image from cash folder to twitter
     */
    private static void shareScreenshotToTwitter(Context context, String message, File file) {

        try {
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            if (contentUri != null) {
                Intent www = new Intent(Intent.ACTION_SEND);
                www.setType("text/plain");
                if (message == null || message.trim().isEmpty()) {
                    message = //"Hey my friend check out this app\n https://play.google.com/store/apps/details?id="
                            "";
                }
                www.putExtra(Intent.EXTRA_TEXT, message + " \n");
                //        www.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
                //                Uri.parse("https://play.google.com/store/apps/details?id="
                //                        + context.getPackageName()))
                //        );
//            www.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("https://play.google.com/store/apps/details?id="
//                            + context.getPackageName()))
//            );
                // temp permission for receiving app to read this file
                www.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

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


                www.putExtra(Intent.EXTRA_STREAM, contentUri);
                www.setType("image/jpeg");

                PackageManager packManager = context.getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(www, PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for (ResolveInfo resolveInfo : resolvedInfoList) {
                    if (
                            resolveInfo.activityInfo.packageName.startsWith(SharedNetwork.Package.TWITTER)
                                    || resolveInfo.activityInfo.packageName.startsWith(SharedNetwork.Package.TWITTER_LITE)
                    ) {
                        www.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name);
                        resolved = true;
                        break;
                    }
                }
                if (resolved) {
                    context.startActivity(www);
                } else {
//            Intent i = new Intent();
//            i.putExtra(Intent.EXTRA_TEXT, message);
//            i.setAction(Intent.ACTION_VIEW);
//            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
//            context.startActivity(i);
                    Toast.makeText(context, "Twitter app isn't found", Toast.LENGTH_LONG).show();
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
//            if(BuildConfig.DEBUG){
//                DLog.d( "@@@: " + file.getAbsolutePath());
//            }
        }

    }


    public static void clearImageCacheDir(Context context) {
        File cache = SharedObjects.imageCacheDir(context);
        File[] files = cache.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean delete = file.delete();
            }
        }
    }
}