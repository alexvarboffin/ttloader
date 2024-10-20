package com.retrytech.medialoot.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retrytech.medialoot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadLinkFragment extends Fragment {


    public DownloadLinkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download_link, container, false);
    }

}
