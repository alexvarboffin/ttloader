package com.walhalla.intentresolver;

import static com.walhalla.abcsharedlib.Share.email;
import static com.walhalla.intentresolver.IntentUtils.makeVideoShare;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.walhalla.abcsharedlib.Share;
import com.walhalla.abcsharedlib.SharedNetwork;

import com.walhalla.intentresolver.utils.UriUtils;
import com.walhalla.ui.DLog;

import java.io.File;
import java.util.List;

public class OkruIntent extends BaseIntent {

    public OkruIntent() {
        super(SharedNetwork.Package.RUOKANDROID);
    }

    @Override
    public void shareMp4Selector(Context context, File file) {


        //resolveForPackageName(context, SharedNetwork.Package.RUOKANDROID);

        //load to my
        resolveMp4ActivitiesForPackage(context, file, packageName);
    }

    @Override
    public void videoShare(Context context, String path) {

    }

    private void resolveForPackageName(Context context, String appPackageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(appPackageName);
        intent.setType("*/*");
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfoList.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                String activityName = resolveInfo.activityInfo.name;
                DLog.d("[" + packageName + "] [" + activityName + "]");
                //context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            DLog.d("Not found activity...");
        }
    }


    private void resolveMp4ActivitiesForPackage0(Context context, File file, String packageName) {
        if (file.exists()) {
            try {
                String title = context.getResources().getString(R.string.sharing_video_title) + ": " + file.getAbsolutePath();
//                intent.setType("video/mp4");
//
//                //Uri uri = getUriFromFile(file);
//                //intent.setDataAndType(uri, "video/mp4");
//
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(localVideo.path));
//                intent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoSubject));
//                intent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.SharingVideoBody) + "\n");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                this.startActivityForResult(Intent.createChooser(intent, title), REQUEST_SHARE);


                try {
                    //Tiktok doc
                    Uri uri = UriUtils.getUriFromFile(context, file);
                    Intent sendIntent = makeSendOkIntent(context.getString(R.string.SharingVideoBody), "*/*");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    ComponentName componentName = new ComponentName(packageName, "ru.ok.android.ui.activity.ExternalShareActivity");
                    sendIntent.setComponent(componentName);
                    //@@@
                    //sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoBody));

                    sendIntent.addFlags(/*Intent.FLAG_ACTIVITY_NEW_TASK |*/ Intent.FLAG_GRANT_READ_URI_PERMISSION);
               //@@@@     sendIntent.setPackage(packageName);
                    if (sendIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(sendIntent);
                    } else {
                        DLog.d("No Activity found to handle this intent.");
                    }

                } catch (IllegalArgumentException r) {
                    DLog.handleException(r);
                    //return Uri.parse(localVideo.path);
                    //return Uri.fromFile(file);
                }
            } catch (ActivityNotFoundException e) {
                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
                //@@@    makeToaster(R.string.abc_err_something_wrong_sharing);
                DLog.handleException(e);
            }
        }
    }

    public static Intent makeSendOkIntent(String content, String type) {

        //Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        //@@@ intent.setType("*/*");
        //@@@ intent.setType(MIME_TYPE_JPEG);
        //@@@ intent.setType("text/plain");
        //intent.setType("image/*");//<================
        //==>intent.setType("*/*");//<================

        intent.setType(type);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Share.comPinterestEXTRA_DESCRIPTION, content);
        if (BuildConfig.DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Share.email});
        }
        return intent;
    }

    protected void resolveMp4ActivitiesForPackage(Context context, File file, String packageName) {
        if (file.exists()) {
            try {
                String title = context.getResources().getString(R.string.sharing_video_title) + ": " + file.getAbsolutePath();
//                intent.setType("video/mp4");
//
//                //Uri uri = getUriFromFile(file);
//                //intent.setDataAndType(uri, "video/mp4");
//
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(localVideo.path));
//                intent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoSubject));
//                intent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.SharingVideoBody) + "\n");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                this.startActivityForResult(Intent.createChooser(intent, title), REQUEST_SHARE);


                try {
                    //Tiktok doc
                    Uri uri = UriUtils.getUriFromFile(context, file);
                    Intent shareIntent = makeVideoShare(context.getString(R.string.SharingVideoBody));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    //@@@
                    //shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoBody));

                    shareIntent.addFlags(/*Intent.FLAG_ACTIVITY_NEW_TASK |*/ Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (packageName == null) {
                        Intent chozzer = Intent.createChooser(shareIntent, title);
                        context.startActivity(chozzer);
                    } else {
                        shareIntent.setPackage(packageName);
                        context.startActivity(shareIntent);
                    }
                } catch (IllegalArgumentException r) {
                    DLog.handleException(r);
                    //return Uri.parse(localVideo.path);
                    //return Uri.fromFile(file);
                }
            } catch (ActivityNotFoundException e) {
                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
                //@@@    makeToaster(R.string.abc_err_something_wrong_sharing);
                DLog.handleException(e);
            }
        }
    }
}
