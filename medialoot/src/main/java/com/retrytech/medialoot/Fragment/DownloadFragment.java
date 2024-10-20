package com.retrytech.medialoot.Fragment;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.retrytech.medialoot.Activity.MainDashboardActivity;
import com.retrytech.medialoot.Adapter.DownloadListAdapter;
import com.retrytech.medialoot.model.DownloadFile;
import com.retrytech.medialoot.R;
import com.retrytech.medialoot.databinding.FragmentDownloadBinding;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {


    private DownloadListAdapter listAdapter;


    public DownloadFragment() {
        // Required empty public constructor
    }

    private FragmentDownloadBinding binding;
    private ArrayList<DownloadFile> downloadFiles = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false);

        AdView adView = new AdView(binding.getRoot().getContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getContext().getString(R.string.admob_banner));
        AdRequest.Builder adRequest= new AdRequest.Builder();
        adView.loadAd(adRequest.build());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.adContainer.addView(adView, params);


        if (getActivity() != null) {
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
            lbm.registerReceiver(receiver, new IntentFilter("filter_string"));
            ((MainDashboardActivity) getActivity()).binding.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (downloadFiles != null && !downloadFiles.isEmpty()) {
                        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        for (int i = 0; i < downloadFiles.size(); i++) {
                            Log.i("count", "onClick: " + i);
                            manager.remove(downloadFiles.get(i).getId());
                            //downloadFiles.remove(i);

                        }

                    /*if (listAdapter != null) {
                        listAdapter.notifyDataSetChanged();
                    }*/
                    }
                }
            });
        }

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
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


        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        if (downloadFiles.isEmpty()) {
            ((MainDashboardActivity) Objects.requireNonNull(getActivity())).binding.tvCancel.setText("");
            binding.layNodata.setText("No Any Downloads");
        } else {
            binding.layNodata.setText("");
            ((MainDashboardActivity) Objects.requireNonNull(getActivity())).binding.tvCancel.setText("Cancel All");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (downloadFiles.isEmpty()) {
            ((MainDashboardActivity) Objects.requireNonNull(getActivity())).binding.tvCancel.setText("");
            binding.layNodata.setText("No Any Downloads");
        } else {
            binding.layNodata.setText("");
            ((MainDashboardActivity) Objects.requireNonNull(getActivity())).binding.tvCancel.setText("Cancel All");
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            DownloadFile downloadFile = intent.getParcelableExtra("progress");
            int position = checkFileisInArray(downloadFile);
            if (position == -1) {
                if (downloadFile.getProgress() != 101) {
                    downloadFiles.add(downloadFile);
                }
            } else {
                downloadFiles.get(position).setProgress(downloadFile.getProgress());
                downloadFiles.get(position).setTotalSize(downloadFile.getTotalSize());
                if (downloadFiles.get(position).getProgress() == 100) {
                    Toast.makeText(context, "Download Finish", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent("string");
                    intent1.putExtra("isdownload", false);
                    if (getActivity() != null) {
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);
                    }
                    downloadFiles.remove(downloadFiles.get(position));
                } else if (downloadFiles.get(position).getProgress() == 101) {
                    downloadFiles.remove(downloadFiles.get(position));
                }
            }
            if (downloadFiles.isEmpty()) {
                if (getActivity() != null && isAdded()) {
                    ((MainDashboardActivity) getActivity()).binding.tvCancel.setText("");
                    binding.layNodata.setText("No Any Downloads");
                }
            } else {
                binding.layNodata.setText("");
                if (getActivity() != null && isAdded()) {
                    ((MainDashboardActivity) getActivity()).binding.tvCancel.setText("Cancel All");
                }
            }

            if (listAdapter == null) {
                listAdapter = new DownloadListAdapter(downloadFiles);
                binding.recyclerview.setAdapter(listAdapter);
            } else {
                listAdapter.notifyDataSetChanged();
            }
            // Log.i("broadcast", "onReceive: " + downloadFile.getProgress());

        }
    };

    private int checkFileisInArray(DownloadFile file) {
        for (int i = 0; i < downloadFiles.size(); i++) {
            if (downloadFiles.get(i).getId() == file.getId()) {
                return i;
            }
        }
        return -1;
    }
}
