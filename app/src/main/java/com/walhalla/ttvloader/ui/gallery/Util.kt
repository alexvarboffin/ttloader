package com.walhalla.ttvloader.ui.gallery

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ui.DLog
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.List
import java.util.Locale
import es.dmoral.toasty.Toasty

class Util {
    companion object {
        @JvmStatic fun getMimeType(url: String): String? {
            var type: String? = null
            var extension: String = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
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
        @JvmStatic fun getFolderSize(folderOrFile: File): Long {
            var length: Long = 0
            if (folderOrFile.isFile()) {
                length = folderOrFile.length()
            } else {
                // listFiles() is used to list the
                // contents of the given folder
                var files: Array<File>? = folderOrFile.listFiles()
                if (files != null) {
                    var count: Int = files.size
                    // loop for traversing the directory
                    var i: Int = 0
                    while (i < count) {
                        if (files[i].isFile()) {
                            length += files[i].length()
                        } else {
                            length += getFolderSize(files[i])
                        }
                        i++
                    }
                }
            }
            return length
        }
        @JvmStatic fun getFileSizeMegaBytes(file: File): String {
            return String.format(Locale.CANADA, "%.2f MB", (getFolderSize(file)).toDouble() / 1024 * 1024)
        }
        ///data/app/SmokeTestApp/SmokeTestApp.apk
        ///storage/emulated/0/Download
        @JvmStatic fun openFolder(context: Context, var0: String) {
            //        if (!var0.isDirectory()) {
            //            Toast.makeText(context, R.string.access_error, Toast.LENGTH_SHORT).show();
            //            return;
            //        }
            if (var0 != null) {
                //Warning! Do this if it's directory
                //No need FileProvider
                var uri: Uri = Uri.parse(var0)
                //It's ok
                //Uri uri = Uri.fromFile(new File(var0)); //Exception
                //                    Intent intent = null;
                //                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                //                        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
                //                    }
                var pm: PackageManager = context.getPackageManager()
                // if you reach this place, it means there is no any file
                // explorer app installed on your device
                //            intent = new Intent(Intent.ACTION_VIEW);
                //            intent.setDataAndType(uri, "*/*");
                //            context.startActivity(intent);
                if (check_Android30FileBrowser_AndroidLysesoftFileBrowser(uri, context, pm, true)) {
                    return
                }
            }
        }
        //api 24 not open folder? not have filebrowser((
        @JvmStatic private fun checkResolver(intent: Intent, pm: PackageManager) {
            var resolvedActivityList = pm.queryIntentActivities(intent, 0)
            for (info in resolvedActivityList) {
                DLog.d("------------[][][" + info.toString())
                var serviceIntent: Intent = intent
                //new Intent();
                //serviceIntent.setAction(Intent.ACTION_VIEW);
                //serviceIntent.setPackage(info.activityInfo.packageName);
                // Check if this package also resolves the Custom Tabs service.
                if (pm.resolveService(serviceIntent, 0) != null) {
                    //packagesSupportingCustomTabs.add(info);
                    DLog.d("-----0------" + info.toString())
                } else if (pm.resolveActivity(serviceIntent, 0) != null) {
                    DLog.d("-----1------" + info.toString())
                }
            }
        }
        @JvmStatic private fun resolwe(intent: Intent, pm: PackageManager) {
            var resolvedActivityList = pm.queryIntentActivities(intent, 0)
            for (info in resolvedActivityList) {
                DLog.d("------------[][][" + info.toString())
                var serviceIntent: Intent = Intent()
                serviceIntent.setAction(Intent.ACTION_VIEW)
                //serviceIntent.setPackage(info.activityInfo.packageName);
                // Check if this package also resolves the Custom Tabs service.
                if (pm.resolveService(serviceIntent, 0) != null) {
                    //packagesSupportingCustomTabs.add(info);
                    DLog.d("-----0------" + info.toString())
                } else if (pm.resolveActivity(serviceIntent, 0) != null) {
                    DLog.d("-----1------" + info.toString())
                }
            }
        }
        @JvmStatic fun check_Android30FileBrowser_AndroidLysesoftFileBrowser(uri: Uri, context: Context, pm: PackageManager, launch: Boolean): Boolean {
            var m: Array<String> = arrayOf("vnd.android.document/directory", "vnd.android.cursor.dir/lysesoft.andexplorer.director")
            //=WORK in Android 30=
            //"vnd.android.cursor.dir/*"
            for (type in m) {
                var intent: Intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, type)
                var mm: ActivityInfo = intent.resolveActivityInfo(pm, 0)
                if (mm != null) {
                    DLog.d("[]" + uri + " " + mm + " " + type)
                    if (launch) {
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            DLog.handleException(e)
                        }
                    }
                    return true
                }
            }
            return false
        }
        /**
             * It's error
             * <p>
             * intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
             * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             * <p>
             * Success
             * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
             * or Intent.FLAG_GRANT_READ_URI_PERMISSION);
             */
        @JvmStatic fun packageName(info: ResolveInfo): String? {
            var zzz: String? = null
            if (info.activityInfo != null) {
                zzz = info.activityInfo.packageName
            }
            if (info.serviceInfo != null) {
                zzz = info.serviceInfo.packageName
            }
            return zzz
        }
        @JvmStatic fun componentName(info: ResolveInfo): String? {
            var zzz: String? = null
            if (info.activityInfo != null) {
                zzz = info.activityInfo.name
            }
            if (info.serviceInfo != null) {
                zzz = info.serviceInfo.name
            }
            return zzz
        }
    }
}
