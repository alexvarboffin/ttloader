package com.walhalla.ttvloader.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ui.DLog;

import java.io.File;

public class IntentUtils {



    //Warning CRASH if packageName == null
    public static void openSettingsForPackageName2(Context context, @NonNull String packageName) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }
    //Requred application installed!, not crash if packageName == null

    public static void openSettingsForPackageName(Context context, String packageName) {
        try {
            Intent intent = new Intent()
                    .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .setData(Uri.parse("package:" + packageName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public static void openFolder(Context context, String s) {

        final File file = new File(s);
        final String parent = new File(s).getParent();
        //DLog.d("@@@@@@@@@@" + parent);

        //Uri uri = Uri.parse(s);
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(parent));
//                    Intent intent = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
//                    }
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType(DocumentsContract.Document.MIME_TYPE_DIR);

            ActivityInfo tmp = intent.resolveActivityInfo(context.getPackageManager(), 0);
            if (tmp != null) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //startActivityForResult(intent, OPEN_REQUEST);
                context.startActivity(intent);
            } else {
                //DLog.d();
            }
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(uri, "text/csv");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(intent, "Open folder"));
        }
    }
}
