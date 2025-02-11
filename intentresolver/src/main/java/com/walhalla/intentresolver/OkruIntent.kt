package com.walhalla.intentresolver

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.walhalla.abcsharedlib.Share
import com.walhalla.abcsharedlib.SharedNetwork
import com.walhalla.intentresolver.utils.UriUtils.getUriFromFile
import com.walhalla.ui.DLog
import java.io.File

class OkruIntent : BaseIntent(SharedNetwork.Package.RUOKANDROID) {
    override fun shareMp4Selector(context: Context, file: File) {
        //resolveForPackageName(context, SharedNetwork.Package.RUOKANDROID);

        //load to my


        resolveMp4ActivitiesForPackage(context, file, packageName)
    }

    override fun videoShare(context: Context, path: String) {
    }

    private fun resolveForPackageName(context: Context, appPackageName: String) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setPackage(appPackageName)
        intent.setType("*/*")
        val resInfoList = context.packageManager.queryIntentActivities(intent, 0)
        if (!resInfoList.isEmpty()) {
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                val activityName = resolveInfo.activityInfo.name
                DLog.d("[$packageName] [$activityName]")
                //context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            DLog.d("Not found activity...")
        }
    }


    private fun resolveMp4ActivitiesForPackage0(context: Context, file: File, packageName: String) {
        if (file.exists()) {
            try {
                val title =
                    context.resources.getString(R.string.sharing_video_title) + ": " + file.absolutePath


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
                    val uri = getUriFromFile(context, file)
                    val sendIntent =
                        makeSendOkIntent(context.getString(R.string.SharingVideoBody), "*/*")
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    val componentName = ComponentName(
                        packageName,
                        "ru.ok.android.ui.activity.ExternalShareActivity"
                    )
                    sendIntent.setComponent(componentName)

                    //@@@
                    //sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoBody));
                    sendIntent.addFlags( /*Intent.FLAG_ACTIVITY_NEW_TASK |*/Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    //@@@@     sendIntent.setPackage(packageName);
                    if (sendIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(sendIntent)
                    } else {
                        DLog.d("No Activity found to handle this intent.")
                    }
                } catch (r: IllegalArgumentException) {
                    DLog.handleException(r)
                    //return Uri.parse(localVideo.path);
                    //return Uri.fromFile(file);
                }
            } catch (e: ActivityNotFoundException) {
                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
                //@@@    makeToaster(R.string.abc_err_something_wrong_sharing);
                DLog.handleException(e)
            }
        }
    }

    protected fun resolveMp4ActivitiesForPackage(
        context: Context,
        file: File,
        packageName: String?
    ) {
        if (file.exists()) {
            try {
                val title =
                    context.resources.getString(R.string.sharing_video_title) + ": " + file.absolutePath


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
                    val uri = getUriFromFile(context, file)
                    val shareIntent =
                        IntentUtils.makeVideoShare(context.getString(R.string.SharingVideoBody))
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

                    //@@@
                    //shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoBody));
                    shareIntent.addFlags( /*Intent.FLAG_ACTIVITY_NEW_TASK |*/Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    if (packageName == null) {
                        val chozzer = Intent.createChooser(shareIntent, title)
                        context.startActivity(chozzer)
                    } else {
                        shareIntent.setPackage(packageName)
                        context.startActivity(shareIntent)
                    }
                } catch (r: IllegalArgumentException) {
                    DLog.handleException(r)
                    //return Uri.parse(localVideo.path);
                    //return Uri.fromFile(file);
                }
            } catch (e: ActivityNotFoundException) {
                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
                //@@@    makeToaster(R.string.abc_err_something_wrong_sharing);
                DLog.handleException(e)
            }
        }
    }

    companion object {
        fun makeSendOkIntent(content: String?, type: String?): Intent {
            //Intent intent = new Intent(Intent.ACTION_SEND);

            val intent = Intent()
            intent.setAction(Intent.ACTION_SEND)

            //@@@ intent.setType("*/*");
            //@@@ intent.setType(MIME_TYPE_JPEG);
            //@@@ intent.setType("text/plain");
            //intent.setType("image/*");//<================
            //==>intent.setType("*/*");//<================
            intent.setType(type)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.putExtra(Share.comPinterestEXTRA_DESCRIPTION, content)
            if (BuildConfig.DEBUG) {
                //intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Share.email))
            }
            return intent
        }
    }
}
