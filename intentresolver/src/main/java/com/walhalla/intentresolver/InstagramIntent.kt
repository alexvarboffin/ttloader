package com.walhalla.intentresolver

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.walhalla.abcsharedlib.SharedNetwork
import com.walhalla.intentresolver.utils.TextUtilz.extractTextBetween
import com.walhalla.intentresolver.utils.TextUtilz.readAllLines
import com.walhalla.intentresolver.utils.UriUtils.getUriFromFile
import com.walhalla.intentresolver.utils.YoutubeUtils.generateDescriptionFromTemplate
import com.walhalla.intentresolver.utils.YoutubeUtils.generateTitle
import java.io.File


class InstagramIntent  //[@@@@@com.instagram.android]com.instagram.direct.share.handler.DirectExternalMediaShareActivityVideoInterop@@] █
    : BaseIntent(SharedNetwork.Package.INSTAGRAM) {
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
    var key_reels: String = "com.instagram.share.handleractivity.ReelShareHandlerActivity"
    var key_story: String = "com.instagram.share.handleractivity.StoryShareHandlerActivity"

    override fun shareMp4Selector(context: Context, file: File) {
//resolveMp4ActivitiesForPackage(context, file, Utils.dec0(tube));

        //List<String> tags = TextUtilz.readAllLines(context, "Chist_tags.txt");

        val tags = readAllLines(context, "tags.txt")

        var title = extractTextBetween(file.name)!!.replace("__", "/")

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
        sendReels(context, file.absolutePath, tags, title, description, contentUri)
    }

    private fun sendReels(
        context: Context,
        videoPath: String,
        tags: List<String>?,
        title: String,
        description: String,
        contentUri: Uri
    ) {
        try {
            val mm = Intent.ACTION_SEND_MULTIPLE
            val intent1 = Intent(mm)
            intent1.setPackage(packageName)
            val componentName = ComponentName(
                packageName!!, key_reels
            )
            intent1.setComponent(componentName)


            if (intent1 != null && !videoPath.isEmpty()) {
                //                Uri contentUri;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
//                            + KEYFILEPROVIDER, new File(videoPath));
//                } else {
//                    contentUri = Uri.fromFile(new File(videoPath));
//                }


                intent1.setType("video/*")
                if (mm == Intent.ACTION_SEND_MULTIPLE) {
                    val uris = ArrayList<Uri>()
                    uris.add(contentUri)
                    intent1.putExtra(Intent.EXTRA_STREAM, uris)
                } else {
                    intent1.putExtra(Intent.EXTRA_STREAM, contentUri)
                }
                intent1.putExtra("ShareHandlerActivity.IS_FROM_INSTAGRAM", true)
                intent1.putExtra("caption_text", title)
                intent1.putExtra(
                    "ReelHashtagStickerConstants.ARGUMENTS_KEY_HASHTAG_STICKER_TEXT",
                    "#123"
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    intent1.putExtra(Intent.EXTRA_TIME, description)
                }

                //Описание
                intent1.putExtra(Intent.EXTRA_SUBJECT, description)


                //Meta Tags
                if (tags != null && !tags.isEmpty()) {
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
                    packageName,
                    contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val resInfoList = resolveForUri(context, intent1, contentUri)
                //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
                if (resInfoList.isNotEmpty()) {
                    context.startActivity(intent1)
                }
            } else {
                Toast.makeText(context, "Instagram app not installed!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun videoShare(context: Context, path: String) {
    }
}
