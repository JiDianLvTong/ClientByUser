package com.android.jidian.repair.mvp.main;

import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.login.LoginActivity;
import com.android.jidian.repair.utils.UserInfoHelper;
import com.android.jidian.repair.widgets.NoScrollViewPager;
import com.android.jidian.repair.widgets.ViewPagerAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivityByMvp<MainPresenter> implements MainContract.View {

    @BindView(R.id.vp_content)
    public NoScrollViewPager vpContent;
    @BindView(R.id.tab_layout_main_page)
    public CommonTabLayout tabLayoutMainPage;

    private List<Fragment> mFragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        startActivity(new Intent(this, LoginActivity.class));
        ArrayList<CustomTabEntity> mMainTabEntities = new ArrayList<>();
        String[] mTitles = {"", "", "", ""};
        int[] mIconUnSelectIds = {
                R.drawable.main_count_gray, R.drawable.main_check_gray,
                R.drawable.main_check_gray, R.drawable.main_user_gray
        };
        int[] mIconSelectIds = {
                R.drawable.main_count_yellow, R.drawable.main_check_yellow,
                R.drawable.main_check_yellow, R.drawable.main_user_yellow
        };

        for (int i = 0; i < mIconSelectIds.length; i++) {
            mMainTabEntities.add(new MainTabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        tabLayoutMainPage.setTabData(mMainTabEntities);
        tabLayoutMainPage.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                onTabIconSelected(position);
                if (TextUtils.isEmpty(UserInfoHelper.getInstance().getUid())) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        //添加fragment
        mFragments = new ArrayList<>();
//        mMainChartFragment = MainChartFragment.newInstance();
//        mFragments.add(mMainChartFragment);
//        mFragments.add(MainToolFragment.newInstance());
//        mFragments.add(MainWalletFragment.newInstance());
//        mFragments.add(MainPersonalCenterFragment.newInstance());

        vpContent.setOffscreenPageLimit(mTitles.length - 1);
        vpContent.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        vpContent.setScrollEnable(false);
        if (TextUtils.isEmpty(UserInfoHelper.getInstance().getUid())) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }

    private void onTabIconSelected(int position) {
        vpContent.setCurrentItem(position, false);
        tabLayoutMainPage.setCurrentTab(position);
        if (position == 0 || position == 2) {
            //首页刷新数据
//            mMainWalletFragment.refreshData(true);
//            switchStatusBar(R.color.transparent, false, false);
            if (position == 2) {
//                ((MainWalletFragment) mFragments.get(2)).onFragmentShow();
            }
        } else {
//            switchStatusBar(R.color.color_FFFFFF, true, true);
        }
    }

    /**
     * 嵌套类
     */
    private static final class MainTabEntity implements CustomTabEntity {
        public String title;
        int selectedIcon;
        int unSelectedIcon;

        MainTabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }
}