package com.walhalla.ttvloader.ui.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.walhalla.permissionresolver.permission.IOUtils;

import java.util.Map;


public class MainActivityPresenter extends BasePresenter {

    private static final String TAG = "@@@";

    private final String[] REQUEST_PERMISSION = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private final ActivityResultLauncher<String[]> launcher29;

    public MainActivityPresenter(AppCompatActivity activity, ActivityResultLauncher<Intent> storageActivityResultLauncher) {
        super(activity, storageActivityResultLauncher);

        launcher29 = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), map -> {
            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                boolean isGranted = entry.getValue();
                if (isGranted) {
                    Toast.makeText(activity, "GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "NOT GRANTED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isNeedGrantPermission() {
        try {
            if (FULL_STORAGE_ACCESS) {

                if (openAllFilesAccessPermission30()) {
                    openManageAllFiles();
                    return true;
                } else {
                    return false;
                }

            } else if (IOUtils.hasMarsallow23()) {//23-29
                int result = ContextCompat.checkSelfPermission(activity, REQUEST_PERMISSION[0]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    String perm = REQUEST_PERMISSION[0];
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
                        showRequestPermissionDialog(activity, perm, (dialog, which) ->
                        {
                            launcher29.launch(REQUEST_PERMISSION);
                        });
                    } else {
                        launcher29.launch(REQUEST_PERMISSION);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "isNeedGrantPermission: " + e.getLocalizedMessage());
        }
        return false;
    }
}
