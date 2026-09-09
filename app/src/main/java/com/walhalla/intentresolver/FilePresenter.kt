package com.walhalla.intentresolver

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import androidx.preference.PreferenceManager
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import com.walhalla.ui.DLog
import java.io.File
import java.util.Locale

class FilePresenter {
    private lateinit var pref: SharedPreferences
    //    private File mSelectedFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    //    String lastEmul = "video.mp4";
    //    DefaultIntent defaultIntent = new LikeeIntent();
    //    private File mSelectedFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    //    String lastEmul = "video_9@16-10-2023_19-29-07.mp4";
    private var mSelectedFolder: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    //real dev
    var lastEmul0: String? = null
    //"video (61).mp4"
    private var mView: FileView? = null
    private var context: Context? = null
    private var mSelectedFileType: String? = null
    private var data: Array<File>? = null
    private var mFileCount: Int = 0
    private var lastNumber: Int = 0
    private var defaultIntent: BaseIntent? = null
    constructor(view: FileView, context: Context) {
        mView = view
        this.context = context
        this.pref = PreferenceManager.getDefaultSharedPreferences(context)
        var tmp: String? = pref.getString(KEY_FOLDER, null)
        if (tmp != null) {
            mSelectedFolder = File(tmp)
            mView!!.showSelectedFolder(mSelectedFolder)
        }
    }
    fun resume() {
        ++mFileCount
        shareFile()
        resolvMp4ActivitiesForPackage(context!!)
    }
    private fun resolvMp4ActivitiesForPackage(context: Context) {
    }
    //        PackageManager packageManager = context.getPackageManager();
    //        Intent shareIntent = new Intent(Intent.ACTION_SEND);
    //        shareIntent.setPackage(PACKAGE_LIKEE);
    //        shareIntent.setType("video/*");
    //
    //        List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, 0);
    //        for (ResolveInfo info : activities) {
    //            String packageName = info.activityInfo.packageName;
    //            String activityName = info.activityInfo.name;
    //            DLog.d("["+packageName+"]"+activityName);
    //        }
    // Метод для возврата BaseIntent по номеру
    private fun getBaseIntentByNumber(selectedIntent: Int): BaseIntent {
        if (selectedIntent == 0) {
            return YoutubeIntent()
        } else if (selectedIntent == 1) {
            return InstagramIntent()
        } else if (selectedIntent == 2) {
            return OkruIntent()
        } else if (selectedIntent == 3) {
            return TiktokIntent()
        } else if (selectedIntent == 4) {
            return LikeeIntent()
        }
        return YoutubeIntent()
    }
    // По умолчанию YoutubeIntent
    fun start(number: Int, selectedIntent: Int) {
        defaultIntent = getBaseIntentByNumber(selectedIntent)
        lastEmul0 = null
        lastNumber = number
        data = mSelectedFolder.listFiles({
                pathname -> pathname.getName().endsWith(".mp4")
                })
        chooseFileType("mp4")
        mFileCount = 0
        DLog.d("@" + mSelectedFolder.exists() + "@" + (data?.size ?: 0))
        restoreNextAfter(lastEmul0 ?: "", lastNumber)
        shareFile()
    }
    fun chooseFolder(context: Context) {
        var properties: DialogProperties = DialogProperties()
        properties.selection_mode = DialogConfigs.SINGLE_MODE
        properties.selection_type = DialogConfigs.DIR_SELECT
        properties.root = File(DialogConfigs.DEFAULT_DIR)
        var dialog: FilePickerDialog = FilePickerDialog(context, properties)
        dialog.setTitle("Select a File")
        dialog.setDialogSelectionListener({ files -> 
                mSelectedFolder = File(files[0])
                pref.edit().putString(KEY_FOLDER, mSelectedFolder.getAbsolutePath()).apply()
                mView!!.showSelectedFolder(mSelectedFolder)
                })
        dialog.show()
    }
    //"/sdcard/Pictures"
    //        final File file = new File(s);
    //        final String parent = new File(s).getParent();
    //        //DLog.d("" + parent);
    //        //"com.speedsoftware.explorer"
    //        Uri uri = Uri.parse(s);
    //        //Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(parent));
    ////                    Intent intent = null;
    ////                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
    ////                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    ////                        intent.addCategory(Intent.CATEGORY_OPENABLE);
    ////                        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
    ////                    }
    //        Intent intent;
    //        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
    //            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    //            intent.addCategory(Intent.CATEGORY_OPENABLE);
    //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //            intent.setType(DocumentsContract.Document.MIME_TYPE_DIR);
    //            ActivityInfo tmp = intent.resolveActivityInfo(context.getPackageManager(), 0);
    //            if (tmp != null) {
    //                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //                //startActivityForResult(intent, OPEN_REQUEST);
    //            } else {
    //                //DLog.d();
    //            }
    //            mView.openFolderChooser(intent);
    //        } else {
    //            intent = new Intent(Intent.ACTION_GET_CONTENT);
    //            intent.setDataAndType(uri, "text/csv");
    //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //            Intent selI = Intent.createChooser(intent, "Open folder");
    //            mView.openFolderChooser(selI);
    //        }
    //emulator /storage/emulated/0/Pictures
    private fun restoreNextAfter(lastEmul: String, lastNumber: Int) {
        if (lastNumber > 0) {
            mFileCount = lastNumber
            return
        }
        if (TextUtils.isEmpty(lastEmul)) {
            return
        }
        val files = data ?: return
        var i: Int = 0
        while (i < files.size) {
            if (files[i].getName().endsWith(lastEmul)) {
                mFileCount = i + 1
                break
            }
            i++
        }
    }
    fun handleSelectedFolder(folderUri: Uri) {
        mSelectedFolder = File(folderUri.getPath())
        mView!!.showSelectedFolder(mSelectedFolder)
    }
    fun chooseFileType(fileType: String) {
        mSelectedFileType = fileType
    }
    fun shareFile() {
        if (mSelectedFolder == null || TextUtils.isEmpty(mSelectedFileType!!)) {
            mView!!.showError("Please select folder and file type first")
            return
        }
        val files = data
        if (files != null && files.size > 0 && mFileCount < files.size) {
            var file: File = files[mFileCount]
            var fileindex: Int = mFileCount + 1
            if (file.getName().endsWith(mSelectedFileType!!)) {
                var label: String = String.format(Locale.getDefault(), "shareFile: (%1\$d/%2\$d) ", fileindex, files.size)
                //DLog.d("@"+ label + " | "+file);
                DLog.d(file.toString())
                defaultIntent!!.shareMp4Selector(context!!, file)
                return
            }
        }
        mView!!.showError("No file of selected type found in the folder")
    }
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_CHOOSE_FOLDER && resultCode == RESULT_OK) {
            var folderUri = data.getData()
            if (folderUri != null) {
                handleSelectedFolder(folderUri)
            }
        }
    }
    companion object {
        private const val KEY_FOLDER = "key_folder"
        const val REQUEST_CODE_CHOOSE_FOLDER = 1077
    }
}
