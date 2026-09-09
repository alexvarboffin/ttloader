package com.walhalla.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewpagerAdapter : FragmentPagerAdapter {
    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    private val mFragmentTitleList: ArrayList<String> = ArrayList()
    constructor(fm: FragmentManager, behavior: Int) : super(fm, behavior) {
    }
    override fun getItem(position: Int): Fragment {
        return mFragmentList.`get`(position)
    }
    //            viewPager!!.currentItem;
    //            return when(position){
    //
    //                0-> download();
    //                1->gallery();
    //                else -> gallery();
    //            }
    override fun getCount(): Int {
        return mFragmentList.size
    }
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
    override fun getPageTitle(position: Int): String {
        return mFragmentTitleList.`get`(position)
    }
}
