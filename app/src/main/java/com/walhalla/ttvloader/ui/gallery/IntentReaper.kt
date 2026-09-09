package com.walhalla.ttvloader.ui.gallery

import com.walhalla.intentresolver.utils.UriUtils.getUriFromFile
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Proxy
import android.net.Uri
import com.walhalla.ui.DLog
import com.walhalla.ui.plugins.MimeType
import java.io.File
import java.util.ArrayList
import java.util.HashMap
import java.util.Set
import java.util.TreeSet

class IntentReaper {
    //mime_all
    //.apk, if install
    //EXTRA_STREAM
    //EXTRA_STREAM
    //EXTRA_STREAM
    //EXTRA_STREAM
    //Intent.ACTION_CREATE_DOCUMENT,//EXTRA_STREAM
    //@ Intent.ACTION_INSTALL_PACKAGE,
    //Intent.ACTION_UNINSTALL_PACKAGE,
    //Intent.ACTION_GET_CONTENT, //We not use
    //EXTRA_STREAM
    //      Intent.ACTION_SEND_MULTIPLE,//EXTRA_STREAM
    private var list: MutableList<QWrap> = ArrayList()
    private lateinit var pm: PackageManager
    private lateinit var context: Context
    constructor(context: Context) {
        this.context = context
        this.pm = context.getPackageManager()
    }
    //private String dir_mime = "vnd.android.document/directory";
    private val dir_mime: String = "vnd.android.cursor.dir/*"
    //private String dir_mime = "*/*";
    fun makeMimeDir() {
        var fake: File = Troubleshooting.defLocation()
        list = makeMimeActivityList(dir_mime, fake, null, context).toMutableList()
    }
    /**
         * DONT Set com.google.android.packageinstaller
         *List<ResolveInfo> resolvedActivityList = pm.queryIntentServices(intent0, 0);
         * @return
         */
    fun makeMimeApk(file: File) {
        list = makeMimeActivityList("application/vnd.android.package-archive", file, videoActions, context).toMutableList()
    }
    fun makeMimeApk() {
        var fake: File = File(Troubleshooting.defLocation(), "fake.apk")
        list = makeMimeActivityList("application/vnd.android.package-archive", fake, videoActions, context).toMutableList()
    }
    //String url = "/storage/emulated/0/Download/com.Mobilicks.PillIdentifier_v4.apk";
    //apk=new File(url);
    //intent0 = new Intent(action, apkUri);
    //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    fun makemimeAll() {
        var fake: File = Troubleshooting.defLocation()
        for (mime in mime_all) {
            var tmp: MutableList<QWrap> = makeMimeActivityList(mime, null, null, context)
            list.addAll(tmp)
        }
    }
    fun makemimeProxy() {
        var aa: Array<String> = arrayOf(Proxy.PROXY_CHANGE_ACTION)
        var fake: File = Troubleshooting.defLocation()
        list = makeMimeActivityList("*/*", null, aa, context).toMutableList()
    }
    companion object {
        @JvmField var mime_all: MutableSet<String> = TreeSet<String>()
        init {
            mime_all.add("application/x-www-form-urlencoded")
            mime_all.add("application/vnd.android.package-archive")
            mime_all.add("application/octet-stream")
            mime_all.add(MimeType.TEXT_PLAIN)
            mime_all.add("image/jpeg")
            mime_all.add("*/*")
            mime_all.add("image/*")
            mime_all.add("vnd.android.cursor.dir/*")
            mime_all.add("resource/folder")
            mime_all.add("text/csv")
            mime_all.add("vnd.android.document/directory")
            mime_all.add("vnd.android.cursor.dir/lysesoft.andexplorer.director")
        }
        @JvmField var videoActions: Array<String> = arrayOf(Intent.ACTION_VIEW, Intent.ACTION_EDIT, Intent.ACTION_ATTACH_DATA, Intent.ACTION_INSERT, Intent.ACTION_DELETE, Intent.ACTION_OPEN_DOCUMENT, Intent.ACTION_SEND, Intent.ACTION_SENDTO)
        @JvmStatic fun getServices(actions: Array<String>, context: Context): MutableList<ResolveInfo> {
            var list: MutableList<ResolveInfo> = ArrayList()
            var pm: PackageManager = context.getPackageManager()
            for (action in actions) {
                var intent: Intent = Intent(action)
                var resolveInfoList = pm.queryIntentServices(intent, 0)
                if (!resolveInfoList.isEmpty()) {
                    list.addAll(resolveInfoList)
                }
            }
            return list
        }
        @JvmStatic fun displayServicesInfo(actions: Array<String>, context: Context) {
            var m: MutableList<ResolveInfo> = getServices(actions, context)
            for (resolveInfo in m) {
                var packageName: String = resolveInfo.serviceInfo.packageName
                var serviceName: String = resolveInfo.serviceInfo.name
                DLog.d("@--> " + packageName + " " + serviceName)
            }
        }
        @JvmStatic fun makeMimeActivityList(mime: String, file: File?, actions: Array<String>?, context: Context): MutableList<QWrap> {
            var list: MutableList<QWrap> = ArrayList()
            var pm: PackageManager = context.getPackageManager()
            try {
                var actionsLocal = actions
                if (actionsLocal == null) {
                    actionsLocal = Mimiq.actions0
                }
                var apkUri: Uri? = null
                if (file != null) {
                    apkUri = getUriFromFile(context, file)
                }
                var map: HashMap<String, MutableList<ResolveInfo>> = HashMap()
                for (action in actionsLocal) {
                    var intent0: Intent = intentMaker(action, mime, apkUri)
                    var resolvedActivityList = pm.queryIntentActivities(intent0, 0)
                    if (resolvedActivityList.size > 0) {
                        if (Intent.ACTION_VIEW.equals(action)) {
                            var newValue: MutableList<ResolveInfo> = ArrayList()
                            for (info in resolvedActivityList) {
                                var packageName: String = Util.packageName(info) ?: ""
                                if (!packageName.startsWith("com.google.android.packageinstaller")) {
                                    newValue.add(info)
                                }
                            }
                            map.put(action, newValue)
                        } else {
                            map.put(action, resolvedActivityList.toMutableList())
                        }
                    }
                }
                list.add(QWrap(mime, map))
            } catch (r: Exception) {
                DLog.handleException(r)
            }
            return list
        }
        @JvmStatic fun intentMaker(action: String, mime: String?, apkUri: Uri?): Intent {
            var intent0: Intent = Intent(action)
            if (mime != null) {
                if (apkUri != null) {
                    intent0.setDataAndType(apkUri, mime)
                } else {
                    intent0.setType(mime)
                }
            }
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return intent0
        }
    }
}
