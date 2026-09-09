package com.walhalla.ttvloader.activity.mime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.databinding.ActivityMimeBinding

class MimiActivity : AppCompatActivity() {
    private var binding: ActivityMimeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMimeBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        //        binding!!.viewPager.setAdapter(new MimeTabAdapter(this));
        //
        //        // Связываем TabLayout с ViewPager2
        //        new TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager,
        //                (tab, position) -> tab.setText(MimeTabData.TAB_TITLES.get(position))
        //        ).attach();
        binding!!.viewPager.setAdapter(MimeTabAdapter(this))
        // Связываем TabLayout с ViewPager2
        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager, {
                tab, position -> tab.setText(MimeTabData.TAB_TITLES.`get`(position))
                }).attach()
    }
}
