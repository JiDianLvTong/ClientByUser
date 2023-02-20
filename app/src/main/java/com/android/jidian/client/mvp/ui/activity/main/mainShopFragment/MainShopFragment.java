package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;


import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseFragment;
import com.android.jidian.client.mvp.ui.activity.main.MainActivityEvent;
import com.android.jidian.client.mvp.ui.activity.main.mainFindFragment.MainFindEvent;
import com.android.jidian.client.util.UserInfoHelper;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainShopFragment extends U6BaseFragment {

    @BindView(R.id.slidingTabLayout)
    public SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    private String[] nameList = new String[]{"购买", "租赁"};
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
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MainShopEvent event) {
        if (event != null) {

        }
    }

    //设置坐标
    public void setFragmentRefresh() {
        if (viewPager != null) {
            if (viewPager.getCurrentItem() == 0) {
                if (fragments[0] != null) {
                    fragments[0].setFragmentRefresh();
                }
            } else {
                if (fragments[1] != null) {
                    fragments[1].setFragmentRefresh();
                }
            }
        }
    }

    @Override
    public void initView(View view) {
        fragments[0] = new MainShopItemFragment(1, mLng, mLat);
        fragments[1] = new MainShopItemFragment(2, mLng, mLat);
        mainShopAdapter = new MainShopAdapter(getFragmentManager(), fragments, nameList);
        viewPager.setAdapter(mainShopAdapter);
        viewPager.setOffscreenPageLimit(2);
        slidingTabLayout.setViewPager(viewPager, nameList);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
////                if (i == 0) {
////                }else {
////                    EventBus.getDefault().post(new ShopFragmentItemEvent(ShopFragmentItemEvent.SHOP_ITEM_2_REFRESH));
////                }
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                Log.d("xiaoming0214", "onPageSelected: " + i);
//                EventBus.getDefault().post(new ShopFragmentItemEvent(ShopFragmentItemEvent.SHOP_ITEM_REFRESH));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {//不可见
//            Log.d("xiaoming0214", "onHiddenChanged: " +viewPager.getCurrentItem());
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("xiaoming0214", "onResume: " + viewPager.getCurrentItem());
//        if (viewPager.getCurrentItem() == 0) {
//            if (fragments[0] != null) {
//                fragments[0].setFragmentRefresh();
//            }
//        }else {
//            if (fragments[1] != null) {
//                fragments[1].setFragmentRefresh();
//            }
//        }
//        if (mPresenter != null) {
//            mPresenter.requestUserPersonal(UserInfoHelper.getInstance().getUid());
//            requestData();
//        }
    }

    //设置坐标
    public void setFragmentPosition(String lng, String lat) {
        this.mLng = lng;
        this.mLat = lat;
        if (fragments[0] != null) {
            fragments[0].setFragmentPosition(lng, lat);
        }
        if (fragments[1] != null) {
            fragments[1].setFragmentPosition(lng, lat);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
