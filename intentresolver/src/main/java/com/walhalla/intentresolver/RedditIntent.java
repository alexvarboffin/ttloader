//package com.walhalla.intentresolver;
//
//
//import static com.walhalla.intentresolver.IntentUtils.makeVideoShare;
//import static com.walhalla.intentresolver.utils.UriUtils.getUriFromFile;
//
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.widget.Toast;
//
//import com.walhalla.abcsharedlib.Share;
//import com.walhalla.abcsharedlib.SharedNetwork;
//import com.walhalla.intentresolver.utils.UriUtils;
//import com.walhalla.ui.BuildConfig;
//import com.walhalla.ui.DLog;
//
//import java.io.File;
//import java.util.List;
//
//public class RedditIntent extends BaseIntent {
//
//    public RedditIntent() {
//        super(SharedNetwork.Package.REDDIT);
//    }
//
//
//    @Override
//    public void shareMp4Selector(Context context, File file) {
//        if (file.exists()) {
//            Uri uri = getUriFromFile(context, file);
//            if (uri != null) {
////                        String type = context.getContentResolver().getType(uri);
////                        DLog.d("___E 1___ " + type + " " + mime + " " + packageName);
////                        DLog.d("___E 1___ " + uri);
////                        DLog.d("___E 1___ " + file);
//
//
////String url = "/data/app/SmokeTestApp/SmokeTestApp.apk";
//
////            if (!url.startsWith("http://") && !url.startsWith("https://")) {
////                url = "http://" + url;
////            }
////            uri = Uri.parse(url);
//
//                String extraValue = context.getString(R.string.app_name);
//                Intent intent1 = new Intent(Intent.ACTION_SEND, uri);
//                intent1.setPackage(packageName);
//                intent1.addCategory("android.intent.category.DEFAULT");
//                intent1.putExtra(Intent.EXTRA_TEXT, extraValue);
////                    if (1 == 1) {
////                        intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"alexvarboffin@gmai.com"});
////                    }
//                //Gmail title
//                //DropBox - document name
//                intent1.putExtra("com.reddit.frontpage.requires_init", true);
//                intent1.putExtra("com.reddit.frontpage.extra_image_url", "str");
//                intent1.putExtra("com.reddit.frontpage.extra_source_page", (String) null);
//                intent1.putExtra("com.reddit.frontpage.extra_type", 4);
//                intent1.putExtra("com.reddit.frontpage.extra_image_width", "i11");
//                intent1.putExtra("com.reddit.frontpage.extra_image_height", "i10");
//                intent1.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
//                DLog.d("------------------------------------");
//
//                //intent1.setDataAndType(uri, "video/mp4");
//                //intent1.putExtra(Intent.EXTRA_STREAM, uri);
//
////                            intent.putExtra(Intent.EXTRA_SUBJECT, str);
////                            intent.putExtra("android.intent.extra.TITLE", str);
////                            intent.putExtra("android.intent.extra.STREAM", uri);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
////                            intent.setPackage(packageName);
////                            com.instagram.android/com.instagram.share.handleractivity.ShareHandlerActivity
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//
//                //resolve
//                //componentName = intent1.resolveActivity(pm);
//                ComponentName componentName = new ComponentName(packageName, "com.reddit.sharing.ShareActivity");
//                try {
//                    if (componentName != null) {
//                        DLog.d("COMPONENT NAME==> " + componentName.toString());
//                        intent1.setComponent(componentName);
//                        //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
//                        context.startActivity(intent1);
//
//                    } else {
//                        DLog.d("@@@@@@");
//                    }
//                } catch (ActivityNotFoundException rr9) {
//
//                    DLog.d("___E 2___ " + packageName);
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setPackage(packageName);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        context.startActivity(intent);
//                    } catch (ActivityNotFoundException rr) {
//
//                    }
////                        if (intent2.resolveActivity(getPackageManager()) != null) {
////                            startActivity(intent2);
////                        } else {
////                            DLog.d("@@empty@@@");
////                        }
//                }
//            } else {
//                Toast.makeText(context, "@ Try Latter", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    public void videoShare(Context context, String path) {
//
//    }
//}
