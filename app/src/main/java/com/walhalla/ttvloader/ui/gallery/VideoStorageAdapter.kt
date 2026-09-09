package com.walhalla.ttvloader.ui.gallery

import com.walhalla.intentresolver.utils.UriUtils.getUriFromFile
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walhalla.adapters.EmptyViewModel
import com.walhalla.adapters.ExpandableListAdapter
import com.walhalla.intentresolver.GoogleDocsUtils
import com.walhalla.ttvloader.GlideApp
import com.walhalla.ttvloader.databinding.DialogExpandableListBinding
import com.walhalla.ttvloader.databinding.VideoItemBinding
import com.walhalla.intentresolver.UIntent
import com.walhalla.intentresolver.YoutubeIntent
import com.walhalla.ttvloader.mvp.MainView
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.models.LocalVideo
import com.walhalla.ttvloader.ui.MItem
import com.walhalla.ttvloader.utils.IntentUtils
import com.walhalla.ttvloader.viewholder.RecyclerViewSimpleTextViewHolder
import com.walhalla.ttvloader.viewholder.VideoViewHolder
import com.walhalla.ui.DLog
import java.io.File
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.List
import java.util.Locale
import java.util.Map
import java.util.TreeMap
import java.util.concurrent.TimeUnit

class VideoStorageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private lateinit var intentReaper: IntentReaper
    private lateinit var list0: MutableList<QWrap>
    private lateinit var pm: PackageManager
    private lateinit var defDrawable: Drawable
    var drawableMap = HashMap<Int, Drawable>()
    var topHeaderListGroup: MutableList<MItem> = ArrayList()
    var listGroup: MutableList<MItem> = ArrayList()
    //private final List<AppModel> tmp;
    @JvmField val VIEW_TYPE_VIDEO: Int = 0
    lateinit var mainView: MainView
    private var items: MutableList<Any> = ArrayList()
    private lateinit var context: Activity
    @JvmField var mActiveActionMode: ActionMode? = null
    @JvmField var multiSelect: Boolean = false
    @JvmField var selectedItems: ArrayList<Int> = ArrayList<Int>()
    @JvmField var actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mActiveActionMode = mode
                multiSelect = true
                mode.getMenuInflater().inflate(R.menu.delete, menu)
                return true
            }
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                AlertDialog.Builder(context).setTitle("Delete " + selectedItems.size + " video?").setMessage(R.string.del_confirm).setCancelable(false).setPositiveButton("DELETE", { dialog, whichButton -> 
                            Collections.sort(selectedItems, Collections.reverseOrder())
                            for (intItem in selectedItems) {
                                //  Log.e("Deleted",intItem.toString());
                                // al_video.remove(intItem);
                                var pos: Int = Integer.parseInt(intItem.toString())
                                var mm: Any = items.`get`(pos)
                                if (mm is LocalVideo) {
                                    deleteItem((mm as LocalVideo), pos)
                                }
                            }
                            mode.finish()
                            }).setNegativeButton("CANCEL", null).show()
                return true
            }
            override fun onDestroyActionMode(mode: ActionMode) {
                multiSelect = false
                selectedItems.clear()
                notifyDataSetChanged()
            }
        }
    private var dialog: AlertDialog? = null
    constructor(context: Activity, data: ArrayList<Any>, mainView: MainView) {
        this.items = data
        this.context = context
        this.mainView = mainView
        this.intentReaper = IntentReaper(this.context)
        this.pm = context.getPackageManager()
        //        if (data != null) {
        //            for (LocalVideo localVideo : data) {
        //                DLog.d("@ " + localVideo.toString());
        //            }
        //        }
        list0 = IntentReaper.makeMimeActivityList("video/*", null, IntentReaper.videoActions, context)
        //list0 = IntentReaper.makeMime0("*/*", null, IntentReaper.videoActions, context);
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_action_watch, R.string.action_watch))
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_baseline_delete_forever_24, R.string.action_delete_video))
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_baseline_share_24, R.string.action_share_video))
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_baseline_folder_open_24, R.string.action_folder_open))
        //        tmp = ConfigUtils.makeShareList();
        //        for (AppModel model : tmp) {
        //            if (PackageUtils.isPackageInstalledForLaunch(context, model.appPackageName)) {
        //                menuItems.add(new MItem(R.drawable.ic_baseline_folder_open_24, model.appName)));
        //            }
        //        }
        //@
        defDrawable = ContextCompat.getDrawable(context, R.drawable.ic_action_android)!!
    }
    fun addQMenu(context: Context, icActionWatch: Int, actionWatch: Int): MItem {
        var drawable: Drawable = ContextCompat.getDrawable(context, icActionWatch)!!
        var name0: String = context.getString(actionWatch)
        return MItem(drawable, name0)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        var inflater: LayoutInflater = LayoutInflater.from(parent.getContext()!!)
        if (viewType == VIEW_TYPE_VIDEO) {
            val binding: VideoItemBinding = VideoItemBinding.inflate(inflater, parent, false)
            viewHolder = VideoViewHolder(binding)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            var v2: View = inflater.inflate(R.layout.row_empty, parent, false)
            viewHolder = EmptyViewHolder(v2)
        } else {
            var v: View = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            viewHolder = RecyclerViewSimpleTextViewHolder(v)
        }
        return viewHolder!!
    }
    override fun getItemViewType(position: Int): Int {
        if (items.`get`(position) is LocalVideo) {
            return VIEW_TYPE_VIDEO
        } else if (items.`get`(position) is EmptyViewModel) {
            return VIEW_TYPE_EMPTY
        }
        return 1
    }
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        var viewType: Int = getItemViewType(position)
        if (viewType == VIEW_TYPE_VIDEO) {
            var item: LocalVideo = (items.`get`(position) as LocalVideo)
            var vh1: VideoViewHolder = (viewHolder as VideoViewHolder)
            if (DLog.nonNull(item.thumb)) {
                GlideApp.with(context).load(item.thumb).into(vh1.binding!!.mediaImgBack)
            }
            //.skipMemoryCache(false)
            vh1.bind(item, position, this)
            handleBinding(item, vh1, position)
            vh1.binding!!.frameLayout.setOnLongClickListener(lmb1@{ view -> 
                        (view.getContext()!! as AppCompatActivity)
                        selectItem(vh1, position)
                        return@lmb1 true
                        })
        } else if (viewType == VIEW_TYPE_EMPTY) {
            var vh2: EmptyViewHolder = (viewHolder as EmptyViewHolder)
            vh2.bind(items.`get`(position))
        } else {
            var vh: RecyclerViewSimpleTextViewHolder = (viewHolder as RecyclerViewSimpleTextViewHolder)
            vh.bind((items.`get`(position) as String))
        }
    }
    fun selectItem(holder: VideoViewHolder, item: Int) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(Integer.valueOf(item))
                holder.binding!!.frameLayout.setBackgroundColor(Color.WHITE)
                holder.binding!!.chkVideoSelected.setVisibility(View.GONE)
                holder.binding!!.vCheckBackColor.setVisibility(View.GONE)
                //Log.e("selctedItems", selectedItems.toString() + "---" + item);
                if (selectedItems.isEmpty()) {
                    multiSelect = false
                    mActiveActionMode!!.finish()
                }
            } else {
                selectedItems.add(item)
                holder.binding!!.frameLayout.setBackgroundColor(Color.LTGRAY)
                holder.binding!!.chkVideoSelected.setVisibility(View.VISIBLE)
                holder.binding!!.vCheckBackColor.setVisibility(View.VISIBLE)
                Log.e("UnselctedItems", selectedItems.toString() + "---" + item)
            }
            mActiveActionMode!!.setTitle(selectedItems.size.toString() + " Selected")
        }
    }
    private fun handleBinding(item: LocalVideo, holder: VideoViewHolder, itemPosition: Int) {
        holder.binding!!.frameLayout.setOnClickListener({ v -> 
                //  iUtils.ShowToast(context,"clicked :*");
                if (multiSelect) {
                    selectItem(holder, itemPosition)
                } else {
                    showMenuDialog0(item, itemPosition)
                }
                })
    }
    private fun showMenuDialog0(item: LocalVideo, itemPosition: Int) {
        listGroup.clear()
        listGroup.addAll(topHeaderListGroup)
        //list = IntentReaper.makeMime0("*/*", null, IntentReaper.apk_actions, context);
        var listItem = TreeMap<String, MutableList<MItem>>()
        //@     listItem.put("<>", listGroup);
        //        if (!apk.canRead()) {
        //            return;
        //        }
        val file: File = File(item.path)
        try {
            var apkUri: Uri = getUriFromFile(context, file)
            var total: Int = list0.size
            var i: Int = 0
            while (i < total) {
                var wrap: QWrap = list0.`get`(i)
                var mime: String = wrap.mime
                for (entry in wrap.map.entries) {
                    var action: String = entry.key
                    var values = entry.value
                    if (values.isEmpty()) {
                        var menu_name: String = action.replace("android.intent.action.", "")
                        if (total > 1) {
                            menu_name = mime + "::" + menu_name
                        }
                        //menu_name=menu_name + " [" + values.size() + "]";
                        var actionMenu: MutableList<MItem> = ArrayList()
                        //DLog.d(action + " " + values.size());
                        for (info in values) {
                            var serviceIntent: Intent = IntentReaper.intentMaker(action, mime, apkUri)
                            serviceIntent.setPackage(Util.packageName(info))
                            //WARNING
                            //pm.resolveService && pm.resolveActivity not work with
                            //                    Intent serviceIntent = new Intent(action);
                            //                    serviceIntent.setPackage(Util.packageName(info));
                            val icon: Drawable = info.loadIcon(pm)
                            var name: String = ""
                            var packageName: String = ""
                            if (pm.resolveService(serviceIntent, 0) != null) {
                                //packagesSupportingCustomTabs.add(info);
                                name = info.serviceInfo.name
                                packageName = info.serviceInfo.packageName
                                actionMenu.add(MItem("{S}" + info.loadLabel(pm).toString() + "" + name, icon))
                            //=>handle(info, action, mime, file);
                            } else if (pm.resolveActivity(serviceIntent, 0) != null) {
                                name = info.activityInfo.name
                                packageName = info.activityInfo.packageName
                                var appName: String = info.loadLabel(pm).toString()
                                if (packageName.equals("com.ss.android.ugc.trill")) {
                                    appName = "\$\$\$\$\$\$\$\$\$\$\$\$"
                                }
                                var itemName: String = appName + "" + name
                                actionMenu.add(MItem(itemName, icon))
                            }
                            //=>>>>>handle(info, action, mime, file);
                            //MediaType.VIDEO
                            //com.ss.android.ugc.trill @ com.ss.android.ugc.aweme.share.SystemShareActivity
                            if (name.contains("com.ss.android.ugc")) {
                                //type -> video
                                DLog.d("\t\t-----0------" + name + "  " + packageName + "  " + info)
                            }
                        }
                        var root: MItem = MItem(menu_name, defDrawable)
                        listGroup.add(root)
                        listItem.put(root.name, actionMenu)
                    }
                }
                i++
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        }
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var binding: DialogExpandableListBinding = DialogExpandableListBinding.inflate(inflater)
        binding!!.expandableListView.setGroupIndicator(null)
        var adapter: ExpandableListAdapter = ExpandableListAdapter(context, listGroup, listItem)
        binding!!.expandableListView.setAdapter(adapter)
        binding!!.expandableListView.setOnGroupClickListener(lmb2@{ parent, v, groupPosition, id -> 
                var item0: MItem = listGroup.`get`(groupPosition)
                return@lmb2 handleGroupItem(item0, item, groupPosition)
                })
        // false чтобы группы можно было раскрывать/сворачивать
        binding!!.fileName.setText(file.getName())
        binding!!.fileSize.setText(Util.getFileSizeMegaBytes(file))
        binding!!.expandableListView.setOnChildClickListener(lmb3@{ parent, v, groupPosition, childPosition, id -> 
                var item0: MItem = listGroup.`get`(groupPosition)
                var mItem: MItem? = listItem.get(item0.name)?.get(childPosition)
                return@lmb3 handle(item0, item, groupPosition, childPosition)
                })
        // true чтобы показать, что клик был обработан
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.abc_choose_an_action)
        builder.setView(binding!!.getRoot())
        builder.setPositiveButton(android.R.string.cancel, null)
        dialog = builder.create()
        //                builder.setItems(items, (dialog, which) -> {
        //                    wwww = items.get(which);
        //                    if (wwww.contains("Watch")) {
        //
        //                        try {
        //                            if (item.path != null) {
        //                                final String path = item.path;
        //                                final File videoFile = new File(path);
        //                                Uri fileUri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER, videoFile);
        //                                Intent intent = new Intent(Intent.ACTION_VIEW);
        //                                intent.setDataAndType(fileUri, "video/*");
        //                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//DO NOT FORGET THIS EVER
        //                                context.startActivity(intent);
        //
        ////                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        ////                                intent.setDataAndType(Uri.parse(path), "video/mp4");
        ////                                context.startActivity(intent);
        //                            }
        //                        } catch (ActivityNotFoundException e) {
        //                            //iUtils.ShowToast(context, " ");
        //                            if (mainView != null) {
        //                                mainView.makeToaster(R.string.err_something_wrong);
        //                            }
        //                            DLog.handleException(e);
        //                        }
        //                    } else if (wwww.contains("Delete")) {
        //                        new AlertDialog.Builder(context)
        //                                .setTitle("Delete")
        //                                .setMessage(Constants.DEL_CONFIRM)
        //                                .setCancelable(false)
        //                                .setPositiveButton("DELETE", (dialog1, whichButton) -> {
        //                                    //  Log.e("Deleted",intItem.toString());
        //                                    // al_video.remove(intItem);
        //                                    deleteItem(id);
        //                                })
        //                                .setNegativeButton(android.R.string.cancel, null).show();
        //
        //                    } else {
        //                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        //                        File fileWithinMyDir = new File(item.path);
        //
        //                        if (fileWithinMyDir.exists()) {
        //
        //                            try {
        //                                intentShareFile.setType("video/mp4");
        //                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(item.path));
        //
        //                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
        //                                        context.getString(R.string.SharingVideoSubject));
        //                                intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SharingVideoBody));
        //
        //                                context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.sharing_video_title)));
        //                            } catch (ActivityNotFoundException e) {
        //                                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
        //                                if (mainView != null) {
        //                                    mainView.makeToaster(R.string.err_something_wrong_sharing);
        //                                }
        //                                DLog.handleException(e);
        //                            }
        //                        }
        //                    }
        //                });
        dialog!!.show()
    }
    // ResolveInfo info, String action, String mime, File file
    //We handle service and activity
    private fun handle(item0: MItem, item: LocalVideo, absolutGroupPosition: Int, childPosition: Int): Boolean {
        var resolver: UIntent = YoutubeIntent()
        var resolved: Boolean = false
        var position: Int = absolutGroupPosition - topHeaderListGroup.size
        var wrap: QWrap = list0.`get`(0)
        var mime: String = wrap.mime
        var map = wrap.map
        var action: String = "android.intent.action." + item0.name
        var infos: MutableList<ResolveInfo>? = map.get(action)
        if (infos != null) {
            var info: ResolveInfo = infos.`get`(childPosition)
            DLog.d("@" + position + "@" + childPosition + "@" + info.toString())
            //        String url = "/storage/emulated/0/Download/com.Mobilicks.PillIdentifier_v4.apk";
            //        showFileList();
            var packageName: String = Util.packageName(info) ?: ""
            var componentNameString: String = Util.componentName(info) ?: ""
            var file: File = File(item.path)
            //            DLog.d("@" + info.icon);
            //            DLog.d("@" + info.resolvePackageName);
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //                DLog.d("@" + info.isCrossProfileIntentForwarderActivity());
            //            }
            //            if (info.activityInfo != null) {
            //                DLog.d("[]" + info.activityInfo);
            //                DLog.d("[]" + info.activityInfo.name);
            //                DLog.d("[]" + info.activityInfo.targetActivity);
            //                DLog.d("[]" + info.activityInfo.parentActivityName);
            //                DLog.d("[]" + info.activityInfo.permission);
            //                DLog.d("[]" + info.activityInfo.processName);
            //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //                    DLog.d("[]" + info.activityInfo.splitName);
            //                }
            //                DLog.d("[]" + info.activityInfo.taskAffinity);
            //                DLog.d("[]" + info.activityInfo.applicationInfo);
            //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //                    DLog.d("[]" + Arrays.toString(info.activityInfo.attributionTags));
            //                }
            //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            //                    DLog.d("[]" + info.activityInfo.banner);
            //                }
            //
            //                DLog.d("[]" + info.activityInfo.getThemeResource());
            //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            //                    DLog.d("[@b@]" + info.activityInfo.loadBanner(pm));
            //                }
            //            }
            //        if (info.serviceInfo != null) {
            //            DLog.d("[]" + info.serviceInfo);
            //            DLog.d("[]" + info.serviceInfo.name);
            //
            //            DLog.d("[]" + info.serviceInfo.permission);
            //            DLog.d("[]" + info.serviceInfo.processName);
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //                DLog.d("[]" + info.serviceInfo.splitName);
            //            }
            //
            //            DLog.d("[]" + info.serviceInfo.applicationInfo);
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //                DLog.d("[]" + Arrays.toString(info.serviceInfo.attributionTags));
            //            }
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            //                DLog.d("[]" + info.serviceInfo.banner);
            //            }
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            //                DLog.d("[@b@]" + info.serviceInfo.loadBanner(pm));
            //            }
            //
            //        }
            //        DLog.d("@" + info.icon);
            //        DLog.d("@" + info.resolvePackageName);
            //
            //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //            DLog.d("@" + info.isCrossProfileIntentForwarderActivity());
            //        }
            if (file.exists() && file.isDirectory()) {
            }
            if (file.exists() || file.canRead()) {
                Toast.makeText(context, "Attachment Error", Toast.LENGTH_SHORT).show()
                return true
            }
            try {
                if (resolver.isClientPackage(packageName)) {
                    //                <category android:name="android.intent.category.DEFAULT"/>
                    //                        intent1.setAction("com.google.android.youtube.intent.action.UPLOAD");
                    //                        intent1.setType("video/*");
                    //                        MediaScannerConnection.scanFile(this, new String[]{mVidFnam}, null,
                    //                                new MediaScannerConnection.OnScanCompletedListener() {
                    //                                    public void onScanCompleted(String path, Uri uri) {
                    //                                        Log.d(TAG, "onScanCompleted uri " + uri);
                    //
                    //
                    //                                    }
                    //                                });
                    resolver.videoShare(context, item.path ?: "")
                } else {
                    var uri: Uri = getUriFromFile(context, file)
                    if (uri != null) {
                        //                        String type = context.getContentResolver().getType(uri)!!;
                        //                        DLog.d("___E 1___ " + type + " " + mime + " " + packageName);
                        //                        DLog.d("___E 1___ " + uri);
                        //                        DLog.d("___E 1___ " + file);
                        //String url = "/data/app/SmokeTestApp/SmokeTestApp.apk";
                        //            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        //                url = "http://" + url;
                        //            }
                        //            uri = Uri.parse(url);
                        var extraValue: String = context.getString(R.string.app_name)
                        var intent1: Intent = Intent(action, uri)
                        intent1.setPackage(packageName)
                        intent1.putExtra(Intent.EXTRA_TEXT, extraValue)
                        //                    if (1 == 1) {
                        //                        intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"alexvarboffin@gmai.com"});
                        //                    }
                        //Gmail title
                        //DropBox - document name
                        intent1.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                        //GMail Send File
                        if ("com.google.android.gm".equals(packageName)) {
                            intent1.setType(mime)
                            //Not work if false mimitype
                            intent1.putExtra(Intent.EXTRA_STREAM, uri)
                        //com.google.android.apps.docs.shareitem.UploadMenuActivity
                        } else if ("com.google.android.apps.docs".equals(packageName)) {
                            GoogleDocsUtils.send(intent1, uri)
                        } else if ("com.google.android.packageinstaller".equals(packageName)) {
                            DLog.d("-------[apk INSTALLER]")
                            intent1.setDataAndType(uri, mime)
                        } else if (packageName.startsWith("com.dropbox.android")) {
                            DLog.d("@DROP_BOX@")
                            intent1.setDataAndType(uri, mime)
                        } else {
                            DLog.d("------------------------------------")
                            intent1.setDataAndType(uri, mime)
                            intent1.putExtra(Intent.EXTRA_STREAM, uri)
                        }
                        //                            intent.putExtra(Intent.EXTRA_SUBJECT, str);
                        //                            intent.putExtra("android.intent.extra.TITLE", str);
                        //                            intent.putExtra("android.intent.extra.STREAM", uri);
                        //                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //                            intent.setPackage(packageName);
                        //                            com.instagram.android/com.instagram.share.handleractivity.ShareHandlerActivity
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        //resolve
                        //componentName = intent1.resolveActivity(pm);
                        var componentName: ComponentName = ComponentName(packageName, componentNameString)
                        try {
                            if (componentName != null) {
                                DLog.d("COMPONENT NAME==> " + componentName.toString())
                                intent1.setComponent(componentName)
                                context.startActivityForResult(intent1, com.walhalla.ttvloader.Const.COMPONENT_REQUEST_CODE)
                            } else {
                                //FileUriExposedException
                                DLog.d("")
                            }
                        } catch (rr9: Exception) {
                            DLog.d("___E 2___ " + mime + " " + mime + " " + packageName)
                            try {
                                var intent: Intent = Intent(action)
                                intent.setPackage(packageName)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                context.startActivity(intent)
                            } catch (rr: Exception) {
                            }
                        }
                    } else {
                        //                        if (intent2.resolveActivity(getPackageManager()) != null) {
                        //                            startActivity(intent2);
                        //                        } else {
                        //                            DLog.d("empty@");
                        //                        }
                        Toast.makeText(context, "@ Try Latter", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
        if (!resolved) {
            if (null != dialog!!) {
                dialog!!.dismiss()
            }
        }
        return true
    }
    private fun handleGroupItem(mItem1: MItem, item: LocalVideo, position: Int): Boolean {
        var resolved: Boolean = false
        var itemName: String = mItem1.name
        //Toast.makeText(context, "Group Clicked: " + itemName, Toast.LENGTH_SHORT).show();
        if (itemName.equals(context.getString(R.string.action_folder_open), ignoreCase = true)) {
            IntentUtils.openFolder(context, item.path ?: "")
            resolved = true
        } else if (itemName.contains(context.getString(R.string.action_watch))) {
            try {
                if (mainView != null) {
                    mainView.watchVideo(item)
                }
            } catch (e: Exception) {
                //iUtils.ShowToast(context, " ");
                if (mainView != null) {
                    mainView.makeToaster(R.string.err_something_wrong)
                }
                DLog.handleException(e)
            }
            resolved = true
        } else if (itemName.contains(context.getString(R.string.action_delete_video))) {
            AlertDialog.Builder(context).setTitle(context.getString(R.string.action_delete_video)).setMessage(R.string.del_confirm).setCancelable(false).setPositiveButton(context.getString(R.string.action_delete_video), { dialog1, whichButton -> 
                        //  Log.e("Deleted",intItem.toString());
                        // al_video.remove(intItem);
                        deleteItem(position)
                        }).setNegativeButton(android.R.string.cancel, null).show()
            resolved = true
        } else if (itemName.contains(context.getString(R.string.action_share_video))) {
            val packageName = isPackageMenuItem(itemName)
            if (TextUtils.isEmpty(packageName)) {
                Toast.makeText(context, "" + packageName, Toast.LENGTH_SHORT).show()
            } else {
                action_share_video(item)
            }
            resolved = true
        } else {
            resolved = position < topHeaderListGroup.size
        }
        //DLog.d("" + position + " " + resolved);
        if (resolved) {
            if (null != dialog!!) {
                dialog!!.dismiss()
            }
        }
        return resolved
    }
    private fun showMenuDialog(item: LocalVideo, position: Int) {
    }
    //        ListAdapter listAdapter = new ExpandableListAdapter(context, menuItems);
    //
    //        AlertDialog.Builder builder = new AlertDialog.Builder(context);
    //        builder.setTitle(R.string.abc_choose_an_action);
    //        builder.setAdapter(listAdapter, (dialog, which) -> {
    //            return handleItemClick();
    //        });
    //
    ////                builder.setItems(items, (dialog, which) -> {
    ////                    wwww = items.get(which);
    ////                    if (wwww.contains("Watch")) {
    ////
    ////                        try {
    ////                            if (item.path != null) {
    ////                                final String path = item.path;
    ////                                final File videoFile = new File(path);
    ////                                Uri fileUri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER, videoFile);
    ////                                Intent intent = new Intent(Intent.ACTION_VIEW);
    ////                                intent.setDataAndType(fileUri, "video/*");
    ////                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//DO NOT FORGET THIS EVER
    ////                                context.startActivity(intent);
    ////
    //////                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
    //////                                intent.setDataAndType(Uri.parse(path), "video/mp4");
    //////                                context.startActivity(intent);
    ////                            }
    ////                        } catch (ActivityNotFoundException e) {
    ////                            //iUtils.ShowToast(context, " ");
    ////                            if (mainView != null) {
    ////                                mainView.makeToaster(R.string.err_something_wrong);
    ////                            }
    ////                            DLog.handleException(e);
    ////                        }
    ////                    } else if (wwww.contains("Delete")) {
    ////                        new AlertDialog.Builder(context)
    ////                                .setTitle("Delete")
    ////                                .setMessage(Constants.DEL_CONFIRM)
    ////                                .setCancelable(false)
    ////                                .setPositiveButton("DELETE", (dialog1, whichButton) -> {
    ////                                    //  Log.e("Deleted",intItem.toString());
    ////                                    // al_video.remove(intItem);
    ////                                    deleteItem(id);
    ////                                })
    ////                                .setNegativeButton(android.R.string.cancel, null).show();
    ////
    ////                    } else {
    ////                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
    ////                        File fileWithinMyDir = new File(item.path);
    ////
    ////                        if (fileWithinMyDir.exists()) {
    ////
    ////                            try {
    ////                                intentShareFile.setType("video/mp4");
    ////                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(item.path));
    ////
    ////                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
    ////                                        context.getString(R.string.SharingVideoSubject));
    ////                                intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SharingVideoBody));
    ////
    ////                                context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.sharing_video_title)));
    ////                            } catch (ActivityNotFoundException e) {
    ////                                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
    ////                                if (mainView != null) {
    ////                                    mainView.makeToaster(R.string.err_something_wrong_sharing);
    ////                                }
    ////                                DLog.handleException(e);
    ////                            }
    ////                        }
    ////                    }
    ////                });
    //        builder.show();
    private fun isPackageMenuItem(name: String): String? {
        //        for (AppModel model : tmp) {
        //            if (model.appPackageName.equals(name)) {
        //                return model.appPackageName;
        //            }
        //        }
        //        return null;
        return null
    }
    fun deleteItem(position: Int) {
        var mm: Any = items.`get`(position)
        if (mm is LocalVideo) {
            deleteItem((mm as LocalVideo), position)
        }
    }
    fun deleteItem(o: LocalVideo, position: Int) {
        var video: String = o.path!!
        // context.getContentResolver().delete(Uri.parse(video), null, null);
        //   Boolean del =   new File(video).getAbsoluteFile().delete();
        //  Log.e("Deleted", new File(Uri.parse(video).getPath()).getAbsoluteFile().toString());
        context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "=?", arrayOf(video))
        items.remove(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, items.size)
        this.notifyDataSetChanged()
    }
    // v.ViewHolder.setVisibility(View.GONE);
    override fun getItemCount(): Int {
        return items.size
    }
    fun secToTime(sec: Int): String? {
        return String.format(Locale.getDefault(), "%d:%d", TimeUnit.MILLISECONDS.toMinutes(sec.toLong()), TimeUnit.MILLISECONDS.toSeconds(sec.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sec.toLong())))
    }
    fun action_share_video(localVideo: LocalVideo) {
        if (mainView != null) {
            mainView.action_share_video(localVideo)
        }
    }
    fun swapAdapter(alLocalVideo0: MutableList<Any>) {
        this.items.clear()
        this.items.addAll(alLocalVideo0)
        this.notifyDataSetChanged()
    }
    fun swapAdapter0(alLocalVideo0: MutableList<LocalVideo>) {
        this.items.clear()
        this.items.addAll(alLocalVideo0)
        this.notifyDataSetChanged()
    }
    class EmptyViewHolder : RecyclerView.ViewHolder {
        //private final TextView response;
        private lateinit var error_msg: TextView
        constructor(v2: View) : super(v2) {
            //response = v2.findViewById(R.id.response);
            error_msg = v2.findViewById(R.id.tv_error_msg)
        }
        fun bind(o: Any) {
            var error: EmptyViewModel = (o as EmptyViewModel)
            if (error != null) {
                error_msg.setText(error.error)
            }
        }
    }
    companion object {
        const val VIEW_TYPE_EMPTY = 1
    }
}
