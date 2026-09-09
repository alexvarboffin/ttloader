package com.walhalla.ttvloader.ui.gallery

import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.walhalla.permissionresolver.permission.IOUtils
import com.walhalla.ttvloader.ui.base.BasePresenter

class DownloadManagerPresenter : BasePresenter {
    private lateinit var mView: View
    // Нужные разрешения для старых версий Android
    // Нужные разрешения для Android 33
    //Manifest.permission.READ_MEDIA_AUDIO,
    interface View {
        abstract fun updateGUI()
        abstract fun showPermission33SnackBar()
    }
    private var oldValueGranted: Boolean? = null
    private lateinit var handler: Handler
    var launcher29: ActivityResultLauncher<Array<String>>? = null
    constructor(view: View, handler: Handler, activity: AppCompatActivity, requestPermissionLauncher: ActivityResultLauncher<Array<String>>, storageActivityResultLauncher: ActivityResultLauncher<Intent>) : super(activity, storageActivityResultLauncher) {
        this.handler = handler
        this.mView = view
        this.launcher29 = requestPermissionLauncher
    }
    //    private void checkAndRequestPermissions(Context context) {
    //
    ////        if (ma != null && !ma.isNeedGrantPermission0()) {
    ////            updateGUI(getActivity()!!.getContentResolver(), getActivity()!!, true);
    ////        }
    //
    //        oldValueGranted = perm.isGrantedPermissionForGallery(getContext()!!);
    //        if (oldValueGranted) {
    //            // Permission is granted. Update the GUI
    //            updateGUI(getActivity()!!.getContentResolver(), getActivity()!!, true);
    //
    //        } else {
    //
    ////            if (ActivityCompat.shouldShowRequestPermissionRationale(context, REQUEST_PERMISSION[0])) {
    ////                String msg =
    ////                        String.format(context.getString(com.walhalla.permissionresolver.@),
    ////                                context.getString(com.walhalla.permissionresolver.R.string.app_name));
    ////
    ////                AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
    ////                localBuilder.setTitle(com.walhalla.permissionresolver.R.string.alert_perm_title);
    ////                localBuilder.setMessage(msg)
    ////                        .setNeutralButton("Grant", (dialog, which) -> ActivityCompat.requestPermissions(
    ////                                context, REQUEST_PERMISSION, REQUEST_PERMISSION_CODE));
    ////                localBuilder.setNegativeButton(
    ////                        android.R.string.cancel, (dialog, which) -> {
    ////                            dialog.dismiss();
    ////                            //finish();
    ////                        });
    ////                localBuilder.show();
    ////
    ////            } else {
    ////                ActivityCompat.requestPermissions(context, REQUEST_PERMISSION, REQUEST_PERMISSION_CODE);
    ////            }
    //            // Permission is not granted. Request it
    //            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    //        }
    //    }
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
                //int result = ContextCompat.checkSelfPermission(activity, REQUEST_PERMISSION[0]);
                if (hasPermissionsList(activity, permissions())) {
                    var perms: Array<String> = permissions()
                    if (shouldShowRequestPermissionRationale00(perms)) {
                        showRequestPermissionDialog(activity, perms, { dialog, which -> 
                                                launcher29!!.launch(perms)
                                                })
                    } else {
                        launcher29!!.launch(perms)
                    }
                    return true
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "isNeedGrantPermission: " + e.localizedMessage)
        }
        return false
    }
    // Определяем перечень нужных разрешений для текущей версии Android
    @RequiresApi(api = Build.VERSION_CODES.R) private fun checkSelfPermissionOrLaunch33() {
        if (openAllFilesAccessPermission30()) {
            //openManageAllFiles();
            mView.showPermission33SnackBar()
        } else {
            //                else if (USE_PARTIAL_ACCESS) {
            //
            //                    boolean m13 = false;
            //
            ////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ////                        boolean v0 = ContextCompat.checkSelfPermission(context, "android.permission.READ_MEDIA_IMAGES") == PERMISSION_GRANTED;
            ////                        boolean v1 = ContextCompat.checkSelfPermission(context, READ_MEDIA_VIDEO) == PERMISSION_GRANTED;
            ////
            ////                        m13 = v0 || v1;
            ////
            ////                        DLog.d("Full access on Android 13+? " + v0+""+v1+""+m13);
            ////
            ////                        checkSelfPermissionOrLaunch(context, "android.permission.READ_MEDIA_IMAGES");
            ////                        //checkSelfPermissionOrLaunch(context, READ_MEDIA_VIDEO);
            ////
            ////                    }
            //
            ////
            //                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            //                        boolean v0 = ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED;
            //                        DLog.d("// Partial access on Android 14+"+v0);
            //                    }
            ////                    else if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            ////                        DLog.d("// Full access up to Android 12");
            ////                    } else {
            ////                        DLog.d("// Access denied");
            ////                    }
            //                }
            mView.updateGUI()
        }
    }
    companion object {
        private const val TAG = "@"
        private const val USE_PARTIAL_ACCESS = true
        @JvmField var storge_permissions: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @JvmField var storge_permissions_33: Array<String> = arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        @JvmStatic fun permissions(): Array<String> {
            var p: Array<String>? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                p = storge_permissions_33
            } else {
                p = storge_permissions
            }
            return p!!
        }
    }
}
