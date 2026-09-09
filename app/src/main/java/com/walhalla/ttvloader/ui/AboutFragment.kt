package com.walhalla.ttvloader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.android.widget.Config
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var binding: FragmentAboutBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutBinding.inflate(inflater, container!!, false)
        return binding!!.getRoot()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState!!)
        var m1: String = getString(R.string.text_about).replace("__appName__", "Likee")
        var m2: String = Config.videoFolder(getContext()!!).getAbsolutePath()
        //        try {
        //            m2=m2+"\n"+Config.videoFolder(getContext()!!).getCanonicalPath();//SharedObjects.externalMemory().absolutePath + File.separator + Q.DOWNLOAD_DIRECTORY;
        //        } catch (IOException e) {
        //            DLog.handleException(e);
        //        }
        binding!!.textAbout.setText(m1)
        binding!!.textAbout2.setText(m2)
    }
}
