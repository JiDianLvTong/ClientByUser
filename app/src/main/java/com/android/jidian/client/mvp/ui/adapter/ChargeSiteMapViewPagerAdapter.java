package com.android.jidian.client.mvp.ui.adapter;

import android.app.Activity;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
//import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ChargeSiteMapViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<View> views;

    public ChargeSiteMapViewPagerAdapter(Activity activity, ArrayList<View> views){
        this.activity = activity;
        this.views = views;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {	//这个方法用来实例化页卡
        container.addView(views.get(position), 0);//添加页卡
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}