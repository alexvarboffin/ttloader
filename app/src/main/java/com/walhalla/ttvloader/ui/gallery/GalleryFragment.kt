package com.walhalla.ttvloader.ui.gallery

import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.walhalla.adapters.CustomSpinnerAdapter
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ttvloader.activity.main.MainActivity
import com.walhalla.ttvloader.databinding.FragmentGalleryBinding
import com.walhalla.ttvloader.mvp.MainView
import com.walhalla.ttvloader.R
import com.walhalla.adapters.EmptyViewModel
import com.walhalla.ttloader.core.GalleryCatalog
import com.walhalla.ttvloader.models.LocalVideo
import com.walhalla.ui.DLog
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map

class GalleryFragment : Fragment(), GalleryPresenter.View {
    var folderNames: MutableList<String> = ArrayList()
    var videosByFolder: LinkedHashMap<String, MutableList<LocalVideo>> = LinkedHashMap()
    private var _videoStorageAdapter: VideoStorageAdapter? = null
    var al_Local_video0: ArrayList<Any> = ArrayList()
    private var mainView: MainView? = null
    private var binding: FragmentGalleryBinding? = null
    private var spinnerAdapter: CustomSpinnerAdapter? = null
    private val mmm: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: android.view.View, position: Int, id: Long) {
                var selectedFolder: String = folderNames.`get`(position)
                if (KEY_ALL_FILES.equals(selectedFolder)) {
                    _videoStorageAdapter!!.swapAdapter(al_Local_video0)
                } else {
                    var selectedVideos: MutableList<LocalVideo> = videosByFolder.get(selectedFolder) ?: ArrayList()
                    _videoStorageAdapter!!.swapAdapter0(selectedVideos)
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
    // Handle the case where no folder is selected
    private var presenter: GalleryPresenter? = null
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState!!)
        var launcher29: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), { map -> 
                var sb: StringBuilder = StringBuilder()
                var isGranted: Boolean = false
                for (entry in map.entries) {
                    var tmp: Boolean = entry.value
                    if (tmp) {
                        isGranted = true
                    }
                    sb.append("").append(entry.key).append(" ").append(entry.value).append("\n")
                }
                if (isGranted) {
                    // Permission is granted. Continue with updating the UI
                    // Permission is granted. Continue with updating the UI
                    updateGUI(getActivity()!!, true)
                } else {
                    //Toast.makeText(getContext()!!, "GRANTED", Toast.LENGTH_LONG).show();
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    //mainView.showNoStoragePermissionSnackbar();
                    showNoStoragePermissionSnackbar()
                    Toast.makeText(getContext()!!, "NOT GRANTED\n" + sb.toString(), Toast.LENGTH_LONG).show()
                }
                })
        var storageActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), { o -> 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //Android is 11 (R) or above
                    if (Environment.isExternalStorageManager()) {
                        //Manage External Storage Permissions Granted
                        DLog.d("onActivityResult: Manage External Storage Permissions Granted")
                    } else {
                        Toast.makeText(getActivity()!!, "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
                    }
                } else {
                }
                })
        //Below android 11
        var handler: Handler = Handler()
        presenter = GalleryPresenter(this, handler, (getActivity()!! as AppCompatActivity), launcher29, storageActivityResultLauncher)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGalleryBinding.inflate(inflater, container!!, false)
        var manager: GridLayoutManager = GridLayoutManager(getContext()!!, 3)
        manager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (_videoStorageAdapter!!.getItemViewType(position) === VideoStorageAdapter.VIEW_TYPE_EMPTY) {
                            return 3
                        }
                        return 1
                    }
                })
        binding!!.recyclerView.setLayoutManager(manager)
        if (al_Local_video0.isEmpty()) {
            al_Local_video0.add(EmptyViewModel(getString(R.string.empty_data)))
        }
        _videoStorageAdapter = VideoStorageAdapter(getActivity()!!, al_Local_video0, mainView!!)
        binding!!.recyclerView.setAdapter(null)
        binding!!.recyclerView.setAdapter(_videoStorageAdapter)
        _videoStorageAdapter!!.notifyDataSetChanged()
        //        m1 ma = ((m1) getActivity()!!);
        //        if (ma != null && !ma.isNeedGrantPermission()) {
        //            fn_video(getActivity()!!.getContentResolver(), getActivity()!!, true);
        //        }
        //getAllMediaFilesOnDevice(getContext()!!);
        //checkAndRequestPermissions();
        //        binding!!.test.setOnClickListener(v->{
        //
        ////                    new RedditIntent().shareMp4Selector(getContext()!!,
        ////                            new File("/storage/emulated/0/screen-recording-1710055824379.mp4"));
        //        });
        spinnerAdapter = CustomSpinnerAdapter(getActivity()!!, R.layout.simple_sp_item, folderNames)
        spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.folderSpinner.setAdapter(spinnerAdapter!!)
        binding!!.folderSpinner.setOnItemSelectedListener(mmm)
        if (BuildConfig.DEBUG) {
            binding!!.getRoot().setOnClickListener({ v -> 
                        showPermission33SnackBar()
                        })
        }
        return binding!!.getRoot()
    }
    override fun onResume() {
        super.onResume()
        presenter!!.onResume()
    }
    //    public List<File> getAllMediaFilesOnDevice(Context context) {
    //        al_Local_video = new ArrayList<>();
    //        List<File> files = new ArrayList<>();
    //        try {
    //
    //            final String[] columns = {MediaStore.Images.Media.DATA,
    //                    MediaStore.Images.Media.DATE_ADDED,
    //                    MediaStore.Images.Media.BUCKET_ID,
    //                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    //
    //            MergeCursor cursor = new MergeCursor(new Cursor[]{context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null),
    //                    context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, null),
    //                    context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null, null, null),
    //                    context.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, columns, null, null, null)
    //            });
    //            cursor.moveToFirst();
    //            files.clear();
    //            while (!cursor.isAfterLast()) {
    //                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
    //                int lastPoint = path.lastIndexOf(".");
    //                path = path.substring(0, lastPoint) + path.substring(lastPoint).lowercase();
    //                files.add(new File(path));
    //
    //                LocalVideo obj_model = new LocalVideo();
    //                obj_model.selected = false;
    //                obj_model.path = path;
    //                al_Local_video.add(obj_model);
    //                cursor.moveToNext();
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //        }
    //        _videoStorageAdapter = new VideoStorageAdapter(getContext()!!, al_Local_video, mainView);
    //        recyclerView.setAdapter(null);
    //        recyclerView.setAdapter(_videoStorageAdapter);
    //        _videoStorageAdapter!!.notifyDataSetChanged();
    //        return files;
    //    }
    private fun updateAdapter(context: Context) {
        //spinnerAdapter.swapData(folderNames);
        spinnerAdapter = CustomSpinnerAdapter(context, R.layout.simple_sp_item, folderNames)
        //spinnerAdapter.setDropDownViewResource(R.layout.simple_sp_item);
        binding!!.folderSpinner.setAdapter(spinnerAdapter!!)
        binding!!.folderSpinner.setPrompt(getString(R.string.app_name))
        binding!!.folderSpinner.setOnItemSelectedListener(mmm)
    }
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            var ma: MainActivity = (getActivity()!! as MainActivity)
            if (ma != null) {
            }
        }
    }
    //presenter.isNeedGrantPermission();
    //getAllMediaFilesOnDevice(getContext()!!);
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainView) {
            mainView = (context as MainView)
        }
    }
    override fun updateGUI() {
        updateGUI(getActivity()!!, true)
    }
    fun updateGUI(activity: Activity, b: Boolean) {
        var contentResolver: ContentResolver = activity.getContentResolver()
        al_Local_video0 = ArrayList()
        var folderNamesTmp: ArrayList<String> = ArrayList()
        folderNames = ArrayList()
        var int_position: Int = 0
        //FileProvider.getUriForFile(context, authority, file);
        var selectionArguments: Array<String> = arrayOf("%" + Environment.DIRECTORY_MOVIES)
        //Q.DOWNLOAD_DIRECTORY
        var sortOrder: String = MediaStore.Video.Media.DATE_TAKEN + " DESC"
        var projection: Array<String> = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Thumbnails.DATA)
        //                MediaStore.Images.Media._ID,
        //                MediaStore.Images.Media.BUCKET_ID,
        //                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        //                //MediaStore.Images.Media.DATA,
        //                MediaStore.Video.Media.DATE_TAKEN,
        //                MediaStore.Video.Media.SIZE,
        //                MediaStore.Video.Media.DURATION
        //@ MediaStore.Video.Media.DATA,
        var mCursor: Cursor? = null
        try {
            mCursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder)
        } catch (e: Exception) {
            //                    MediaStore.Video.Media.DATA + " LIKE ?", //condition
            //                    selectionArguments, //selectionArguments,
            DLog.d("В поиск добавлено поле которого не существует")
        }
        try {
            //Primary external storage directory
            if (null == mCursor) {
                DLog.d("CURSOR NULL")
            } else if (mCursor.getCount() < 1) {
                DLog.d("CURSOR EMPTY")
            } else {
                /*
                                 * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
                                 * an error. You may want to offer the user the option to insert a new row, or re-type the
                                 * search term.
                                 */
                //                StringBuilder sb = new StringBuilder();
                //                int mm = mCursor.getColumnCount();
                //                for (int i = 0; i < mm; i++) {
                //                    String name = mCursor.getColumnName(i);
                //                    int index = mCursor.getColumnIndexOrThrow(name);
                //                    //if (index > -1) {
                //                    sb.append(name).append(" ").append(index);
                ////                    } else {
                ////                        DLog.d("NOT_FOUND --> " + name);
                ////                    }
                //                    sb.append("\t");
                //                }
                //                DLog.d(sb.toString());
                var _id: Int = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                var column_index_data: Int = mCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                var column_index_folder_name: Int = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                var thum: Int = mCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
                var duration: Int = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                var i: Int = 0
                videosByFolder = LinkedHashMap()
                var sortedByDate = LinkedHashMap<String, LocalVideo>()
                while (mCursor.moveToNext()) {
                    var absolutePathOfImage: String? = mCursor.getString(column_index_data)
                    var folderName: String = mCursor.getString(column_index_folder_name) ?: ""
                    //DLog.d(absolutePathOfImage);
                    //DLog.d("@"+mCursor.getString(column_index_folder_name));
                    //                    DLog.d(mCursor.getString(column_id));
                    //                    DLog.d(mCursor.getString(thum));
                    //                    DLog.d(mCursor.getString(duration));
                    var video: LocalVideo = LocalVideo()
                    video.selected = false
                    video.path = absolutePathOfImage
                    video.thumb = mCursor.getString(thum)
                    video.duration = mCursor.getInt(duration)
                    video.id = i
                    //al_Local_video0.add(video);
                    sortedByDate.put(video.path ?: "", video)
                    //
                    // Сортировка по папкам
                    if (!videosByFolder.containsKey(folderName)) {
                        videosByFolder.put(folderName, ArrayList())
                        // Создаем новую папку если она не существует
                        folderNamesTmp.add(folderName)
                    }
                    videosByFolder.get(folderName)?.add(video)
                    //
                    i = i + 1
                }
                for (entry in sortedByDate.entries) {
                    al_Local_video0.add(entry.value)
                }
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        } finally {
            if (mCursor != null) {
                mCursor.close()
            }
        }
        if (al_Local_video0.isEmpty()) {
            al_Local_video0.add(EmptyViewModel(getString(R.string.empty_data)))
        }
        _videoStorageAdapter!!.swapAdapter(al_Local_video0)
        updateAdapter(getActivity()!!)
        if (folderNamesTmp.isEmpty()) {
            binding!!.folderSpinner.setVisibility(android.view.View.GONE)
        } else {
            folderNames.add(KEY_ALL_FILES)
            folderNames.addAll(folderNamesTmp)
        }
    }
    //        //recyclerView1!!.setLayoutManager(null);
    //        recyclerView1!!.getRecycledViewPool().clear();
    //        recyclerView1!!.swapAdapter(adapter_videoFolder, false);
    //       // recyclerView1!!.setLayoutManager(layoutManager);
    //        adapter_videoFolder!!.notifyDataSetChanged();
    private fun showNoStoragePermissionSnackbar() {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding!!.snackbarContainer, R.string.label_no_storage_permission, Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(android.R.color.black)).setTextColor(getResources().getColor(android.R.color.white)).setActionTextColor(getResources().getColor(android.R.color.white)).setAction(getString(R.string.action_settings), { v -> 
                        //.setAction("Нет", null)
                        openApplicationSettings(getActivity()!!)
                        var t: Toast = Toast.makeText(getActivity()!!, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG)
                        t.show()
                        })
        } else {
            if (snackbar!!.isShown()) {
                snackbar!!.dismiss()
            }
        }
        snackbar!!.show()
    }
    private fun openApplicationSettings(context: Context) {
        var appSettingsIntent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()))
        startActivityForResult(appSettingsIntent, MainActivity.APPLICATION_DETAILS_SETTINGS)
    }
    override fun showPermission33SnackBar() {
        DLog.d("")
        if (snackbar == null) {
            snackbar = Snackbar.make(binding!!.snackbarContainer, R.string.label_no_storage_permission, LENGTH_INDEFINITE).setBackgroundTint(getResources().getColor(android.R.color.black)).setTextColor(getResources().getColor(android.R.color.white)).setActionTextColor(getResources().getColor(android.R.color.white)).setAction(getString(R.string.action_settings), { v -> 
                        //.setAction("Нет", null)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            presenter!!.openManageAllFiles()
                        } else {
                            mainView!!.openApplicationSettings()
                        }
                        })
        } else {
            if (snackbar!!.isShown()) {
                snackbar!!.dismiss()
            }
        }
        snackbar!!.show()
    }
    override fun onPause() {
        super.onPause()
        if (snackbar!! != null && snackbar!!.isShown()) {
            snackbar!!.dismiss()
        }
    }
    companion object {
        private const val KEY_ALL_FILES = "All Files"
    }
}
