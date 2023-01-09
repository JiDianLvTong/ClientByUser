package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.base.U6BaseFragment;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.MainShopContract;
import com.android.jidian.client.mvp.presenter.MainShopPresenter;
import com.flyco.tablayout.SlidingTabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class MainShopFragment extends U6BaseFragment {

    @BindView(R.id.slidingTabLayout)
    public SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    private String[] nameList = new String[]{"购买","租赁"};
    private MainShopItemFragment[] fragments = new MainShopItemFragment[2];
    private MainShopAdapter mainShopAdapter;

    //获取地理位置
    private String mLng = "";
    private String mLat = "";

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_shop;
    }

    @Override
    public void initView(View view) {
        fragments[0] = new MainShopItemFragment(1,mLng , mLat);
        fragments[1] = new MainShopItemFragment(2,mLng , mLat);
        mainShopAdapter = new MainShopAdapter(getFragmentManager() , fragments , nameList);
        viewPager.setAdapter(mainShopAdapter);
        viewPager.setOffscreenPageLimit(2);
        slidingTabLayout.setViewPager(viewPager, nameList);
    }

    //设置坐标
    public void setFragmentPosition(String lng, String lat) {
        this.mLng = lng;
        this.mLat = lat;
        if(fragments[0] != null){
            fragments[0].setFragmentPosition(lng,lat);
        }
        if(fragments[1] != null){
            fragments[1].setFragmentPosition(lng,lat);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
