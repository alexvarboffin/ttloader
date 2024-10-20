//package com.walhalla.ttvloader.intentresolver;
//
//import static com.walhalla.compat.UriUtils.KEYFILEPROVIDER;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.widget.Toast;
//
//import androidx.core.content.FileProvider;
//
//import com.walhalla.abcsharedlib.SharedNetwork;
//import com.walhalla.ttvloader.BuildConfig;
//import com.walhalla.ttvloader.R;
//import com.walhalla.ui.DLog;
//
//import java.io.File;
//import java.util.List;
//
//public class PinterestIntent extends DefaultIntent {
//
//    String EXTRA_URL = "com.pinterest.EXTRA_URL";
//
//
//    @Override
//    public void videoShare(Context context, String path) {
//        Toast.makeText(context, "@@@@@", Toast.LENGTH_SHORT).show();
//        String url = "https://play.google.com/store/apps/details?id=com.walhalla.ttloader";
//
//
//        //File file = new File("/storage/emulated/0/Movies/1717050075775.mp4");
//        File file = new File("/storage/emulated/0/Movies/1717050046576.mp4");
//
//        Uri contentUri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
//                    + KEYFILEPROVIDER, file);
//        } else {
//            contentUri = Uri.fromFile(file);
//        }
//        sendv(context, contentUri);
//    }
//
//    private void sendv(Context context, Uri url) {
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.example.client0000", "com.pinterest.feature.pin.creation.CreationActivity"));
//        intent.putExtra("1", "*");
//        intent.putExtra("2", ".");
//        intent.putExtra("3", "*");
//        intent.putExtra("4", "*");
//        intent.putExtra("com.pinterest.EXTRA_IS_DEEPLINK", true);
//
//
//        intent.putExtra("com.pinterest.EXTRA_IDEA_PIN_CREATION_SESSION_ID", "123");
//        intent.putExtra("com.pinterest.EXTRA_CREATE_MEDIA_URI", url);
//
//        intent.putExtra("com.pinterest.EXTRA_MEDIA_URI_IS_VIDEO", true);
//        //this.f50644m = bundle3.getBoolean("com.pinterest.EXTRA_IS_COLLAGE");
//
//        intent.putExtra("com.pinterest.EXTRA_PIN_CREATE_TYPE", "type");
//
//        //String string2 = bundle3.getString("com.pintrest.EXTRA_DEEPLINK_SOURCE");
//
//        intent.putExtra("com.pinterest.EXTRA_IS_STORY_PIN_DRAFT", false);
//        intent.putExtra("com.pinterest.EXTRA_STORY_PIN_TRIM_REQUIRED", false);
//        intent.putExtra("com.pinterest.EXTRA_STORY_PIN_CREATION_ENTRY_TYPE", "string3");
//        intent.putExtra("com.pinterest.EXTRA_BOARD_ID", "string4");
//
//        intent.putExtra("com.pinterest.EXTRA_BOARD_SECTION_ID", "string5");
//        intent.putExtra("com.pinterest.EXTRA_COMMENT_ID", "string6");
//        intent.putExtra("com.pinterest.EXTRA_COMMENT_AUTHOR_NAME", "string7");
//        intent.putExtra("com.pinterest.EXTRA_COMMENT_TEXT", "string8");
////        String string9 = bundle3.getString("com.pinterest.EXTRA_COMMENT_PIN_ID");
////        if (string9 != null) {
////            intent.putExtra("com.pinterest.EXTRA_COMMENT_PIN_ID", string9);
////        }
//        //intent.putExtra("com.pinterest.EXTRA_COMMENT_PIN_THUMBNAIL_PATH", "string10");
//        intent.putExtra("com.pinterest.EXTRA_COMMENT_PIN_IS_ACCESSIBLE", true);
//        intent.putExtra("com.pinterest.EXTRA_MEDIA_GALLERY_TYPE", "galleryRouter");
//
////        bundle4.putInt("com.pinterest.EXTRA_PIN_SCHEDULED_TIME_SECONDS", bundle3.getInt("com.pinterest.EXTRA_PIN_SCHEDULED_TIME_SECONDS"));
//
////        if (Intrinsics.d(c13, w1.e())) {
////            String string11 = bundle4.getString("com.pinterest.EXTRA_MEDIA_GALLERY_TYPE");
////            if (string11 == null) {
////                string11 = "StoryPinPageAdd";
////            }
////            intent.putExtra("com.pinterest.EXTRA_MEDIA_GALLERY_TYPE", string11);
////            if (this.f50654w == null) {
////                Intrinsics.t("galleryRouter");
////                throw null;
////            }
////            bundle4.putBoolean("com.pinterest.EXTRA_ENABLE_VIDEO_UPLOAD", l.f(a.n.valueOf(string11)));
////            bundle4.putInt("com.pinterest.EXTRA_LOCAL_STORY_PIN_CLIP_COUNT", bundle4.getInt("com.pinterest.EXTRA_LOCAL_STORY_PIN_CLIP_COUNT"));
////            intent.putExtra("com.pinterest.EXTRA_MEDIA_GALLERY_MAX_SELECTED_ITEMS", 20);
////        }
//
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        PackageManager packageManager = context.getPackageManager();
//        List<ResolveInfo> resInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (!resInfoList.isEmpty()) {
//            DLog.d("Found activity: " + resInfoList.get(0).activityInfo.name);
//            context.startActivity(intent);
//        } else {
//            DLog.d("Not found activity...");
//        }
//        context.startActivity(intent);
//    }
//
//
//    private void realEmpty(Context context, Uri url) {
//        Intent intent = new Intent();
//        intent.putExtra("1", "*");
//        intent.putExtra("2", ".");
//        intent.putExtra("3", "*");
//        intent.putExtra("4", "*");
//
//        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (!resInfoList.isEmpty()) {
//            for (ResolveInfo resolveInfo : resInfoList) {
//                String packageName = resolveInfo.activityInfo.packageName;
//                String activityName = resolveInfo.activityInfo.name;
//                if (packageName.contains("000")) {
//                    DLog.d("[" + packageName + "]" + activityName);
//                }
//                context.grantUriPermission(packageName, url, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }
//        } else {
//            DLog.d("Not found activity...");
//        }
//
//        Intent chozzer = Intent.createChooser(intent, "12");
//        chozzer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context.startActivity(chozzer);
//    }
//
////    private void sendVideo(Context context, Uri url) {
////
////        //creationActivity
////        //com.pinterest.activity.create.PinItActivity
////
////        String title = context.getResources().getString(R.string.sharing_video_title);
////
////        Intent intent = new Intent(Intent.ACTION_SEND);
////        intent.setType("image/*");
////
////
////        //intent.setDataAndType(path, MIME_TYPE_JPEG); //Not application/octet-stream type
////        intent.putExtra(Intent.EXTRA_STREAM, url);
////
//////        intent.putExtra(EXTRA_URL, url); or intent.putExtra(Intent.EXTRA_TEXT, url);//saved with clickable url
////
////
////        //intent.putExtra(EXTRA_URL, url);//eny type image/* or image/text
////
////
////        intent.putExtra(Intent.EXTRA_TEXT, title);
////        //intent.putExtra(Intent.EXTRA_EMAIL, "alexvarboffin@gmail.com");//Work only with intent.setType("*/*");
////        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
////        intent.putExtra(SharedNetwork.comPinterestEXTRA_DESCRIPTION, title);
////        intent.putExtra("com.pinterest.EXTRA_BOARD_ID", "zzzzz");
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////
////        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
////        if (!resInfoList.isEmpty()) {
////            for (ResolveInfo resolveInfo : resInfoList) {
////                String packageName = resolveInfo.activityInfo.packageName;
////                String activityName = resolveInfo.activityInfo.name;
////                if (packageName.contains("000")) {
////                    DLog.d("[" + packageName + "]" + activityName);
////                }
////                context.grantUriPermission(packageName, url, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            }
////        } else {
////            DLog.d("Not found activity...");
////        }
////
////        Intent chozzer = Intent.createChooser(intent, title);
////        chozzer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////        context.startActivity(chozzer);
////    }
//    //With url in description
////    private void sendUrl(Context context, String url) {
////
////        String title = context.getResources().getString(R.string.sharing_video_title) + ": " + url;
////
////        Intent intent = new Intent(Intent.ACTION_SEND);
////        intent.putExtra(EXTRA_URL, url);
////        intent.setType(MimeType.TEXT_PLAIN);
////
////
////        //intent.putExtra(Intent.EXTRA_EMAIL, "alexvarboffin@gmail.com");//Work only with intent.setType("*/*");
////        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
////        //intent.setType("*/*");
////        intent.putExtra(SharedNetwork.comPinterestEXTRA_DESCRIPTION, url);
////        intent.putExtra("com.pinterest.EXTRA_BOARD_ID", "zzzzz");
////
////
////        Intent chozzer = Intent.createChooser(intent, title);
////        chozzer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(chozzer);
////    }
//
////    private void sendUrl(Context context, String url) {
////
////        //com.pinterest.activity.create.PinItActivity
////
////        String title = context.getResources().getString(R.string.sharing_video_title);
////
////        Intent intent = new Intent(Intent.ACTION_SEND);
////        intent.setType(MimeType.TEXT_PLAIN);
////
////
////
////
//////        intent.putExtra(EXTRA_URL, url); or intent.putExtra(Intent.EXTRA_TEXT, url);//saved with clickable url
////        intent.putExtra(EXTRA_URL, url);
////        intent.putExtra(Intent.EXTRA_TEXT, title);
////        //intent.putExtra(Intent.EXTRA_EMAIL, "alexvarboffin@gmail.com");//Work only with intent.setType("*/*");
////        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
////        intent.putExtra(SharedNetwork.comPinterestEXTRA_DESCRIPTION, title);
////        intent.putExtra("com.pinterest.EXTRA_BOARD_ID", "zzzzz");
////
////        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
////        if (!resInfoList.isEmpty()) {
////            for (ResolveInfo resolveInfo : resInfoList) {
////                String packageName = resolveInfo.activityInfo.packageName;
////                String activityName = resolveInfo.activityInfo.name;
////                if(packageName.contains("000")){
////                    DLog.d("[" + packageName + "]" + activityName);
////                }
////                //context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            }
////        } else {
////            DLog.d("Not found activity...");
////        }
////
////        Intent chozzer = Intent.createChooser(intent, title);
////        chozzer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(chozzer);
////    }
//
////    private void sendUrl(Context context, String url) {
////
////        //com.pinterest.activity.create.PinItActivity
////
////        String title = context.getResources().getString(R.string.sharing_video_title);
////
//////        Intent intent = new Intent(Intent.ACTION_SEND);
//////        intent.setType(MimeType.TEXT_PLAIN);
////
////        Intent intent = new Intent("com.pinterest.action.PIN_IT");
////        intent.addCategory(Intent.CATEGORY_DEFAULT);
////
////
//////        intent.putExtra(EXTRA_URL, url);
////        intent.putExtra(Intent.EXTRA_TEXT, title);
////
//////
//////
//////        //intent.putExtra(Intent.EXTRA_EMAIL, "alexvarboffin@gmail.com");//Work only with intent.setType("*/*");
//////        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
//////        intent.putExtra(SharedNetwork.comPinterestEXTRA_DESCRIPTION, title);
//////        intent.putExtra("com.pinterest.EXTRA_BOARD_ID", "zzzzz");
////
////        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
////        if (!resInfoList.isEmpty()) {
////            for (ResolveInfo resolveInfo : resInfoList) {
////                String packageName = resolveInfo.activityInfo.packageName;
////                String activityName = resolveInfo.activityInfo.name;
////                if (packageName.contains("000")) {
////                    DLog.d("[" + packageName + "]" + activityName);
////                }
////                //context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            }
////        } else {
////            DLog.d("Not found activity...");
////        }
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(intent);
////    }
//}