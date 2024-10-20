package com.walhalla.intentresolver;


import static com.walhalla.abcsharedlib.SharedNetwork.Package.tube;
import static com.walhalla.intentresolver.utils.TextUtilz.dec0;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import android.widget.Toast;

import com.walhalla.intentresolver.utils.TextUtilz;
import com.walhalla.intentresolver.utils.UriUtils;
import com.walhalla.intentresolver.utils.YoutubeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YoutubeIntent extends BaseIntent {

//    "android.intent.extra.TITLE"
//            "android.intent.extra.SUBJECT"
//            "android.intent.extra.TEXT"

    private String Shell_UploadActivity = "com.google.android.apps.youtube.app.application.Shell_UploadActivity";

    public YoutubeIntent() {
        super(dec0(tube));
    }
    //private String Shell_MultipleUploadsActivity = "com.google.android.apps.youtube.app.application.Shell_MultipleUploadsActivity";


    @Override
    public void shareMp4Selector(Context context, File file) {
        //resolveMp4ActivitiesForPackage(context, file, Utils.dec0(tube));

        //List<String> tags = TextUtilz.readAllLines(context, "Chist_tags.txt");
        List<String> tags = TextUtilz.readAllLines(context, "tags.txt");

        String title = TextUtilz.extractTextBetween(file.getName())
                .replace("__", "/");

        if (title.length() > 100) {
            title = title.replace("video.", "video");
        }
        if (title.length() > 100) {
            title = title.replace("#motivational", "#quotes");
        }
        if (title.length() > 100) {
            title = title.replace("#quotes", "");
        }
        if (title.length() > 100) {
            title = title.replace("#viral", "");
        }
        if (title.length() > 100) {
            title = title.replace("#shorts", "");
        }
        if (title.length() > 100) {
            title = title.replace("Motivational status video", "");
        }

        if (title.length() > 100) {
            title = title.replace(" / ", ": ").trim();
        }


        String simpleTitle = YoutubeUtils.generateTitle(file).split("\\.")[0];
        String description = YoutubeUtils.generateDescriptionFromTemplate(context, tags, simpleTitle);

        Uri contentUri = UriUtils.getUriFromFile(context, file);
        sendMultiple(context, file.getAbsolutePath(), tags, title, description, contentUri);
    }


    //permission String cls0 = "com.google.android.apps.youtube.app.extensions.upload.UploadActivity";


//    Intent.ACTION_SEND_MULTIPLE
//    String cls0 = "com.google.android.apps.youtube.app.application.Shell_MultipleUploadsActivity";

    //Intent.ACTION_SEND || "com.google.android.youtube.intent.action.INTERNAL_UPLOAD"
    //"com.google.android.apps.youtube.app.application.Shell_UploadActivity"

    public void videoShare(Context context, String videoPath) {
        //send(context, videoPath, Shell_UploadActivity, Intent.ACTION_SEND);


        File file = new File(videoPath);
        List<String> tags = new ArrayList<>();
        String title = file.getName();
        String description = "";


        Uri contentUri = UriUtils.getUriFromFile(context, file);
        sendMultiple(context, videoPath, tags, title, description, contentUri);
    }

    private void sendMultiple(Context context, String videoPath, List<String> tags, String title, String description,
                              Uri contentUri) {
        try {
            String csYoutubePackage = dec0(tube);
            Intent intent1 = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent1.setPackage(csYoutubePackage);


//  No more work (          ComponentName componentName = new ComponentName(csYoutubePackage, Shell_MultipleUploadsActivity);
//  No more work (          //DLog.d("COMPONENT NAME==> " + componentName);
//  No more work (          intent1.setComponent(componentName);


            if (intent1 != null && !videoPath.isEmpty()) {

//                Uri contentUri;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
//                            + KEYFILEPROVIDER, new File(videoPath));
//                } else {
//                    contentUri = Uri.fromFile(new File(videoPath));
//                }

                ArrayList<Uri> uris = new ArrayList<>();

                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);


                intent1.setType("video/*");
                intent1.putExtra(Intent.EXTRA_STREAM, uris);


                //intent1.putExtra(Intent.EXTRA_TIME, descriptions);

                //Описание
                intent1.putExtra(Intent.EXTRA_SUBJECT, description);


                //Meta Tags
                if (tags != null && !tags.isEmpty()) {
                    String eTags = String.join(",", tags);
                    intent1.putExtra(Intent.EXTRA_TEXT, eTags);
                }

                //Название
                intent1.putExtra("com.google.android.libraries.youtube.upload.extra_upload_activity_preset_title", title);

                intent1.putExtra(Intent.EXTRA_TITLE, title);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.grantUriPermission(csYoutubePackage, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                List<ResolveInfo> resInfoList = resolveForUri(context, intent1, contentUri);
                //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
                context.startActivity(intent1);
            } else {
                Toast.makeText(context, "Youtube app not installed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }



//ArrayList<CharSequence> but value was a java.lang.Stringintent.extra.STREAM
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
