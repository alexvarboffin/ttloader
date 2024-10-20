package com.walhalla.ttvloader.ui.gallery;

import static com.walhalla.intentresolver.utils.UriUtils.getUriFromFile;
import static com.walhalla.ttvloader.Const.COMPONENT_REQUEST_CODE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.adapters.EmptyViewModel;

import com.walhalla.adapters.ExpandableListAdapter;
import com.walhalla.intentresolver.GoogleDocsUtils;
import com.walhalla.ttvloader.GlideApp;
import com.walhalla.ttvloader.databinding.DialogExpandableListBinding;
import com.walhalla.ttvloader.databinding.VideoItemBinding;
import com.walhalla.intentresolver.UIntent;
import com.walhalla.intentresolver.YoutubeIntent;
import com.walhalla.ttvloader.mvp.MainView;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.models.LocalVideo;

import com.walhalla.ttvloader.ui.MItem;
import com.walhalla.ttvloader.utils.IntentUtils;
import com.walhalla.ttvloader.viewholder.RecyclerViewSimpleTextViewHolder;
import com.walhalla.ttvloader.viewholder.VideoViewHolder;
import com.walhalla.ui.DLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class VideoStorageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final IntentReaper intentReaper;
    private final List<QWrap> list0;
    private final PackageManager pm;
    private final Drawable defDrawable;

    Map<Integer, Drawable> drawableMap = new HashMap<>();

    List<MItem> topHeaderListGroup = new ArrayList<>();
    List<MItem> listGroup = new ArrayList<>();

    //private final List<AppModel> tmp;

    public final int VIEW_TYPE_VIDEO = 0;
    public static final int VIEW_TYPE_EMPTY = 1;

    public final MainView mainView;
    private List<Object> items = new ArrayList<>();

    private final Activity context;
    public ActionMode mActiveActionMode;
    public boolean multiSelect = false;
    public ArrayList<Integer> selectedItems = new ArrayList<Integer>();

    public ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActiveActionMode = mode;

            multiSelect = true;
            mode.getMenuInflater().inflate(R.menu.delete, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

            new AlertDialog.Builder(context)
                    .setTitle("Delete " + selectedItems.size() + " video?")
                    .setMessage(R.string.del_confirm)
                    .setCancelable(false)
                    .setPositiveButton("DELETE", (dialog, whichButton) -> {
                        Collections.sort(selectedItems, Collections.reverseOrder());
                        for (Integer intItem : selectedItems) {
                            //  Log.e("Deleted",intItem.toString());
                            // al_video.remove(intItem);
                            int pos = Integer.parseInt(intItem.toString());
                            Object mm = items.get(pos);
                            if (mm instanceof LocalVideo) {
                                deleteItem((LocalVideo) mm, pos);
                            }
                        }
                        mode.finish();
                    })
                    .setNegativeButton("CANCEL", null).show();

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };
    private AlertDialog dialog;


    public VideoStorageAdapter(Activity context, ArrayList<Object> data, MainView mainView) {
        this.items = data;
        this.context = context;
        this.mainView = mainView;
        this.intentReaper = new IntentReaper(this.context);
        this.pm = context.getPackageManager();
//        if (data != null) {
//            for (LocalVideo localVideo : data) {
//                DLog.d("@@@ " + localVideo.toString());
//            }
//        }

        list0 = IntentReaper.makeMimeActivityList("video/*", null, IntentReaper.videoActions, context);

        //list0 = IntentReaper.makeMime0("*/*", null, IntentReaper.videoActions, context);


        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_action_watch, R.string.action_watch));
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_baseline_delete_forever_24, R.string.action_delete_video));
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_baseline_share_24, R.string.action_share_video));
        topHeaderListGroup.add(addQMenu(context, R.drawable.ic_baseline_folder_open_24, R.string.action_folder_open));


//        tmp = ConfigUtils.makeShareList();
//        for (AppModel model : tmp) {
//            if (PackageUtils.isPackageInstalledForLaunch(context, model.appPackageName)) {
//                menuItems.add(new MItem(R.drawable.ic_baseline_folder_open_24, model.appName)));
//            }
//        }

        //@@@
        defDrawable = ContextCompat.getDrawable(context, R.drawable.ic_action_android);
    }

    public MItem addQMenu(Context context, @DrawableRes int icActionWatch, @StringRes int actionWatch) {
        Drawable drawable = ContextCompat.getDrawable(context, icActionWatch);
        String name0 = context.getString(actionWatch);
        return new MItem(drawable, name0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_VIDEO) {
            final @NonNull VideoItemBinding binding = VideoItemBinding.inflate(inflater, parent, false);
            viewHolder = new VideoViewHolder(binding);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            View v2 = inflater.inflate(R.layout.row_empty, parent, false);
            viewHolder = new EmptyViewHolder(v2);

        } else {
            View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new RecyclerViewSimpleTextViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof LocalVideo) {
            return VIEW_TYPE_VIDEO;
        } else if (items.get(position) instanceof EmptyViewModel) {
            return VIEW_TYPE_EMPTY;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_VIDEO) {
            LocalVideo item = (LocalVideo) items.get(position);
            VideoViewHolder vh1 = (VideoViewHolder) viewHolder;
            if (DLog.nonNull(item.thumb)) {
                GlideApp.with(context)
                        .load(item.thumb)
                        //.skipMemoryCache(false)
                        .into(vh1.binding.mediaImgBack);
            }
            vh1.bind(item, position, this);
            handleBinding(item, vh1, position);
            vh1.binding.frameLayout.setOnLongClickListener(view -> {
                ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                selectItem(vh1, position);
                return true;
            });

        } else if (viewType == VIEW_TYPE_EMPTY) {
            EmptyViewHolder vh2 = (EmptyViewHolder) viewHolder;
            vh2.bind(items.get(position));

        } else {
            RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
            vh.bind((String) items.get(position));
        }
    }

    void selectItem(VideoViewHolder holder, int item) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(Integer.valueOf(item));
                holder.binding.frameLayout.setBackgroundColor(Color.WHITE);
                holder.binding.chkVideoSelected.setVisibility(View.GONE);
                holder.binding.vCheckBackColor.setVisibility(View.GONE);

                //Log.e("selctedItems", selectedItems.toString() + "---" + item);

                if (selectedItems.isEmpty()) {
                    multiSelect = false;
                    mActiveActionMode.finish();
                }
            } else {
                selectedItems.add(item);
                holder.binding.frameLayout.setBackgroundColor(Color.LTGRAY);
                holder.binding.chkVideoSelected.setVisibility(View.VISIBLE);
                holder.binding.vCheckBackColor.setVisibility(View.VISIBLE);

                Log.e("UnselctedItems", selectedItems.toString() + "---" + item);

            }
            mActiveActionMode.setTitle(selectedItems.size() + " Selected");
        }
    }

    private void handleBinding(LocalVideo item, VideoViewHolder holder, int itemPosition) {
        holder.binding.frameLayout.setOnClickListener(v -> {
            //  iUtils.ShowToast(context,"clicked :*");
            if (multiSelect) {
                selectItem(holder, itemPosition);
            } else {
                showMenuDialog0(item, itemPosition);
            }
        });
    }


    private void showMenuDialog0(LocalVideo item, int itemPosition) {
        listGroup.clear();
        listGroup.addAll(topHeaderListGroup);

        //list = IntentReaper.makeMime0("*/*", null, IntentReaper.apk_actions, context);
        Map<String, List<MItem>> listItem = new TreeMap<>();
        //@@@     listItem.put("<>", listGroup);
        //        if (!apk.canRead()) {
//            return;
//        }

        final File file = new File(item.path);
        try {

            Uri apkUri = getUriFromFile(context, file);
            int total = list0.size();
            for (int i = 0; i < total; i++) {
                QWrap wrap = list0.get(i);
                String mime = wrap.mime;
                for (Map.Entry<String, List<ResolveInfo>> entry : wrap.map.entrySet()) {
                    String action = entry.getKey();
                    List<ResolveInfo> values = entry.getValue();
                    if (!values.isEmpty()) {
                        String menu_name = action.replace("android.intent.action.", "");
                        if (total > 1) {
                            menu_name = mime + "::" + menu_name;
                        }
                        //menu_name=menu_name + " [" + values.size() + "]";

                        List<MItem> actionMenu = new ArrayList<>();
                        //DLog.d(action + " " + values.size());

                        for (ResolveInfo info : values) {

                            Intent serviceIntent = IntentReaper.intentMaker(action, mime, apkUri);
                            serviceIntent.setPackage(Util.packageName(info));

                            //WARNING
                            //pm.resolveService && pm.resolveActivity not work with
                            //                    Intent serviceIntent = new Intent(action);
                            //                    serviceIntent.setPackage(Util.packageName(info));

                            final Drawable icon = info.loadIcon(pm);
                            String name = "";
                            String packageName = "";

                            if (pm.resolveService(serviceIntent, 0) != null) {
                                //packagesSupportingCustomTabs.add(info);
                                name = info.serviceInfo.name;
                                packageName = info.serviceInfo.packageName;

                                actionMenu.add(new MItem("{S}" + info.loadLabel(pm).toString() + "@@" + name, icon));
                                //@@=>handle(info, action, mime, file);

                            } else if (pm.resolveActivity(serviceIntent, 0) != null) {
                                name = info.activityInfo.name;
                                packageName = info.activityInfo.packageName;


                                String appName = info.loadLabel(pm).toString();
                                if (packageName.equals("com.ss.android.ugc.trill")) {
                                    appName = "$$$$$$$$$$$$";
                                }
                                String itemName = appName + "@@" + name;
                                actionMenu.add(new MItem(itemName, icon));
                                //@@=>>>>>handle(info, action, mime, file);
                            }

                            //MediaType.VIDEO

                            //com.ss.android.ugc.trill @ com.ss.android.ugc.aweme.share.SystemShareActivity

                            if (name.contains("com.ss.android.ugc")) {
                                //type -> video
                                DLog.d("\t\t-----0------" + name + " @@ " + packageName + " @@ " + info);
                            }

                        }

                        MItem root = new MItem(menu_name, defDrawable);
                        listGroup.add(root);
                        listItem.put(root.name, actionMenu);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            DLog.handleException(e);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        @NonNull DialogExpandableListBinding binding = DialogExpandableListBinding.inflate(inflater);
        binding.expandableListView.setGroupIndicator(null);
        ExpandableListAdapter adapter = new ExpandableListAdapter(context, listGroup, listItem);
        binding.expandableListView.setAdapter(adapter);
        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            MItem item0 = listGroup.get(groupPosition);
            return handleGroupItem(item0, item, groupPosition); // false чтобы группы можно было раскрывать/сворачивать
        });

        binding.fileName.setText(file.getName());
        binding.fileSize.setText(Util.getFileSizeMegaBytes(file));

        binding.expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MItem item0 = listGroup.get(groupPosition);
            MItem mItem = listItem.get(item0.name).get(childPosition);
            return handle(item0, item, groupPosition, childPosition); // true чтобы показать, что клик был обработан
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.abc_choose_an_action);
        builder.setView(binding.getRoot());
        builder.setPositiveButton(android.R.string.cancel, null);

        dialog = builder.create();


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
        dialog.show();
    }

    //@@ ResolveInfo info, String action, String mime, File file


    //We handle service and activity

    private boolean handle(MItem item0, LocalVideo item, int absolutGroupPosition, int childPosition) {

        UIntent resolver = new YoutubeIntent();

        boolean resolved = false;

        int position = absolutGroupPosition - topHeaderListGroup.size();
        QWrap wrap = list0.get(0);
        String mime = wrap.mime;
        Map<String, List<ResolveInfo>> map = wrap.map;
        String action = "android.intent.action." + item0.name;
        List<ResolveInfo> infos = map.get(action);
        if (infos != null) {
            ResolveInfo info = infos.get(childPosition);
            DLog.d("@@@" + position + "@" + childPosition + "@@@@@" + info.toString());
//        String url = "/storage/emulated/0/Download/com.Mobilicks.PillIdentifier_v4.apk";
//        showFileList();

            String packageName = Util.packageName(info);
            String componentNameString = Util.componentName(info);

            File file = new File(item.path);

//            DLog.d("@@@" + info.icon);
//            DLog.d("@@@" + info.resolvePackageName);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                DLog.d("@@@" + info.isCrossProfileIntentForwarderActivity());
//            }
//            if (info.activityInfo != null) {
//                DLog.d("[@@]" + info.activityInfo);
//                DLog.d("[@@]" + info.activityInfo.name);
//                DLog.d("[@@]" + info.activityInfo.targetActivity);
//                DLog.d("[@@]" + info.activityInfo.parentActivityName);
//                DLog.d("[@@]" + info.activityInfo.permission);
//                DLog.d("[@@]" + info.activityInfo.processName);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    DLog.d("[@@]" + info.activityInfo.splitName);
//                }
//                DLog.d("[@@]" + info.activityInfo.taskAffinity);
//                DLog.d("[@@]" + info.activityInfo.applicationInfo);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    DLog.d("[@@]" + Arrays.toString(info.activityInfo.attributionTags));
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//                    DLog.d("[@@]" + info.activityInfo.banner);
//                }
//
//                DLog.d("[@@]" + info.activityInfo.getThemeResource());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//                    DLog.d("[@b@]" + info.activityInfo.loadBanner(pm));
//                }
//            }


//        if (info.serviceInfo != null) {
//            DLog.d("[@@]" + info.serviceInfo);
//            DLog.d("[@@]" + info.serviceInfo.name);
//
//            DLog.d("[@@]" + info.serviceInfo.permission);
//            DLog.d("[@@]" + info.serviceInfo.processName);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                DLog.d("[@@]" + info.serviceInfo.splitName);
//            }
//
//            DLog.d("[@@]" + info.serviceInfo.applicationInfo);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                DLog.d("[@@]" + Arrays.toString(info.serviceInfo.attributionTags));
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//                DLog.d("[@@]" + info.serviceInfo.banner);
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//                DLog.d("[@b@]" + info.serviceInfo.loadBanner(pm));
//            }
//
//        }
//        DLog.d("@@@" + info.icon);
//        DLog.d("@@@" + info.resolvePackageName);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            DLog.d("@@@" + info.isCrossProfileIntentForwarderActivity());
//        }

            if (file.exists() && !file.isDirectory()) {
            }

            if (!file.exists() || !file.canRead()) {
                Toast.makeText(context, "Attachment Error", Toast.LENGTH_SHORT).show();
                return true;
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

                    resolver.videoShare(context, item.path);

                } else {
                    Uri uri = getUriFromFile(context, file);
                    if (uri != null) {
//                        String type = context.getContentResolver().getType(uri);
//                        DLog.d("___E 1___ " + type + " " + mime + " " + packageName);
//                        DLog.d("___E 1___ " + uri);
//                        DLog.d("___E 1___ " + file);


//String url = "/data/app/SmokeTestApp/SmokeTestApp.apk";

//            if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                url = "http://" + url;
//            }
//            uri = Uri.parse(url);

                        String extraValue = context.getString(R.string.app_name);
                        Intent intent1 = new Intent(action, uri);
                        intent1.setPackage(packageName);
                        intent1.putExtra(Intent.EXTRA_TEXT, extraValue);
//                    if (1 == 1) {
//                        intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"alexvarboffin@gmai.com"});
//                    }
                        //Gmail title
                        //DropBox - document name
                        intent1.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));

                        //GMail Send File
                        if ("com.google.android.gm".equals(packageName)) {
                            intent1.setType(mime);//Not work if false mimitype
                            intent1.putExtra(Intent.EXTRA_STREAM, uri);
                        }


                        //com.google.android.apps.docs.shareitem.UploadMenuActivity
                        else if ("com.google.android.apps.docs".equals(packageName)) {
                            GoogleDocsUtils.send(intent1, uri);
                        } else if ("com.google.android.packageinstaller".equals(packageName)) {
                            DLog.d("-------[apk INSTALLER]");
                            intent1.setDataAndType(uri, mime);
                        } else if (packageName.startsWith("com.dropbox.android")) {
                            DLog.d("@DROP_BOX@");
                            intent1.setDataAndType(uri, mime);
                        } else {
                            DLog.d("------------------------------------");
                            intent1.setDataAndType(uri, mime);
                            intent1.putExtra(Intent.EXTRA_STREAM, uri);

//                            intent.putExtra(Intent.EXTRA_SUBJECT, str);
//                            intent.putExtra("android.intent.extra.TITLE", str);
//                            intent.putExtra("android.intent.extra.STREAM", uri);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//                            intent.setPackage(packageName);
//                            com.instagram.android/com.instagram.share.handleractivity.ShareHandlerActivity

                        }
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);


                        //resolve
                        //componentName = intent1.resolveActivity(pm);
                        ComponentName componentName = new ComponentName(packageName, componentNameString);
                        try {
                            if (componentName != null) {
                                DLog.d("COMPONENT NAME==> " + componentName.toString());
                                intent1.setComponent(componentName);
                                context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);//FileUriExposedException
                            } else {
                                DLog.d("@@@@@@");
                            }
                        } catch (ActivityNotFoundException rr9) {

                            DLog.d("___E 2___ " + mime + " " + mime + " " + packageName);
                            try {
                                Intent intent = new Intent(action);
                                intent.setPackage(packageName);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                context.startActivity(intent);
                            } catch (ActivityNotFoundException rr) {

                            }
//                        if (intent2.resolveActivity(getPackageManager()) != null) {
//                            startActivity(intent2);
//                        } else {
//                            DLog.d("@@empty@@@");
//                        }
                        }
                    } else {
                        Toast.makeText(context, "@ Try Latter", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (StringIndexOutOfBoundsException e) {
                DLog.handleException(e);
            }
        }
        if (!resolved) {
            if (null != dialog) {
                dialog.dismiss();
            }
        }
        return true;
    }

    private boolean handleGroupItem(final MItem mItem1, LocalVideo item, int position) {

        boolean resolved = false;
        String itemName = mItem1.name;
        //Toast.makeText(context, "Group Clicked: " + itemName, Toast.LENGTH_SHORT).show();

        if (itemName.equalsIgnoreCase(context.getString(R.string.action_folder_open))) {
            IntentUtils.openFolder(context, item.path);
            resolved = true;
        } else if (itemName.contains(context.getString(R.string.action_watch))) {
            try {
                if (mainView != null) {
                    mainView.watchVideo(item);
                }
            } catch (ActivityNotFoundException e) {
                //iUtils.ShowToast(context, " ");
                if (mainView != null) {
                    mainView.makeToaster(R.string.err_something_wrong);
                }
                DLog.handleException(e);
            }
            resolved = true;
        } else if (itemName.contains(context.getString(R.string.action_delete_video))) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.action_delete_video))
                    .setMessage(R.string.del_confirm)
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.action_delete_video),
                            (dialog1, whichButton) -> {
                                //  Log.e("Deleted",intItem.toString());
                                // al_video.remove(intItem);
                                deleteItem(position);
                            })
                    .setNegativeButton(android.R.string.cancel, null).show();
            resolved = true;
        } else if (itemName.contains(context.getString(R.string.action_share_video))) {
            final String packageName = isPackageMenuItem(itemName);
            if (!TextUtils.isEmpty(packageName)) {
                Toast.makeText(context, "" + packageName, Toast.LENGTH_SHORT).show();
            } else {
                action_share_video(item);
            }
            resolved = true;
        } else {
            resolved = position < topHeaderListGroup.size();
            //DLog.d("@@@@" + position + " " + resolved);
        }
        if (resolved) {
            if (null != dialog) {
                dialog.dismiss();
            }
        }
        return resolved;
    }

    private void showMenuDialog(LocalVideo item, int position) {
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
    }

    private String isPackageMenuItem(String name) {
//        for (AppModel model : tmp) {
//            if (model.appPackageName.equals(name)) {
//                return model.appPackageName;
//            }
//        }
//        return null;
        return null;
    }

    public void deleteItem(int position) {
        Object mm = items.get(position);
        if (mm instanceof LocalVideo) {
            deleteItem((LocalVideo) mm, position);
        }
    }

    public void deleteItem(LocalVideo o, int position) {
        String video = o.path;
        // context.getContentResolver().delete(Uri.parse(video), null, null);

        //   Boolean del =   new File(video).getAbsoluteFile().delete();

        //  Log.e("Deleted", new File(Uri.parse(video).getPath()).getAbsoluteFile().toString());
        context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA + "=?", new String[]{video});

        items.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, items.size());
        this.notifyDataSetChanged();

        // v.ViewHolder.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public String secToTime(int sec) {
        return String.format(Locale.getDefault(), "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(sec),
                TimeUnit.MILLISECONDS.toSeconds(sec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sec))
        );
    }

    public void action_share_video(LocalVideo localVideo) {
        if (mainView != null) {
            mainView.action_share_video(localVideo);
        }
    }

    public void swapAdapter(List<Object> alLocalVideo0) {
        this.items.clear();
        this.items.addAll(alLocalVideo0);
        this.notifyDataSetChanged();
    }

    public void swapAdapter0(List<LocalVideo> alLocalVideo0) {
        this.items.clear();
        this.items.addAll(alLocalVideo0);
        this.notifyDataSetChanged();
    }


    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        //private final TextView response;
        private final TextView error_msg;

        public EmptyViewHolder(View v2) {
            super(v2);
            //response = v2.findViewById(R.id.response);
            error_msg = v2.findViewById(R.id.tv_error_msg);
        }

        public void bind(Object o) {
            EmptyViewModel error = (EmptyViewModel) o;
            if (error != null) {
                error_msg.setText(error.error);
            }
        }
    }


}