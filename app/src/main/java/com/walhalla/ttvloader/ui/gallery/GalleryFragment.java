package com.walhalla.ttvloader.ui.gallery;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.google.android.material.snackbar.Snackbar;
import com.walhalla.adapters.CustomSpinnerAdapter;
import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ttvloader.activity.main.MainActivity;
import com.walhalla.ttvloader.databinding.FragmentGalleryBinding;
import com.walhalla.ttvloader.mvp.MainView;
import com.walhalla.ttvloader.R;

import com.walhalla.adapters.EmptyViewModel;
import com.walhalla.ttvloader.models.LocalVideo;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment implements GalleryPresenter.View {

    private static final String KEY_ALL_FILES = "All Files";


    List<String> folderNames = new ArrayList<>();
    Map<String, List<LocalVideo>> videosByFolder = new LinkedHashMap<>();

    private VideoStorageAdapter _videoStorageAdapter = null;
    ArrayList<Object> al_Local_video0 = new ArrayList<>();

    private MainView mainView;
    private FragmentGalleryBinding binding;

    private CustomSpinnerAdapter spinnerAdapter;

    private final AdapterView.OnItemSelectedListener mmm = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
            String selectedFolder = folderNames.get(position);
            if (KEY_ALL_FILES.equals(selectedFolder)) {
                _videoStorageAdapter.swapAdapter(al_Local_video0);
            } else {
                List<LocalVideo> selectedVideos = videosByFolder.get(selectedFolder);
                _videoStorageAdapter.swapAdapter0(selectedVideos);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // Handle the case where no folder is selected
        }
    };


    private GalleryPresenter presenter;
    private Snackbar snackbar;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResultLauncher<String[]> launcher29 = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), map -> {


            StringBuilder sb = new StringBuilder();

            boolean isGranted = false;

            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                boolean tmp = entry.getValue();
                if (tmp) {
                    isGranted = true;
                }
                sb.append("").append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }

            if (isGranted) {// Permission is granted. Continue with updating the UI
                // Permission is granted. Continue with updating the UI
                updateGUI(getActivity(), true);
                //Toast.makeText(getContext(), "GRANTED", Toast.LENGTH_LONG).show();
            } else { // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

                //mainView.showNoStoragePermissionSnackbar();
                showNoStoragePermissionSnackbar();
                Toast.makeText(getContext(), "NOT GRANTED\n"
                        + sb.toString(), Toast.LENGTH_LONG).show();
            }
        });


        ActivityResultLauncher<Intent> storageActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        o -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                //Android is 11 (R) or above
                                if (Environment.isExternalStorageManager()) {
                                    //Manage External Storage Permissions Granted
                                    DLog.d("onActivityResult: Manage External Storage Permissions Granted");
                                } else {
                                    Toast.makeText(getActivity(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Below android 11

                            }
                        });

        Handler handler = new Handler();
        presenter = new GalleryPresenter(this, handler, (AppCompatActivity) getActivity(),
                launcher29,
                storageActivityResultLauncher
        );
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (_videoStorageAdapter.getItemViewType(position) == VideoStorageAdapter.VIEW_TYPE_EMPTY) {
                    return 3;
                }
                return 1;
            }
        });
        binding.recyclerView.setLayoutManager(manager);

        if (al_Local_video0.isEmpty()) {
            al_Local_video0.add(new EmptyViewModel(getString(R.string.empty_data)));
        }

        _videoStorageAdapter = new VideoStorageAdapter(getActivity(), al_Local_video0, mainView);
        binding.recyclerView.setAdapter(null);
        binding.recyclerView.setAdapter(_videoStorageAdapter);
        _videoStorageAdapter.notifyDataSetChanged();

//        m1 ma = ((m1) getActivity());
//        if (ma != null && !ma.isNeedGrantPermission()) {
//            fn_video(getActivity().getContentResolver(), getActivity(), true);
//        }
        //getAllMediaFilesOnDevice(getContext());

        //checkAndRequestPermissions();
//        binding.test.setOnClickListener(v->{
//
////                    new RedditIntent().shareMp4Selector(getContext(),
////                            new File("/storage/emulated/0/screen-recording-1710055824379.mp4"));
//        });

        spinnerAdapter = new CustomSpinnerAdapter(
                getActivity(), R.layout.simple_sp_item, folderNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.folderSpinner.setAdapter(spinnerAdapter);
        binding.folderSpinner.setOnItemSelectedListener(mmm);

        if (BuildConfig.DEBUG) {
            binding.getRoot().setOnClickListener(v -> {
                showPermission33SnackBar();
            });
        }

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
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
//                path = path.substring(0, lastPoint) + path.substring(lastPoint).toLowerCase();
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
//        _videoStorageAdapter = new VideoStorageAdapter(getContext(), al_Local_video, mainView);
//        recyclerView.setAdapter(null);
//        recyclerView.setAdapter(_videoStorageAdapter);
//        _videoStorageAdapter.notifyDataSetChanged();
//        return files;
//    }


    private void updateAdapter(Context context) {
        //spinnerAdapter.swapData(folderNames);
        spinnerAdapter = new CustomSpinnerAdapter(context, R.layout.simple_sp_item, folderNames);
        //spinnerAdapter.setDropDownViewResource(R.layout.simple_sp_item);
        binding.folderSpinner.setAdapter(spinnerAdapter);
        binding.folderSpinner.setPrompt(getString(R.string.app_name));

        binding.folderSpinner.setOnItemSelectedListener(mmm);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            MainActivity ma = ((MainActivity) getActivity());
            if (ma != null) {
                //presenter.isNeedGrantPermission();
            }
            //getAllMediaFilesOnDevice(getContext());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainView) {
            mainView = (MainView) context;
        }
    }


    @Override
    public void updateGUI() {
        updateGUI(getActivity(), true);
    }

    public void updateGUI(@NonNull Activity activity, boolean b) {
        ContentResolver contentResolver = activity.getContentResolver();
        al_Local_video0 = new ArrayList<>();
        ArrayList<String> folderNamesTmp = new ArrayList<>();
        folderNames = new ArrayList<>();

        int int_position = 0;
        //FileProvider.getUriForFile(context, authority, file);
        String[] selectionArguments = new String[]{"%" + Environment.DIRECTORY_MOVIES};//Q.DOWNLOAD_DIRECTORY
        String sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC";
        String[] projection = new String[]{
//                MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.BUCKET_ID,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//                //@@MediaStore.Images.Media.DATA,
//                MediaStore.Video.Media.DATE_TAKEN,
//                MediaStore.Video.Media.SIZE,
//                MediaStore.Video.Media.DURATION

                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.SIZE,
                //@@@ MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Thumbnails.DATA
        };
        Cursor mCursor = null;


        try {
            mCursor = contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,//                    MediaStore.Video.Media.DATA + " LIKE ?", //condition
                    null,//                    selectionArguments, //selectionArguments,
                    sortOrder
            );
        } catch (CursorIndexOutOfBoundsException e) {
            DLog.d("В поиск добавлено поле которого не существует");
        }


        try {
            //Primary external storage directory

            if (null == mCursor) {
                DLog.d("CURSOR NULL");

            } else if (mCursor.getCount() < 1) {
                DLog.d("CURSOR EMPTY");
                /*
                 * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
                 * an error. You may want to offer the user the option to insert a new row, or re-type the
                 * search term.
                 */

            } else {
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

                int _id = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int column_index_data = mCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                int column_index_folder_name = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                int thum = mCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
                int duration = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                int i = 0;

                videosByFolder = new LinkedHashMap<>();


                Map<String, LocalVideo> sortedByDate = new LinkedHashMap<>();

                while (mCursor.moveToNext()) {
                    String absolutePathOfImage = mCursor.getString(column_index_data);
                    String folderName = mCursor.getString(column_index_folder_name);

                    //DLog.d(absolutePathOfImage);
                    //DLog.d("@@@"+mCursor.getString(column_index_folder_name));
//                    DLog.d(mCursor.getString(column_id));
//                    DLog.d(mCursor.getString(thum));
//                    DLog.d(mCursor.getString(duration));

                    LocalVideo video = new LocalVideo();
                    video.selected = false;
                    video.path = absolutePathOfImage;
                    video.thumb = mCursor.getString(thum);
                    video.duration = mCursor.getInt(duration);
                    video.setId(i);

                    //al_Local_video0.add(video);
                    sortedByDate.put(video.path, video);


                    //
                    // Сортировка по папкам
                    if (!videosByFolder.containsKey(folderName)) {
                        videosByFolder.put(folderName, new ArrayList<>());  // Создаем новую папку если она не существует

                        folderNamesTmp.add(folderName);
                    }
                    videosByFolder.get(folderName).add(video);
                    //

                    i = i + 1;
                }

                for (Map.Entry<String, LocalVideo> entry : sortedByDate.entrySet()) {
                    al_Local_video0.add(entry.getValue());
                }
            }


        } catch (Exception e) {
            DLog.handleException(e);
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        if (al_Local_video0.isEmpty()) {
            al_Local_video0.add(new EmptyViewModel(getString(R.string.empty_data)));
        }

        _videoStorageAdapter.swapAdapter(al_Local_video0);
        updateAdapter(getActivity());

        if (folderNamesTmp.isEmpty()) {
            binding.folderSpinner.setVisibility(android.view.View.GONE);
        } else {
            folderNames.add(KEY_ALL_FILES);
            folderNames.addAll(folderNamesTmp);
        }

//        //recyclerView1!!.setLayoutManager(null);
//        recyclerView1!!.getRecycledViewPool().clear();
//        recyclerView1!!.swapAdapter(adapter_videoFolder, false);
//       // recyclerView1!!.setLayoutManager(layoutManager);
//        adapter_videoFolder!!.notifyDataSetChanged();
    }

    private void showNoStoragePermissionSnackbar() {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.snackbarContainer
                            , R.string.label_no_storage_permission, Snackbar.LENGTH_LONG)
                    //.setAction("Нет", null)
                    .setBackgroundTint(getResources().getColor(android.R.color.black))
                    .setTextColor(getResources().getColor(android.R.color.white))
                    .setActionTextColor(getResources().getColor(android.R.color.white))
                    .setAction(getString(R.string.action_settings), v -> {
                        openApplicationSettings(getActivity());
                        Toast t = Toast.makeText(getActivity()
                                , getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
                        t.show();
                    });
        } else {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
        snackbar.show();
    }

    private void openApplicationSettings(Context context) {
        Intent appSettingsIntent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
        startActivityForResult(appSettingsIntent, MainActivity.APPLICATION_DETAILS_SETTINGS);
    }

    @Override
    public void showPermission33SnackBar() {
        DLog.d("");
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.snackbarContainer, R.string.label_no_storage_permission, LENGTH_INDEFINITE)
                    //.setAction("Нет", null)
                    .setBackgroundTint(getResources().getColor(android.R.color.black))
                    .setTextColor(getResources().getColor(android.R.color.white))
                    .setActionTextColor(getResources().getColor(android.R.color.white))
                    .setAction(getString(R.string.action_settings), v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            presenter.openManageAllFiles();
                        } else {
                            mainView.openApplicationSettings();
                        }
                    });
        } else {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
        snackbar.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
