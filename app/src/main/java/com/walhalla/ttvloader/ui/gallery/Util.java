package com.walhalla.ttvloader.ui.gallery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ui.DLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Util {

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

//    public static String getDate(PackageMeta meta) {
//
//        try {
////            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
////            Date netDate = (new Date(meta));
////            return sdf.format(netDate);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
//            String fut = sdf.format(new Date(meta.firstInstallTime));
//            String lut = sdf.format(new Date(meta.lastUpdateTime));
//
//            return "First/Last update time:\n" + fut + "\t" + lut;
//        } catch (Exception ex) {
//            return "xx";
//        }
//    }
    /*
    StringBuilder sb = new StringBuilder();
        sb.append(p.sharedUserId).append((char)10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sb.append(p.baseRevisionCode).append((char)10);
        }
        sb.append(p.firstInstallTime).append((char)10);
        sb.append(p.lastUpdateTime).append((char)10);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sb.append(p.installLocation).append((char)10);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            sb.append(p.isApex).append((char)10);
        }

        sb.append(p.sharedUserLabel).append((char)10);
        sb.append(Arrays.toString(p.activities)).append((char)10);
        sb.append(Arrays.toString(p.gids)).append((char)10);
        sb.append(Arrays.toString(p.permissions)).append((char)10);
        sb.append(Arrays.toString(p.providers)).append((char)10);
        sb.append(Arrays.toString(p.receivers)).append((char)10);

        return sb.toString();*/

    public static long getFolderSize(File folderOrFile) {
        long length = 0;
        if (folderOrFile.isFile()) {
            length = folderOrFile.length();
        } else {
            // listFiles() is used to list the
            // contents of the given folder
            File[] files = folderOrFile.listFiles();

            if (files != null) {
                int count = files.length;
                // loop for traversing the directory
                for (int i = 0; i < count; i++) {
                    if (files[i].isFile()) {
                        length += files[i].length();
                    } else {
                        length += getFolderSize(files[i]);
                    }
                }
            }
        }
        return length;
    }

    public static String getFileSizeMegaBytes(File file) {
        return String.format(Locale.CANADA, "%.2f MB", (double) getFolderSize(file) / (1024 * 1024));
    }


    ///data/app/SmokeTestApp/SmokeTestApp.apk
    ///storage/emulated/0/Download

    public static void openFolder(Context context, String var0) {

//        if (!var0.isDirectory()) {
//            Toast.makeText(context, R.string.access_error, Toast.LENGTH_SHORT).show();
//            return;
//        }


        if (var0 != null) {
            //Warning! Do this if it's directory
            //No need FileProvider


            Uri uri = Uri.parse(var0);//It's ok
            //Uri uri = Uri.fromFile(new File(var0)); //Exception

//                    Intent intent = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
//                    }

            PackageManager pm = context.getPackageManager();
            // if you reach this place, it means there is no any file
            // explorer app installed on your device

//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "*/*");
//            context.startActivity(intent);

            if (check_Android30FileBrowser_AndroidLysesoftFileBrowser(uri, context, pm, true)) {
                return;
            }


            //api 24 not open folder? not have filebrowser((
        }
    }

    private static void checkResolver(Intent intent, PackageManager pm) {
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : resolvedActivityList) {
            DLog.d("------------[][][" + info.toString());

            Intent serviceIntent = intent;//new Intent();
            //serviceIntent.setAction(Intent.ACTION_VIEW);
            //serviceIntent.setPackage(info.activityInfo.packageName);
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                //packagesSupportingCustomTabs.add(info);
                DLog.d("-----0------" + info.toString());
            } else if (pm.resolveActivity(serviceIntent, 0) != null) {
                DLog.d("-----1------" + info.toString());
            }

        }
    }


    private static void resolwe(Intent intent, PackageManager pm) {
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : resolvedActivityList) {
            DLog.d("------------[][][" + info.toString());

            Intent serviceIntent = new Intent();
            serviceIntent.setAction(Intent.ACTION_VIEW);
            //serviceIntent.setPackage(info.activityInfo.packageName);
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                //packagesSupportingCustomTabs.add(info);
                DLog.d("-----0------" + info.toString());
            } else if (pm.resolveActivity(serviceIntent, 0) != null) {
                DLog.d("-----1------" + info.toString());
            }

        }
    }


    public static boolean check_Android30FileBrowser_AndroidLysesoftFileBrowser(
            Uri uri, Context context, PackageManager pm, boolean launch) {

        String[] m = new String[]{
                "vnd.android.document/directory", //=WORK in Android 30=
                "vnd.android.cursor.dir/lysesoft.andexplorer.director",
                //"vnd.android.cursor.dir/*"
        };

        for (String type : m) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, type);
            ActivityInfo mm = intent.resolveActivityInfo(pm, 0);
            if (mm != null) {
                DLog.d("[@@]" + uri + " " + mm + " " + type);
                if (launch) {
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        DLog.handleException(e);
                    }
                }
                return true;
            }
        }
        return false;
    }


    /**
     * It's error
     * <p>
     * intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
     * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * <p>
     * Success
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
     * | Intent.FLAG_GRANT_READ_URI_PERMISSION);
     */

    public static String packageName(ResolveInfo info) {
        String zzz = null;
        if (info.activityInfo != null) {
            zzz = info.activityInfo.packageName;
        }
        if (info.serviceInfo != null) {
            zzz = info.serviceInfo.packageName;
        }
        return zzz;
    }

    public static String componentName(ResolveInfo info) {
        String zzz = null;
        if (info.activityInfo != null) {
            zzz = info.activityInfo.name;
        }
        if (info.serviceInfo != null) {
            zzz = info.serviceInfo.name;
        }
        return zzz;
    }

}
