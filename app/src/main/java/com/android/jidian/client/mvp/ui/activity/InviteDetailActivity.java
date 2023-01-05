package com.android.jidian.client.mvp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseActivity2;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.mvp.contract.LoginContract;
import com.android.jidian.client.mvp.presenter.LoginPresenter;
import com.android.jidian.client.mvp.ui.fragment.InviteDetailAccountFragment;
import com.android.jidian.client.mvp.ui.fragment.InviteDetailFragment;
import com.android.jidian.client.widgets.NoScrollViewPager;
import com.android.jidian.client.widgets.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class InviteDetailActivity extends BaseActivity2<LoginPresenter> implements LoginContract.View {

    private List<Fragment> mFragments = new ArrayList<>();
    private NoScrollViewPager vpContent;
    private String mAid;
    private ImageView titleLayoutImageView1;
    private TextView titleLayoutTextView1,
            tvInviteDetailAccount, tvInviteDetailAccountTitle, tvInviteDetailAccountUnit, tvInviteDetailAccountBottomLine,
            tvInviteDetailAmount, tvInviteDetailAmountUnit, tvInviteDetailAmountTitle, tvInviteDetailAmountBottomLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invite_detail);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mAid = getIntent().getStringExtra("aid");
        tvInviteDetailAccount = findViewById(R.id.tv_invite_detail_account);
        tvInviteDetailAccountTitle = findViewById(R.id.tv_invite_detail_account_title);
        tvInviteDetailAccountUnit = findViewById(R.id.tv_invite_detail_account_unit);
        tvInviteDetailAccountBottomLine = findViewById(R.id.tv_invite_detail_account_bottom_line);
        tvInviteDetailAmount = findViewById(R.id.tv_invite_detail_amount);
        tvInviteDetailAmountUnit = findViewById(R.id.tv_invite_detail_amount_unit);
        tvInviteDetailAmountTitle = findViewById(R.id.tv_invite_detail_amount_title);
        tvInviteDetailAmountBottomLine = findViewById(R.id.tv_invite_detail_amount_bottom_line);
        vpContent = findViewById(R.id.vp_content);


        findViewById(R.id.title_layout_imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title_layout_textView1)).setText("邀请明细");

        String[] mTitles = {"", ""};
        mFragments.add(InviteDetailFragment.newInstance());
        mFragments.add(InviteDetailAccountFragment.newInstance());
        vpContent.setOffscreenPageLimit(1);
        vpContent.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        vpContent.setScrollEnable(false);
        vpContent.setCurrentItem(0);
        TextView tvBgInviteDetailAccount = findViewById(R.id.tv_bg_invite_detail_account);
        tvBgInviteDetailAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTabChange(0);
            }
        });
        TextView tvBgInviteDetailAmount = findViewById(R.id.tv_bg_invite_detail_amount);
        tvBgInviteDetailAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTabChange(1);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    OnTabChange(0);
                }
            }
        }, 200);
    }

    private void OnTabChange(int position) {
        //总收益
        tvInviteDetailAmount.setTextColor(Color.parseColor(0 == position ? "#555555" : "#FD4931"));
        tvInviteDetailAmountUnit.setTextColor(Color.parseColor(0 == position ? "#555555" : "#FD4931"));
        tvInviteDetailAmountTitle.setTextColor(Color.parseColor(0 == position ? "#555555" : "#FD4931"));
        tvInviteDetailAmountBottomLine.setVisibility(0 == position ? View.GONE : View.VISIBLE);
        //总人数
        tvInviteDetailAccount.setTextColor(Color.parseColor(0 == position ? "#FD4931" : "#555555"));
        tvInviteDetailAccountTitle.setTextColor(Color.parseColor(0 == position ? "#FD4931" : "#555555"));
        tvInviteDetailAccountUnit.setTextColor(Color.parseColor(0 == position ? "#FD4931" : "#555555"));
        tvInviteDetailAccountBottomLine.setVisibility(0 == position ? View.VISIBLE : View.GONE);
        vpContent.setCurrentItem(position);
        if (position == 0) {
            ((InviteDetailFragment) mFragments.get(position)).setRefresh(mAid, new InviteDetailFragment.OnFragmentRefreshListener() {
                @Override
                public void onRefresh(String num, String profit) {
                    tvInviteDetailAccount.setText(num);
                    tvInviteDetailAmount.setText(profit);
                }
            });
        }else {
            ((InviteDetailAccountFragment) mFragments.get(position)).setRefresh(mAid, new InviteDetailAccountFragment.OnFragmentRefreshListener() {
                @Override
                public void onRefresh(String num, String profit) {
                    tvInviteDetailAccount.setText(num);
                    tvInviteDetailAmount.setText(profit);
                }
            });
        }

    }

    @Override
    public void sendVerificationCodeResult(String msg) {

    }

    @Override
    public void requestLoginSuccess(LoginBean bean) {

    }

    @Override
    public void requestLoginError(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}