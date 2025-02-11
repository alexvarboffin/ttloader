package com.walhalla.intentresolver

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import com.walhalla.ui.BuildConfig
import com.walhalla.ui.DLog

abstract class BaseIntent(@JvmField var packageName: String?) : UIntent {

    override fun isClientPackage(packageName: String?): Boolean {
        return this.packageName != null && this.packageName == packageName
    }


    protected fun resolveForUri(
        context: Context,
        intent: Intent,
        contentUri: Uri
    ): List<ResolveInfo> {
        val resInfoList =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resInfoList.isNotEmpty()) {
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                val activityName = resolveInfo.activityInfo.name
                if (BuildConfig.DEBUG) {
                    DLog.d("[@@@@@$packageName]$activityName@@]")
                }
                context.grantUriPermission(
                    packageName, contentUri,
                    (Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            or Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                )
            }
        } else {
            DLog.d("Not found activity...")
        }
        return resInfoList
    }
}
