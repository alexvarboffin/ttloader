package com.walhalla.intentresolver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;


import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;

import java.util.List;

public abstract class BaseIntent implements UIntent {

    String packageName;

    public BaseIntent(String packageName) {
        this.packageName = packageName;
    }

    public boolean isClientPackage(String packageName) {
        return this.packageName != null && this.packageName.equals(packageName);
    }


    protected List<ResolveInfo> resolveForUri(Context context, Intent intent, Uri contentUri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (!resInfoList.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                String activityName = resolveInfo.activityInfo.name;
                if (BuildConfig.DEBUG) {
                    DLog.d("[@@@@@" + packageName + "]" + activityName + "@@]");
                }
                context.grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }
        } else {
            DLog.d("Not found activity...");
        }
        return resInfoList;
    }
}
