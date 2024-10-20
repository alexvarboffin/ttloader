package com.walhalla.ttvloader.ui.gallery;

import static com.walhalla.intentresolver.utils.UriUtils.getUriFromFile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Proxy;
import android.net.Uri;

import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IntentReaper {

    public static Set<String> mime_all = new TreeSet<>(); //mime_all

    static {
        mime_all.add("application/x-www-form-urlencoded");
        mime_all.add("application/vnd.android.package-archive");//.apk, if install
        mime_all.add("application/octet-stream");
        mime_all.add(MimeType.TEXT_PLAIN);
        mime_all.add("image/jpeg");
        mime_all.add("*/*");
        mime_all.add("image/*");
        mime_all.add("vnd.android.cursor.dir/*");
        mime_all.add("resource/folder");
        mime_all.add("text/csv");
        mime_all.add("vnd.android.document/directory");
        mime_all.add("vnd.android.cursor.dir/lysesoft.andexplorer.director");
    }


    static String[] videoActions = new String[]{
            Intent.ACTION_VIEW,//EXTRA_STREAM
            Intent.ACTION_EDIT,//EXTRA_STREAM
            Intent.ACTION_ATTACH_DATA,//EXTRA_STREAM
            Intent.ACTION_INSERT,//EXTRA_STREAM
            //Intent.ACTION_CREATE_DOCUMENT,//EXTRA_STREAM


            //@@@@@ Intent.ACTION_INSTALL_PACKAGE,
            //Intent.ACTION_UNINSTALL_PACKAGE,

            //Intent.ACTION_GET_CONTENT, //We not use
            Intent.ACTION_DELETE,
            Intent.ACTION_OPEN_DOCUMENT,
            Intent.ACTION_SEND,//EXTRA_STREAM
            //      Intent.ACTION_SEND_MULTIPLE,//EXTRA_STREAM
            Intent.ACTION_SENDTO,
    };
    private List<QWrap> list = new ArrayList<>();

    private final PackageManager pm;
    private final Context context;

    public IntentReaper(Context context) {
        this.context = context;
        this.pm = context.getPackageManager();
    }

    //private String dir_mime = "vnd.android.document/directory";
    private final String dir_mime = "vnd.android.cursor.dir/*";

    //private String dir_mime = "*/*";

    public void makeMimeDir() {
        File fake = Troubleshooting.defLocation();
        list = makeMimeActivityList(dir_mime, fake, null, context);
    }

    public static List<ResolveInfo> getServices(String[] actions, Context context) {
        List<ResolveInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        for (String action : actions) {
            Intent intent = new Intent(action);
            List<ResolveInfo> resolveInfoList = pm.queryIntentServices(intent, 0);
            if (!resolveInfoList.isEmpty()) {
                list.addAll(resolveInfoList);
            }
        }
        return list;
    }

    public static void displayServicesInfo(String[] actions, Context context) {
        List<ResolveInfo> m = getServices(actions, context);
        for (ResolveInfo resolveInfo : m) {
            String packageName = resolveInfo.serviceInfo.packageName;
            String serviceName = resolveInfo.serviceInfo.name;
            DLog.d("@@@--> " + packageName + " " + serviceName);
        }
    }

    /**
     * DONT Set com.google.android.packageinstaller
     *List<ResolveInfo> resolvedActivityList = pm.queryIntentServices(intent0, 0);
     * @return
     */
    public static List<QWrap> makeMimeActivityList(String mime, File file, String[] actions, Context context) {
        List<QWrap> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        try {
            if (actions == null) {
                actions = Mimiq.actions0;
            }
            Uri apkUri = null;
            if (file != null) {
                apkUri = getUriFromFile(context, file);
            }
            Map<String, List<ResolveInfo>> map = new HashMap<>();
            for (String action : actions) {
                Intent intent0 = intentMaker(action, mime, apkUri);
                List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(intent0, 0);
                if (resolvedActivityList.size() > 0) {
                    if (Intent.ACTION_VIEW.equals(action)) {
                        List<ResolveInfo> newValue = new ArrayList<>();
                        for (ResolveInfo info : resolvedActivityList) {
                            String packageName = Util.packageName(info);
                            if (!packageName.startsWith("com.google.android.packageinstaller")) {
                                newValue.add(info);
                            }
                        }
                        map.put(action, newValue);
                    } else {
                        map.put(action, resolvedActivityList);
                    }
                }
            }
            list.add(new QWrap(mime, map));
        } catch (IllegalArgumentException r) {
            DLog.handleException(r);
        }
        return list;
    }


    public void makeMimeApk(File file) {
        list = makeMimeActivityList("application/vnd.android.package-archive", file, videoActions, context);
    }

    public void makeMimeApk() {
        File fake = new File(Troubleshooting.defLocation(), "fake.apk");
        list = makeMimeActivityList("application/vnd.android.package-archive", fake, videoActions, context);
    }

    public static Intent intentMaker(String action, String mime, Uri apkUri) {
        Intent intent0 = new Intent(action);
        if (mime != null) {
            //String url = "/storage/emulated/0/Download/com.Mobilicks.PillIdentifier_v4.apk";
            //apk=new File(url);
            //intent0 = new Intent(action, apkUri);
            if (apkUri != null) {
                intent0.setDataAndType(apkUri, mime);
            } else {
                intent0.setType(mime);
            }
        }

//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent0;
    }


    public void makemimeAll() {
        File fake = Troubleshooting.defLocation();
        for (String mime : mime_all) {
            List<QWrap> tmp = makeMimeActivityList(mime, null, null, context);
            list.addAll(tmp);
        }
    }

    public void makemimeProxy() {
        String[] aa = {
                Proxy.PROXY_CHANGE_ACTION
        };
        File fake = Troubleshooting.defLocation();
        list = makeMimeActivityList("*/*", null, aa, context);
    }
}
