package com.walhalla.intentresolver;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.walhalla.ui.DLog;

import java.io.File;
import java.util.Locale;

public class FilePresenter {

    private static final String KEY_FOLDER = "key_folder";

    private final SharedPreferences pref;




//    private File mSelectedFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//    String lastEmul = "video.mp4";

//    DefaultIntent defaultIntent = new LikeeIntent();
//    private File mSelectedFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//    String lastEmul = "video_9@16-10-2023_19-29-07.mp4";




    private File mSelectedFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);//real dev
    String lastEmul0 = null;//"video (61).mp4"


    public static final int REQUEST_CODE_CHOOSE_FOLDER = 1077;

    private FileView mView;
    private Context context;


    private String mSelectedFileType;

    private File[] data;
    private int mFileCount;
    private int lastNumber;
    private BaseIntent defaultIntent;


    public FilePresenter(FileView view, Context context) {
        mView = view;
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        String tmp = pref.getString(KEY_FOLDER, null);
        if (tmp != null) {
            mSelectedFolder = new File(tmp);
            mView.showSelectedFolder(mSelectedFolder);
        }

    }

    public void resume() {
        ++mFileCount;
        shareFile();


        resolvMp4ActivitiesForPackage(context);
    }


    private void resolvMp4ActivitiesForPackage(Context context) {
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
    }
    // Метод для возврата BaseIntent по номеру
    private BaseIntent getBaseIntentByNumber(int selectedIntent) {
        if (selectedIntent == 0) {
            return new YoutubeIntent();
        } else if (selectedIntent == 1) {
            return new InstagramIntent();
        } else if (selectedIntent == 2) {
            return new OkruIntent();
        } else if (selectedIntent == 3) {
            return new TiktokIntent();
        } else if (selectedIntent == 4) {
            return new LikeeIntent();
        }
        return new YoutubeIntent(); // По умолчанию YoutubeIntent
    }

    public void start(int number, int selectedIntent) {
        defaultIntent=getBaseIntentByNumber(selectedIntent);
        
        lastEmul0 = null;
        lastNumber = number;

        data = mSelectedFolder.listFiles(pathname -> pathname.getName().endsWith(".mp4"));
        chooseFileType("mp4");
        mFileCount = 0;
        DLog.d("@@@" + mSelectedFolder.exists() + "@@@" + data.length);

        restoreNextAfter(lastEmul0, lastNumber);
        shareFile();
    }

    public void chooseFolder(Context context) {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        FilePickerDialog dialog = new FilePickerDialog(context, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(files -> {
            mSelectedFolder = new File(files[0]);
            pref.edit().putString(KEY_FOLDER, mSelectedFolder.getAbsolutePath()).apply();
            mView.showSelectedFolder(mSelectedFolder);
        });
        dialog.show();

        //"/sdcard/Pictures"
//        final File file = new File(s);
//        final String parent = new File(s).getParent();
//        //DLog.d("@@@@@@@@@@" + parent);
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


    }

    private void restoreNextAfter(String lastEmul, int lastNumber) {
        if (lastNumber > 0) {
            mFileCount = lastNumber;
            return;
        }
        if (TextUtils.isEmpty(lastEmul)) {
            return;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i].getName().endsWith(lastEmul)) {
                mFileCount = i + 1;
                break;
            }
        }
    }

    public void handleSelectedFolder(Uri folderUri) {
        mSelectedFolder = new File(folderUri.getPath());
        mView.showSelectedFolder(mSelectedFolder);
    }

    public void chooseFileType(String fileType) {
        mSelectedFileType = fileType;
    }

    public void shareFile() {
        if (mSelectedFolder == null || TextUtils.isEmpty(mSelectedFileType)) {
            mView.showError("Please select folder and file type first");
            return;
        }

        if (data != null && data.length > 0 && mFileCount < data.length) {
            File file = data[mFileCount];
            int fileindex = mFileCount + 1;
            if (file.getName().endsWith(mSelectedFileType)) {
                String label = String.format(Locale.getDefault(), "shareFile: (%1$d/%2$d) ", fileindex, data.length);
                //DLog.d("@@@"+ label + " | "+file);
                DLog.d(String.valueOf(file));

                defaultIntent.shareMp4Selector(context, file);
                return;
            }
        }
        mView.showError("No file of selected type found in the folder");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_FOLDER && resultCode == RESULT_OK) {
            Uri folderUri = data.getData();
            if (folderUri != null) {
                handleSelectedFolder(folderUri);
            }
        }
    }


}