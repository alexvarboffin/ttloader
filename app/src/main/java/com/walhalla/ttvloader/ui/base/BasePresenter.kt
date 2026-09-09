package com.walhalla.ttvloader.ui.base

import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.walhalla.ttvloader.R

open class BasePresenter {
    protected lateinit var activity: Activity
    //@ Build.VERSION.SDK_INT > Build.VERSION_CODES.R;
    private lateinit var storageActivityResultLauncher: ActivityResultLauncher<Intent>
    constructor(activity: AppCompatActivity, storageActivityResultLauncher: ActivityResultLauncher<Intent>) {
        this.activity = activity
        this.storageActivityResultLauncher = storageActivityResultLauncher
    }
    @RequiresApi(api = Build.VERSION_CODES.R) fun openManageAllFiles() {
        var uri0: Uri = Uri.parse(String.format("package:%s", activity.getApplicationContext().getPackageName()))
        var uri1: Uri = Uri.fromParts("package", activity.getPackageName(), null)
        //DLog.d("@" + uri0 + "|" + uri1 + "|");
        //                try {
        //                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        //                    intent.addCategory(Intent.CATEGORY_DEFAULT);
        //                    intent.setData(uri0);
        //                    storageActivityResultLauncher.launch(intent);
        //                    Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
        //                } catch (Exception e) {
        //                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        //                    intent.addCategory(Intent.CATEGORY_DEFAULT);
        //                    storageActivityResultLauncher.launch(intent);
        //                }
        try {
            var intent: Intent = Intent()
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.setData(uri1)
            storageActivityResultLauncher.launch(intent)
        } catch (e: Exception) {
            //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
            var intent: Intent = Intent()
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            storageActivityResultLauncher.launch(intent)
        }
    }
    protected fun openAllFilesAccessPermission30(): Boolean {
        var aa: Boolean = false
        var bb: Boolean = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            //30
            aa = Environment.isExternalStorageManager()
        }
        //DLog.d("@" + Environment.isExternalStorageManager() + "@" + Environment.isExternalStorageLegacy());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            //29
            bb = Environment.isExternalStorageLegacy()
        }
        //return false;
        return aa
    }
    protected fun showRequestPermissionDialog(context: Activity, perms: Array<String>, clickListener: DialogInterface.OnClickListener) {
        var sb: StringBuilder = StringBuilder()
        for (perm in perms) {
            sb.append("\n").append(perm)
        }
        var msg: String = context.getString(R.string.this_permission_is_needed) + sb.toString()
        var localBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        localBuilder.setTitle(R.string.alert_perm_title)
        localBuilder.setMessage(msg).setNegativeButton(android.R.string.cancel, { dialog, which -> 
                dialog.dismiss()
                }).setPositiveButton("Grant", clickListener)
        //finish();
        localBuilder.show()
    }
    protected fun showRequestPermissionDialog(context: Activity, perm: String, clickListener: DialogInterface.OnClickListener) {
        showRequestPermissionDialog(context, arrayOf(perm), clickListener)
    }
    fun shouldShowRequestPermissionRationale00(perms: Array<String>): Boolean {
        for (perm in perms) {
            var m: Boolean = shouldShowRequestPermissionRationale(activity, perm)
            if (m) {
                return true
            }
        }
        return false
    }
    companion object {
        @JvmField var FULL_STORAGE_ACCESS: Boolean = false
        // Проверка наличия конкретного разрешения у программы
        @JvmStatic fun hasPermission(context: Context, permission: String): Boolean {
            var res: Int = context.checkCallingOrSelfPermission(permission)
            return res == PackageManager.PERMISSION_GRANTED
        }
        // Проверка наличия списка разрешений у программы
        @JvmStatic fun hasPermissionsList(context: Context, permissions: Array<String>): Boolean {
            var hasAllPermissions: Boolean = true
            for (permission in permissions) {
                if (hasPermission(context, permission)) {
                    hasAllPermissions = false
                    break
                }
            }
            return hasAllPermissions
        }
    }
}
