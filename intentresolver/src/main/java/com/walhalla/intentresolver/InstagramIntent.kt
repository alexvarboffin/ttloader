package com.walhalla.intentresolver;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.walhalla.abcsharedlib.SharedNetwork;
import com.walhalla.intentresolver.utils.TextUtilz;
import com.walhalla.intentresolver.utils.UriUtils;
import com.walhalla.intentresolver.utils.YoutubeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstagramIntent extends BaseIntent {
//   <activity theme="Theme.AppCompat.DayNight.NoActionBar"
//   name="com.instagram.share.handleractivity.ReelShareHandlerActivity" exported="true"
//   taskAffinity="com.instagram.share.handleractivity.ReelShareHandlerActivity" excludeFromRecents="true" launchMode="3" noHistory="true">
//    <meta-data name="enable-stage" value="enable-cold-pretos" />
//    <intent-filter label="Reels">
//     <action name="android.intent.action.SEND" />
//     <action name="android.intent.action.SEND_MULTIPLE" />
//     <category name="android.intent.category.DEFAULT" />
//     <data mimeType="video/*" />
//     <data mimeType="image/*" />
//   </intent-filter>
//  </activity>
    //[@@@@@com.instagram.android]com.instagram.share.handleractivity.ShareHandlerActivity@@] █


    String key_reels = "com.instagram.share.handleractivity.ReelShareHandlerActivity";
    String key_story = "com.instagram.share.handleractivity.StoryShareHandlerActivity";

//[@@@@@com.instagram.android]com.instagram.direct.share.handler.DirectExternalMediaShareActivityVideoInterop@@] █

    public InstagramIntent() {
        super(SharedNetwork.Package.INSTAGRAM);
    }

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
        sendReels(context, file.getAbsolutePath(), tags, title, description, contentUri);
    }

    private void sendReels(Context context, String videoPath, List<String> tags, String title, String description,
                           Uri contentUri) {
        try {
            String mm = Intent.ACTION_SEND_MULTIPLE;
            Intent intent1 = new Intent(mm);
            intent1.setPackage(packageName);
            ComponentName componentName = new ComponentName(packageName, key_reels);
            intent1.setComponent(componentName);


            if (intent1 != null && !videoPath.isEmpty()) {

//                Uri contentUri;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
//                            + KEYFILEPROVIDER, new File(videoPath));
//                } else {
//                    contentUri = Uri.fromFile(new File(videoPath));
//                }


                intent1.setType("video/*");
                if (mm.equals(Intent.ACTION_SEND_MULTIPLE)) {
                    ArrayList<Uri> uris = new ArrayList<>();
                    uris.add(contentUri);
                    intent1.putExtra(Intent.EXTRA_STREAM, uris);
                } else {
                    intent1.putExtra(Intent.EXTRA_STREAM, contentUri);
                }
                intent1.putExtra("ShareHandlerActivity.IS_FROM_INSTAGRAM", true);
                intent1.putExtra("caption_text", title);
                intent1.putExtra("ReelHashtagStickerConstants.ARGUMENTS_KEY_HASHTAG_STICKER_TEXT", "#123");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    intent1.putExtra(Intent.EXTRA_TIME, description);
                }

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
                context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                List<ResolveInfo> resInfoList = resolveForUri(context, intent1, contentUri);
                //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
                if (!resInfoList.isEmpty()) {
                    context.startActivity(intent1);
                }
            } else {
                Toast.makeText(context, "Instagram app not installed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void videoShare(Context context, String path) {

    }
}
