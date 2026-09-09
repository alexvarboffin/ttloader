package com.walhalla.ttvloader.ui.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.walhalla.permissionresolver.permission.IOUtils
import java.util.Map

class AutoPresenter : BasePresenter {
    private val REQUEST_PERMISSION: Array<String> = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var launcher29: ActivityResultLauncher<Array<String>>
    constructor(activity: AppCompatActivity, var0: ActivityResultLauncher<Intent>) : super(activity, var0) {
        launcher29 = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), { map -> 
                for (entry in map.entries) {
                    var isGranted: Boolean = entry.value
                    if (isGranted) {
                        Toast.makeText(activity, "GRANTED", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "NOT GRANTED", Toast.LENGTH_SHORT).show()
                    }
                }
                })
    }
    fun isNeedGrantPermission(): Boolean {
        try {
            if (FULL_STORAGE_ACCESS) {
                if (openAllFilesAccessPermission30()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        openManageAllFiles()
                    }
                    return true
                } else {
                    return false
                }
            } else if (IOUtils.hasMarsallow23()) {
                //23-29
                var result: Int = ContextCompat.checkSelfPermission(activity, REQUEST_PERMISSION[0])
                if (result != PackageManager.PERMISSION_GRANTED) {
                    var perm: String = REQUEST_PERMISSION[0]
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
                        showRequestPermissionDialog(activity, perm, { dialog, which -> 
                                                launcher29.launch(REQUEST_PERMISSION)
                                                })
                    } else {
                        launcher29.launch(REQUEST_PERMISSION)
                    }
                    return true
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "isNeedGrantPermission: " + e.localizedMessage)
        }
        return false
    }
    companion object {
        private const val TAG = "@"
    }
}

//    public static void showRequestPermissionDialog(Activity context, DialogInterface.OnClickListener clickListener) {
//        String msg = String.format(context.getString(R.string.format_request_permision), context.getString(R.string.app_name));
//        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
//        localBuilder.setTitle(R.string.alert_perm_title);
//        localBuilder
//                .setMessage(msg)
//                .setNeutralButton("Grant", clickListener);
//        localBuilder.setNegativeButton(
//                android.R.string.cancel, (dialog, which) -> {
//                    dialog.dismiss();
//                    //finish();
//                });
//        localBuilder.show();
//    }
//    public boolean isNeedGrantPermission() {
//        if (FULL_STORAGE_ACCESS) {//>30
//
//        } else /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)*/ {//>=23-29
//            String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//            boolean var0 = ContextCompat.checkSelfPermission(getActivity()!!, perm) == PackageManager.PERMISSION_GRANTED;
//            if (var0) {
//                onSave29(holder0);
//            } else {
//                //show permission popup
//                PermissionResolver.saveThumb0(getActivity()!!, requestPermissionLauncher);
//            }
//        }
//
//        return false;
//    }
