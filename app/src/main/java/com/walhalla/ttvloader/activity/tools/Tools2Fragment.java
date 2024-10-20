package com.walhalla.ttvloader.activity.tools;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.Fragment;

import com.walhalla.ttvloader.databinding.AaaBinding;
import com.walhalla.ui.DLog;

import java.util.Arrays;

public class Tools2Fragment extends Fragment {


    final String PSU_URL = "https://tou.edu.kz/images/links/gos_vestnik.png"; // Картинка для загрузки
    final int RCODE_LOADING = 1; // Константа для идентификации запроса разрешений
    String currentURL; // Переменная с URL-адресом загружаемой картинки
    private AaaBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AaaBinding.inflate(inflater, container, false);
        binding.button.setOnClickListener(v -> {
            currentURL = PSU_URL;
            saveImage(getContext(), currentURL);
        });
        return binding.getRoot();
    }


    //-------------------------------------------------------

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

    // Нужные разрешения для старых версий Android
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // Нужные разрешения для Android 33
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            //Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

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

    //-------------------------------------------------------

    // Вызывается сразу после установки разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DLog.d("@@@@@@"+ Arrays.toString(permissions));
        DLog.d("@@@@@@"+ Arrays.toString(grantResults));

//        PackageManager.PERMISSION_DENIED
//        android.permission.READ_MEDIA_IMAGES, android.permission.READ_MEDIA_AUDIO, android.permission.READ_MEDIA_VIDEO

        // Если вызывается наш запрос на разрешения
        if (requestCode == RCODE_LOADING) {

            // Если разрешения даны, то сохранить картинку
            if (hasPermissionsList(getContext(), permissions())) {
                saveImage(getContext(), currentURL);
            }
        }
    }

    // Сохранение картинки с заданного адреса URL
    private void saveImage(Context context, String url) {
        try {
            // Проверяем, есть ли нужные разрешения, если нет то выдаем окно запроса с выходом из этого метода
            if (!hasPermissionsList(context, permissions())) {
                String[] perms = permissions();
                DLog.d("@@@aaaaa@@@@@" + Arrays.toString(perms));
                requestPermissions(perms, RCODE_LOADING);
                DLog.d("@@@bbbbb@@@@@" + Arrays.toString(perms));

                for (String perm : perms) {

                    if (shouldShowRequestPermissionRationale(perm)) {
                        DLog.d("@@@@@@@@@@@@@" + perm);
//                        showRequestPermissionDialog(getActivity(), perm, (dialog, which) -> {
//                            //.... requestPermissionLauncher.launch(perm)
//                        });
                    }
                }
                return;
            }
            // Если адрес нормальный, то загружаем картинку
            if (URLUtil.isValidUrl(url)) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES, Uri.parse(url).getLastPathSegment());
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(context, "R.string.loaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "R.string.error", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "R.string.error", Toast.LENGTH_LONG).show();
        }
    }
}
