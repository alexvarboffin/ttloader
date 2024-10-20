package com.retrytech.medialoot.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPaggerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public ViewPaggerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setFragment(ArrayList<Fragment> fragments) {

        this.fragments = fragments;
    }
}
