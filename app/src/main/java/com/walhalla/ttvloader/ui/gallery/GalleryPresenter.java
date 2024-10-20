package com.walhalla.ttvloader.ui.gallery;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.permissionresolver.permission.IOUtils;
import com.walhalla.ttvloader.ui.base.BasePresenter;

public class GalleryPresenter extends BasePresenter {


    private static final boolean USE_PARTIAL_ACCESS = true;
    // Нужные разрешения для старых версий Android
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final View mView;


    // Нужные разрешения для Android 33
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            //Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public interface View {
        void updateGUI();

        void showPermission33SnackBar();
    }

    private Boolean oldValueGranted;

    private final Handler handler;



    ActivityResultLauncher<String[]> launcher29;

    public GalleryPresenter(GalleryPresenter.View view, Handler handler, AppCompatActivity activity,
                            ActivityResultLauncher<String[]> requestPermissionLauncher,
                            ActivityResultLauncher<Intent> storageActivityResultLauncher) {
        super(activity, storageActivityResultLauncher);
        this.handler = handler;
        this.mView = view;

        this.launcher29 = requestPermissionLauncher;

    }


//    private void checkAndRequestPermissions(Context context) {
//
////        if (ma != null && !ma.isNeedGrantPermission0()) {
////            updateGUI(getActivity().getContentResolver(), getActivity(), true);
////        }
//
//        oldValueGranted = perm.isGrantedPermissionForGallery(getContext());
//        if (oldValueGranted) {
//            // Permission is granted. Update the GUI
//            updateGUI(getActivity().getContentResolver(), getActivity(), true);
//
//        } else {
//
////            if (ActivityCompat.shouldShowRequestPermissionRationale(context, REQUEST_PERMISSION[0])) {
////                String msg =
////                        String.format(context.getString(com.walhalla.permissionresolver.@@@@@@@@@@@@@),
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

    public void onResume() {
        isNeedGrantPermission();

//        if (oldValueGranted == null) {
//            DLog.d("-------------------------------first launch");
//            oldValueGranted = perm.isGrantedPermissionForGallery(getContext());
//            if (oldValueGranted) {
//                // Permission is granted. Update the GUI
//                updateGUI(getActivity().getContentResolver(), getActivity(), true);
//            }
//        } else {
//            //DLog.d("-------------we camback------------------" + oldValueGranted);
//            if (!oldValueGranted) {
//                boolean newValue = perm.isGrantedPermissionForGallery(getContext());
//                if (newValue) {
//                    checkAndRequestPermissions(getContext());
//                }
//            }
//        }
    }


    public void isNeedGrantPermission() {
        try {
            if (FULL_STORAGE_ACCESS) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    checkSelfPermissionOrLaunch33();
                }
            } else if (IOUtils.hasMarsallow23()) {//23-29
                checkSelfPermissionOrLaunch0(activity, permissions());
            } else {
                mView.updateGUI();
            }
        } catch (Exception e) {
            Log.d("@@@", "isNeedGrantPermission: " + e.getLocalizedMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void checkSelfPermissionOrLaunch33() {
        if (openAllFilesAccessPermission30()) {
            //openManageAllFiles();
            mView.showPermission33SnackBar();
        }
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
////                        DLog.d("Full access on Android 13+? " + v0+"@@"+v1+"@@"+m13);
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
        else {
            mView.updateGUI();
        }
    }
    // Определяем перечень нужных разрешений для текущей версии Android
    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    private void checkSelfPermissionOrLaunch0(Activity context, String[] strings) {
        //int result = ContextCompat.checkSelfPermission(activity, REQUEST_PERMISSION[0]);
        if (!hasPermissionsList(activity, strings)) {
            String[] perms = permissions();
            if (shouldShowRequestPermissionRationale00(perms)) {
                showRequestPermissionDialog(activity, perms, (dialog, which) -> {
                    launcher29.launch(perms);
                });
            } else {
                launcher29.launch(perms);
            }
        } else {
            mView.updateGUI();
        }

    }
}
