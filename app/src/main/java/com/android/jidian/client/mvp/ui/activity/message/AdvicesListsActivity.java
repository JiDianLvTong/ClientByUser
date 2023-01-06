package com.android.jidian.client.mvp.ui.activity.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.client.BaseActivity;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.widgets.ViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AdvicesListsActivity extends U6BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvRightTitle;
    @BindView(R.id.advices_lists_tabLayout)
    SlidingTabLayout advicesListsTabLayout;
    @BindView(R.id.advices_lists_viewPager)
    ViewPager advicesListsViewPager;

    private String[] mTabs;
    private AdvicesListsFragment1 fragment1;
    private AdvicesListsFragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_advices_lists);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragment1 = AdvicesListsFragment1.newInstance("", "");
        fragment2 = AdvicesListsFragment2.newInstance("", "");
        fragments.add(fragment1);
        fragments.add(fragment2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTabs);
        advicesListsViewPager.setAdapter(adapter);
        advicesListsViewPager.setOffscreenPageLimit(1);
        mTabs = getResources().getStringArray(R.array.app_msg_tab);
        advicesListsTabLayout.setViewPager(advicesListsViewPager, mTabs);
        advicesListsViewPager.setCurrentItem(1, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                if (fragment1 != null) {
                    fragment1.advicesAllRead();
                }
                if (fragment2 != null) {
                    fragment2.advicesAllRead();
                }
                //点击全部标记已读（活动消息）
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_ACTIVITY_MSG_ALL_READ);
                //点击全部标记已读（系统消息）
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_MSG_ALL_READ);
                break;
            default:
                break;
        }
    }
}