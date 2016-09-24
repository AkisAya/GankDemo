package com.example.aki.gankdemo.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Aki on 2016/9/24.
 */

public class ContentPagerAdapter extends FragmentPagerAdapter{

    List<Fragment> mFragments;
    List<String> mTabTitles;

    public ContentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitles) {
        super(fm);
        mFragments = fragments;
        mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles.get(position);
    }
}
