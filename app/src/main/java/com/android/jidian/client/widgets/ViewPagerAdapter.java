package com.android.jidian.client.widgets;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author : PTT
 * date: 2020/7/30 16:01
 * company: 兴达智联
 * description:
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] mTabs;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabs) {
        super(fm);
        this.fragments = fragments;
        this.mTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position];
    }
}