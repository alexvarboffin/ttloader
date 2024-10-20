package com.walhalla.ttvloader.ui.base;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.ttvloader.R;

public class BasePresenter {

    protected final Activity activity;
    public static boolean FULL_STORAGE_ACCESS = false;//@@@ Build.VERSION.SDK_INT > Build.VERSION_CODES.R;

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher;

    public BasePresenter(AppCompatActivity activity, ActivityResultLauncher<Intent> storageActivityResultLauncher) {
        this.activity = activity;
        this.storageActivityResultLauncher = storageActivityResultLauncher;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void openManageAllFiles() {
        Uri uri0 = Uri.parse(String.format("package:%s", activity.getApplicationContext().getPackageName()));
        Uri uri1 = Uri.fromParts("package", activity.getPackageName(), null);
        //DLog.d("@@@" + uri0 + "|" + uri1 + "|");
//                try {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.setData(uri0);
//                    storageActivityResultLauncher.launch(intent);
//                    Toast.makeText(activity, "@@@@", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    storageActivityResultLauncher.launch(intent);
//                }
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(uri1);
            storageActivityResultLauncher.launch(intent);
            //Toast.makeText(activity, "@@@@", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            storageActivityResultLauncher.launch(intent);
        }
    }


    protected boolean openAllFilesAccessPermission30() {
        boolean aa = false;
        boolean bb = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {//30
            aa = !Environment.isExternalStorageManager();
            //DLog.d("@@@@@" + Environment.isExternalStorageManager() + "@@@" + Environment.isExternalStorageLegacy());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {//29
            bb = !Environment.isExternalStorageLegacy();
        }
        //return false;
        return aa;
    }

    protected void showRequestPermissionDialog(Activity context, String[] perms, DialogInterface.OnClickListener clickListener) {
        StringBuilder sb = new StringBuilder();
        for (String perm : perms) {
            sb.append("\n").append(perm);
        }
        String msg = context.getString(R.string.this_permission_is_needed) + sb.toString();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(R.string.alert_perm_title);
        localBuilder
                .setMessage(msg)
                .setNegativeButton(
                        android.R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                            //finish();
                        })
                .setPositiveButton("Grant", clickListener);
        localBuilder.show();
    }

    protected void showRequestPermissionDialog(Activity context, String perm, DialogInterface.OnClickListener clickListener) {
        showRequestPermissionDialog(context, new String[]{perm}, clickListener);
    }

    public boolean shouldShowRequestPermissionRationale00(String[] perms) {
        for (String perm : perms) {
            boolean m = shouldShowRequestPermissionRationale(activity, perm);
            if (m) {
                return true;
            }
        }
        return false;
    }

    // Проверка наличия конкретного разрешения у программы
    public static boolean hasPermission(Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    // Проверка наличия списка разрешений у программы
    public static boolean hasPermissionsList(Context context, String[] permissions) {
        boolean hasAllPermissions = true;
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false;
                break;
            }
        }
        return hasAllPermissions;
    }
}
