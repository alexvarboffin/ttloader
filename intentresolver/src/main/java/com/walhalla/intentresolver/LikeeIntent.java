package com.walhalla.intentresolver;


import android.content.Context;

import com.walhalla.abcsharedlib.SharedNetwork;

import java.io.File;

public class LikeeIntent extends BaseIntent {

    public LikeeIntent() {
        super(SharedNetwork.Package.LIKEE);
    }

    //sg.bigo.live.share.receivesharing.SharingActivity
//"video/mp4"mView.shareMp4Selector(file, );

//        PackageManager packageManager = context.getPackageManager();
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setPackage(PACKAGE_LIKEE);
//        shareIntent.setType("*/*");
//
//        List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, 0);
//        for (ResolveInfo info : activities) {
//            String packageName = info.activityInfo.packageName;
//            String activityName = info.activityInfo.name;
//            DLog.d("[" + packageName + "]" + activityName);
//        }
//        context.startActivity(shareIntent);


    @Override
    public void shareMp4Selector(Context context, File file) {
        IntentUtils.resolveMp4ActivitiesForPackage(context, file, packageName);
    }

    @Override
    public void videoShare(Context context, String path) {

    }
}
