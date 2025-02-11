package com.walhalla.intentresolver

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.walhalla.abcsharedlib.SharedNetwork
import com.walhalla.intentresolver.utils.TextUtilz.dec0
import com.walhalla.intentresolver.utils.TextUtilz.extractTextBetween
import com.walhalla.intentresolver.utils.TextUtilz.readAllLines
import com.walhalla.intentresolver.utils.UriUtils.getUriFromFile
import com.walhalla.intentresolver.utils.YoutubeUtils.generateDescriptionFromTemplate
import com.walhalla.intentresolver.utils.YoutubeUtils.generateTitle
import java.io.File


class YoutubeIntent : BaseIntent(dec0(SharedNetwork.Package.tube)) {
    //    "android.intent.extra.TITLE"
    //            "android.intent.extra.SUBJECT"
    //            "android.intent.extra.TEXT"
    private val Shell_UploadActivity =
        "com.google.android.apps.youtube.app.application.Shell_UploadActivity"

    //private String Shell_MultipleUploadsActivity = "com.google.android.apps.youtube.app.application.Shell_MultipleUploadsActivity";
    override fun shareMp4Selector(context: Context, file: File) {
        //resolveMp4ActivitiesForPackage(context, file, Utils.dec0(tube));

        //List<String> tags = TextUtilz.readAllLines(context, "Chist_tags.txt");

        val tags = readAllLines(context, "tags.txt")

        var title = extractTextBetween(file.name).replace("__", "/")

        if (title.length > 100) {
            title = title.replace("video.", "video")
        }
        if (title.length > 100) {
            title = title.replace("#motivational", "#quotes")
        }
        if (title.length > 100) {
            title = title.replace("#quotes", "")
        }
        if (title.length > 100) {
            title = title.replace("#viral", "")
        }
        if (title.length > 100) {
            title = title.replace("#shorts", "")
        }
        if (title.length > 100) {
            title = title.replace("Motivational status video", "")
        }

        if (title.length > 100) {
            title = title.replace(" / ", ": ").trim { it <= ' ' }
        }


        val simpleTitle = generateTitle(file).split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[0]
        val description = generateDescriptionFromTemplate(context, tags, simpleTitle)

        val contentUri = getUriFromFile(context, file)
        sendMultiple(context, file.absolutePath, tags, title, description, contentUri)
    }


    //permission String cls0 = "com.google.android.apps.youtube.app.extensions.upload.UploadActivity";
    //    Intent.ACTION_SEND_MULTIPLE
    //    String cls0 = "com.google.android.apps.youtube.app.application.Shell_MultipleUploadsActivity";
    //Intent.ACTION_SEND || "com.google.android.youtube.intent.action.INTERNAL_UPLOAD"
    //"com.google.android.apps.youtube.app.application.Shell_UploadActivity"
    override fun videoShare(context: Context, videoPath: String) {
        //send(context, videoPath, Shell_UploadActivity, Intent.ACTION_SEND);


        val file = File(videoPath)
        val tags: List<String> = ArrayList()
        val title = file.name
        val description = ""


        val contentUri = getUriFromFile(context, file)
        sendMultiple(context, videoPath, tags, title, description, contentUri)
    }

    private fun sendMultiple(
        context: Context,
        videoPath: String,
        tags: List<String>?,
        title: String,
        description: String,
        contentUri: Uri
    ) {
        try {
            val csYoutubePackage = dec0(SharedNetwork.Package.tube)
            val intent1 = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent1.setPackage(csYoutubePackage)


            //  No more work (          ComponentName componentName = new ComponentName(csYoutubePackage, Shell_MultipleUploadsActivity);
//  No more work (          //DLog.d("COMPONENT NAME==> " + componentName);
//  No more work (          intent1.setComponent(componentName);
            if (videoPath.isNotEmpty()) {
                //                Uri contentUri;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
//                            + KEYFILEPROVIDER, new File(videoPath));
//                } else {
//                    contentUri = Uri.fromFile(new File(videoPath));
//                }

                val uris = ArrayList<Uri>()

                uris.add(contentUri)


                //                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);
                intent1.setType("video/*")
                intent1.putExtra(Intent.EXTRA_STREAM, uris)


                //intent1.putExtra(Intent.EXTRA_TIME, descriptions);

                //Описание
                intent1.putExtra(Intent.EXTRA_SUBJECT, description)


                //Meta Tags
                if (!tags.isNullOrEmpty()) {
                    val eTags = java.lang.String.join(",", tags)
                    intent1.putExtra(Intent.EXTRA_TEXT, eTags)
                }

                //Название
                intent1.putExtra(
                    "com.google.android.libraries.youtube.upload.extra_upload_activity_preset_title",
                    title
                )

                intent1.putExtra(Intent.EXTRA_TITLE, title)
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.grantUriPermission(
                    csYoutubePackage,
                    contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val resInfoList = resolveForUri(context, intent1, contentUri)
                //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
                context.startActivity(intent1)
            } else {
                Toast.makeText(context, "Youtube app not installed!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    } //ArrayList<CharSequence> but value was a java.lang.Stringintent.extra.STREAM
    //    private void sendMultiple(Context context, String videoPath, List<String> tags, String title, String description) {
    //        try {
    //            String csYoutubePackage = Utils.dec0(tube);
    //            Intent intent1 = new Intent(Intent.ACTION_SEND_MULTIPLE);
    //            intent1.setPackage(csYoutubePackage);
    //            //ComponentName componentName = new ComponentName(csYoutubePackage, Shell_MultipleUploadsActivity);
    //            //DLog.d("COMPONENT NAME==> " + componentName);
    //            //intent1.setComponent(componentName);
    //            if (intent1 != null && !videoPath.isEmpty()) {
    //
    //    ...
    //
    //                ArrayList<Uri> uris = new ArrayList<>();
    //
    //                uris.add(contentUri);
    ////                uris.add(contentUri);
    ////                uris.add(contentUri);
    ////                uris.add(contentUri);
    ////                uris.add(contentUri);
    //
    //
    //                intent1.setType("video/*");
    //                intent1.putExtra(Intent.EXTRA_STREAM, uris);
    //
    //
    //                //intent1.putExtra(Intent.EXTRA_TIME, descriptions);
    //
    //                //Описание
    //                intent1.putExtra(Intent.EXTRA_SUBJECT, description);
    //
    //
    //                //Meta Tags
    //                String eTags = String.join(",", tags);
    //                intent1.putExtra(Intent.EXTRA_TEXT, eTags);
    //
    //                //Название
    //                intent1.putExtra("com.google.android.libraries.youtube.upload.extra_upload_activity_preset_title", title);
    //
    //                intent1.putExtra(Intent.EXTRA_TITLE, "title 1 aaaaaaaaaaa aaaaaaaaaaaa");
    //                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //
    //                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent1, PackageManager.MATCH_DEFAULT_ONLY);
    //                if (!resInfoList.isEmpty()) {
    //                    for (ResolveInfo resolveInfo : resInfoList) {
    //                        String packageName = resolveInfo.activityInfo.packageName;
    //                        String activityName = resolveInfo.activityInfo.name;
    //                        //DLog.d("[@@@@@" + packageName + "]" + activityName + "@@]");
    //                        context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //                    }
    //                } else {
    //                    DLog.d("Not found activity...");
    //                }
    //
    //                //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
    //                context.startActivity(intent1);
    //            } else {
    //                Toast.makeText(context, "Youtube app not installed!", Toast.LENGTH_SHORT).show();
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //        }
    //    }
    //    private void send(Activity context, String videoPath, String cls0, String s1) {
    //        try {
    //            String csYoutubePackage = Utils.dec0(tube);
    //            Intent intent1 = new Intent(s1);
    //            intent1.setPackage(csYoutubePackage);
    //            ComponentName componentName = new ComponentName(csYoutubePackage, cls0);
    //            DLog.d("COMPONENT NAME==> " + componentName.toString());
    //            intent1.setComponent(componentName);
    //            if (intent1 != null && !videoPath.isEmpty()) {
    //
    //                Uri contentUri;
    //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    //                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
    //                            + KEYFILEPROVIDER, new File(videoPath));
    //
    //                } else {
    //                    contentUri = Uri.fromFile(new File(videoPath));
    //
    //                }
    //                intent1.setType("video/*");
    //                intent1.putExtra(Intent.EXTRA_STREAM, contentUri);
    //                intent1.putExtra(Intent.EXTRA_TITLE, "description");
    //                intent1.putExtra(Intent.EXTRA_TEXT, "description");
    //                intent1.putExtra(Intent.EXTRA_SUBJECT, context.getString(com.walhalla.ui.R.string.app_name));
    //
    //                context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
    //            } else {
    //                Toast.makeText(context, "Youtube app not installed!", Toast.LENGTH_SHORT).show();
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //        }
    //    }
    //    public void videoShare(Activity context, String videoPath) {
    //        try {
    //            String csYoutubePackage = Utils.dec0(tube);
    //            Intent intent = context.getPackageManager().getLaunchIntentForPackage(csYoutubePackage);
    //            if (intent != null && !videoPath.isEmpty()) {
    //                Intent share = new Intent(Intent.ACTION_SEND);
    //                share.setPackage(csYoutubePackage);
    //                Uri contentUri;
    //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    //                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
    //                            + KEYFILEPROVIDER, new File(videoPath));
    //
    //                } else {
    //                    contentUri = Uri.fromFile(new File(videoPath));
    //
    //                }
    //                share.setType("video/*");
    //                share.putExtra(Intent.EXTRA_STREAM, contentUri);
    //                intent.putExtra(Intent.EXTRA_TITLE, "description");
    //                intent.putExtra(Intent.EXTRA_TEXT, "description");
    //                intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(com.walhalla.ui.R.string.app_name));
    //
    //                context.startActivityForResult(share, COMPONENT_REQUEST_CODE);
    //            } else {
    //                Toast.makeText(context, "Youtube app not installed!", Toast.LENGTH_SHORT).show();
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //        }
    //    }
}
