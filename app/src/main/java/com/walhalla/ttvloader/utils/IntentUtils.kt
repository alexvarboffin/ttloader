package com.walhalla.ttvloader.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ui.DLog
import java.io.File

class IntentUtils {
    companion object {
        //Warning CRASH if packageName == null
        @JvmStatic fun openSettingsForPackageName2(context: Context, packageName: String) {
            try {
                var intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                var uri: Uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                context.startActivity(intent)
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
        //Requred application installed!, not crash if packageName == null
        @JvmStatic fun openSettingsForPackageName(context: Context, packageName: String) {
            try {
                var intent: Intent = Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).addCategory(Intent.CATEGORY_DEFAULT).setData(Uri.parse("package:" + packageName)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                context.startActivity(intent)
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
        @JvmStatic fun openFolder(context: Context, s: String) {
            val file: File = File(s)
            val parent: String = File(s).getParent()
            //DLog.d("" + parent);
            //Uri uri = Uri.parse(s);
            var uri: Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", File(parent))
            //                    Intent intent = null;
            //                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            //                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            //                        intent.addCategory(Intent.CATEGORY_OPENABLE);
            //                        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
            //                    }
            var intent: Intent? = null
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent!!.addCategory(Intent.CATEGORY_OPENABLE)
                intent!!.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent!!.setType(DocumentsContract.Document.MIME_TYPE_DIR)
                var tmp: ActivityInfo = intent!!.resolveActivityInfo(context.getPackageManager(), 0)
                if (tmp != null) {
                    intent!!.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    //startActivityForResult(intent, OPEN_REQUEST);
                    context.startActivity(intent!!)
                } else {
                }
            } else {
                //DLog.d();
                intent = Intent(Intent.ACTION_GET_CONTENT)
                intent!!.setDataAndType(uri, "text/csv")
                intent!!.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(Intent.createChooser(intent!!, "Open folder"))
            }
        }
    }
}
