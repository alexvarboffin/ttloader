package com.walhalla.intentresolver;

import static com.walhalla.abcsharedlib.Share.comPinterestEXTRA_DESCRIPTION;
import static com.walhalla.abcsharedlib.Share.email;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.walhalla.abcsharedlib.Share;
import com.walhalla.intentresolver.utils.UriUtils;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;

import java.io.File;

public class IntentUtils {

    protected static void resolveMp4ActivitiesForPackage(Context context, File file, String packageName) {
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

    public static Intent makeVideoShare(String content) {
        return IntentUtils.makeVideoShare(content, "video/*");
    }

    public static Intent makeVideoShare(String content, String type) {

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
}
