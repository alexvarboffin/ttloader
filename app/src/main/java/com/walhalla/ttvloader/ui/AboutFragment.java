package com.walhalla.ttvloader.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.widget.Config;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String m1 = getString(R.string.text_about).replace("__appName__", "Likee");
        String m2 =
                Config.videoFolder(getContext()).getAbsolutePath();
//        try {
//            m2=m2+"\n"+Config.videoFolder(getContext()).getCanonicalPath();//SharedObjects.externalMemory() + File.separator + Q.DOWNLOAD_DIRECTORY;
//        } catch (IOException e) {
//            DLog.handleException(e);
//        }

        binding.textAbout.setText(m1);
        binding.textAbout2.setText(m2);

    }
}
