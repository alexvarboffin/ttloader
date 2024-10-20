package com.retrytech.medialoot.Fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.retrytech.medialoot.Adapter.SaveFileAdapter;
import com.retrytech.medialoot.model.DownloadFile;
import com.retrytech.medialoot.R;
import com.retrytech.medialoot.databinding.FragmentSaveFileBinding;
import com.io.tools.android.ramiloif.folderchooser.ChooseDirectoryDialog;
import com.walhalla.ui.DLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SaveFileAdapter.OnItemClickListener {


    private static final int FILE_SELECT_CODE = 0;
    private Cursor videocursor;
    private File[] allFiles;
    private SaveFileAdapter adapter;
    private String path;
    private ArrayList<DownloadFile> downloadFileArrayList = new ArrayList<>();

    public SaveFileFragment() {
        // Required empty public constructor
    }

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    FragmentSaveFileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save_file, container, false);
        binding.saveSwipe.setOnRefreshListener(this::onRefresh);

        AdView adView = new AdView(binding.getRoot().getContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getContext().getString(R.string.admob_banner));
        AdRequest.Builder adRequest= new AdRequest.Builder();
        adView.loadAd(adRequest.build());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.adContainer.addView(adView, params);


        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("savefile"));
        perMission();

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        /*binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Exit");
                    alertDialog.setMessage("Are you sure you want to Exit ?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        }
                    }).setNegativeButton("No", null);
                    alertDialog.show();

                    return true;
                }
                return false;
            }
        });
*/
        return binding.getRoot();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("isfinish", false)) {
               /* binding.saveSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        binding.saveSwipe.setRefreshing(true);
                        binding.ivDelete.setVisibility(View.GONE);
                        binding.clear.setVisibility(View.GONE);
                        init_phone_video_grid();
                    }
                });*/

//                 binding.saveSwipe.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            adapter.isMultiselect = false;
                            adapter.notifyDataSetChanged();
                            binding.ivDelete.setVisibility(View.GONE);
                            binding.clear.setVisibility(View.GONE);
                            init_phone_video_grid();
                        }
                    }
                }, 700);
            }
        }

    };

    private void init_phone_video_grid() {

        System.gc();
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED};
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + Environment.DIRECTORY_DOWNLOADS + "%"};
        videocursor = getActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, selection, selectionArgs, MediaStore.Video.Media.DATE_ADDED + " DESC");
//        Toast.makeText(getContext(), String.valueOf(videocursor.getCount()), Toast.LENGTH_SHORT).show();
        downloadFileArrayList = new ArrayList<>();
        if (videocursor.moveToFirst()) {
            do {
                DownloadFile downloadFile = new DownloadFile();
                String data = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String filename = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String filesize = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String fileduration = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String filedate = videocursor.getString(videocursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                downloadFile.setFilename(filename);
                downloadFile.setPath(data);
                downloadFile.setTotalSize(filesize);
                downloadFile.setAddedDate(filedate);
                downloadFile.setDuration(fileduration);
                downloadFileArrayList.add(downloadFile);

                // do what ever you want here
            } while (videocursor.moveToNext());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

//        binding.directory.setText("Directory: "+Environment.DIRECTORY_DOWNLOADS);

        Collections.sort(downloadFileArrayList, new Comparator<DownloadFile>() {
            public int compare(DownloadFile example, DownloadFile t1) {
                Integer v1 = (int) Long.parseLong(example.getAddedDate());
                Integer v2 = (int) Long.parseLong(t1.getAddedDate());
                return v2.compareTo(v1);
            }
        });

        adapter = new SaveFileAdapter(allFiles, downloadFileArrayList, this);
        binding.rvFile.setAdapter(adapter);

        binding.saveSwipe.setRefreshing(false);

        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.isMultiselect = false;
                adapter.notifyDataSetChanged();
                binding.ivDelete.setVisibility(View.GONE);
                binding.clear.setVisibility(View.GONE);
            }
        });

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.remove_action_btn)
                        .setMessage("Remove " + adapter.getDeletebleArray().size() + " Videos?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for (int i = 0; i < adapter.getDeletebleArray().size(); i++) {
                                    File files = new File(adapter.getDeletebleArray().get(i));
                                    boolean deleted = files.delete();

                                    adapter.scanFile(files);
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.isMultiselect = false;
                                        adapter.notifyDataSetChanged();
                                        binding.ivDelete.setVisibility(View.GONE);
                                        binding.clear.setVisibility(View.GONE);
                                        init_phone_video_grid();
                                    }
                                }, 700);


                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();


            }
        });
    }

    private void perMission() {
        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            init_phone_video_grid();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (checkSelfPermission(context, permission) != PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.ivDelete.setVisibility(View.GONE);
                        binding.clear.setVisibility(View.GONE);
                        init_phone_video_grid();
                    }
                }, 700);
            }
        }
    }

    @Override
    public void onRefresh() {

        binding.saveSwipe.setRefreshing(true);
        binding.ivDelete.setVisibility(View.GONE);
        binding.clear.setVisibility(View.GONE);
        init_phone_video_grid();

    }

    @Override
    public void onItemClick(SaveFileAdapter.SaveFileViewHolder holder, int position, DownloadFile file) {

        ChooseDirectoryDialog dialog =
                ChooseDirectoryDialog.builder(getActivity()). // Context
                        titleText("Choose directory"). // The title will be shown
                        startDir(Environment.getExternalStorageDirectory().getAbsoluteFile()).// File from where to start
                        showNeverAskAgain(false). // Enable or disable 'Never ask again checkbox
                        neverAskAgainText("Never ask again"). // Text of never ask again check box(if enabled)
                        onPickListener(new ChooseDirectoryDialog.DirectoryChooseListener() {
                    @Override
                    public void onDirectoryPicked(ChooseDirectoryDialog.DialogResult result) {

                        String downloadPath = result.getPath();

                        File newFile = new File(new File(downloadPath), file.getFilename());
                        FileChannel outputChannel = null;
                        FileChannel inputChannel = null;
                        try {
                            outputChannel = new FileOutputStream(newFile).getChannel();
                            inputChannel = new FileInputStream(file.getPath()).getChannel();
                            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                            inputChannel.close();

                            new File(file.getPath()).delete();
                            adapter.remove(position);

                            Toast.makeText(getContext(), "File Move To " + newFile.getPath(), Toast.LENGTH_SHORT).show();

                        } catch (FileNotFoundException e) {
                            DLog.handleException(e);
                        } catch (IOException e) {
                            DLog.handleException(e);
                        } finally {
                            if (inputChannel != null) {
                                try {
                                    inputChannel.close();
                                } catch (IOException e) {
                                    DLog.handleException(e);
                                }
                            }
                            if (outputChannel != null) {
                                try {
                                    outputChannel.close();
                                } catch (IOException e) {
                                    DLog.handleException(e);
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancel() {
//                                resultTV.setText("operation canceled");
                    }
                }).build();
        dialog.show();

    }

    @Override
    public void onMultidelete(boolean ischeck) {
        if (ischeck) {
            binding.ivDelete.setVisibility(View.VISIBLE);
            binding.clear.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
            binding.clear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemRename() {
        init_phone_video_grid();
    }


}
