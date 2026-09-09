package com.walhalla.ttvloader.activity.tools

import android.content.Context.DOWNLOAD_SERVICE
import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.walhalla.ttvloader.databinding.AaaBinding
import com.walhalla.ui.DLog
import java.util.Arrays

class Tools2Fragment : Fragment() {
    val PSU_URL: String = "https://tou.edu.kz/images/links/gos_vestnik.png"
    // Картинка для загрузки
    val RCODE_LOADING: Int = 1
    // Константа для идентификации запроса разрешений
    var currentURL: String? = null
    // Переменная с URL-адресом загружаемой картинки
    private var binding: AaaBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AaaBinding.inflate(inflater, container!!, false)
        binding!!.button.setOnClickListener({ v -> 
                currentURL = PSU_URL
                saveImage(getContext()!!, currentURL!!)
                })
        return binding!!.getRoot()
    }
    //-------------------------------------------------------
    // Проверка наличия конкретного разрешения у программы
    // Проверка наличия списка разрешений у программы
    // Нужные разрешения для старых версий Android
    // Нужные разрешения для Android 33
    //Manifest.permission.READ_MEDIA_AUDIO,
    // Определяем перечень нужных разрешений для текущей версии Android
    //-------------------------------------------------------
    // Вызывается сразу после установки разрешения
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        DLog.d("" + Arrays.toString(permissions))
        DLog.d("" + Arrays.toString(grantResults))
        //        PackageManager.PERMISSION_DENIED
        //        android.permission.READ_MEDIA_IMAGES, android.permission.READ_MEDIA_AUDIO, android.permission.READ_MEDIA_VIDEO
        // Если вызывается наш запрос на разрешения
        if (requestCode == RCODE_LOADING) {
            // Если разрешения даны, то сохранить картинку
            if (hasPermissionsList(getContext()!!, permissions())) {
                saveImage(getContext()!!, currentURL!!)
            }
        }
    }
    // Сохранение картинки с заданного адреса URL
    private fun saveImage(context: Context, url: String) {
        try {
            // Проверяем, есть ли нужные разрешения, если нет то выдаем окно запроса с выходом из этого метода
            if (hasPermissionsList(context, permissions())) {
                var perms: Array<String> = permissions()
                DLog.d("@aaaaa@" + Arrays.toString(perms))
                requestPermissions(perms, RCODE_LOADING)
                DLog.d("@bbbbb@" + Arrays.toString(perms))
                for (perm in perms) {
                    if (shouldShowRequestPermissionRationale(perm)) {
                        DLog.d("@" + perm)
                    }
                }
                //                        showRequestPermissionDialog(getActivity()!!, perm, (dialog, which) -> {
                //                            //.... requestPermissionLauncher.launch(perm)
                //                        });
                return
            }
            // Если адрес нормальный, то загружаем картинку
            if (URLUtil.isValidUrl(url)) {
                var request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, Uri.parse(url).getLastPathSegment())
                var downloadManager: DownloadManager = (context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager)
                downloadManager.enqueue(request)
                Toast.makeText(context, "R.string.loaded", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "R.string.error", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "R.string.error", Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        @JvmStatic fun hasPermission(context: Context, permission: String): Boolean {
            var res: Int = context.checkCallingOrSelfPermission(permission)
            return res == PackageManager.PERMISSION_GRANTED
        }
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
