package com.android.jidian.repair.mvp.user.userMessage;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.user.userMessage.fragment.MessageFragment;
import com.android.jidian.repair.mvp.user.userMessage.fragment.MessageFragmentEvent;
import com.android.jidian.repair.widgets.ViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserMessageActivity extends BaseActivityByMvp<UserMessagePresenter> implements UserMessageContract.View {

    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.advices_lists_tabLayout)
    SlidingTabLayout advicesListsTabLayout;
    @BindView(R.id.advices_lists_viewPager)
    ViewPager advicesListsViewPager;

    private int mCurrentIndex = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_message;
    }

    @Override
    public void initView() {
        mPresenter = new UserMessagePresenter();
        mPresenter.attachView(this);
        ((TextView) findViewById(R.id.tv_title)).setText("消息中心");
        tvTitleRight.setText("全部已读");
        String[] mTabs = new String[]{"系统消息", "活动消息"};
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(MessageFragment.newInstance("0"));
        mFragmentList.add(MessageFragment.newInstance("2"));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTabs);
        advicesListsViewPager.setAdapter(adapter);
        advicesListsViewPager.setOffscreenPageLimit(1);
        advicesListsTabLayout.setViewPager(advicesListsViewPager, mTabs);
        advicesListsViewPager.setCurrentItem(1, false);
        advicesListsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                mCurrentIndex = i;
            }
        });
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    @OnClick(R.id.tv_title_right)
    public void onClicktvTitleRight() {
        mPresenter.requestAdvicesAllRead(0 == mCurrentIndex ? "0" : "2");
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

    @Override
    public void requestAdvicesAllReadSuccess(BaseBean bean) {
        showMessage(bean.getMsg());
        EventBus.getDefault().postSticky(new MessageFragmentEvent(MessageFragmentEvent.MESSAGE_ALL_READ));
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }
}