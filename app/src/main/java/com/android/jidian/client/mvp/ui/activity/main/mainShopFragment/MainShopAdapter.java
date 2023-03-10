package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainShopAdapter extends FragmentPagerAdapter {

    private String[] mTitle;
    private MainShopItemFragment[] fragments;

    public MainShopAdapter(FragmentManager fm, MainShopItemFragment[] fragments, String[] pageNames) {
        super(fm);
        this.fragments = fragments;
        this.mTitle = pageNames;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
