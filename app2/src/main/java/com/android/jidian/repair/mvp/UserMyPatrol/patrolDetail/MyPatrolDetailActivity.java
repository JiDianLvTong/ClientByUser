package com.android.jidian.repair.mvp.UserMyPatrol.patrolDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.bumptech.glide.Glide;
import com.itheima.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class MyPatrolDetailActivity extends BaseActivityByMvp<MyPartolDetailPresenter> implements MyPartolDetailContract.View {

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_patrol_detail_address)
    public TextView tvPatrolDetailAddress;
    @BindView(R.id.iv_punch_img)
    public RoundedImageView ivPunchImg;
    @BindView(R.id.iv_screen_clean_img)
    public RoundedImageView ivScreenCleanImg;
    @BindView(R.id.iv_back_lock_img)
    public RoundedImageView ivBackLockImg;
    @BindView(R.id.iv_outside_clean_img_1)
    public RoundedImageView ivOutsideCleanImg1;
    @BindView(R.id.iv_outside_clean_img_2)
    public RoundedImageView ivOutsideCleanImg2;
    @BindView(R.id.iv_outside_clean_img_3)
    public RoundedImageView ivOutsideCleanImg3;
    @BindView(R.id.iv_outside_clean_img_4)
    public RoundedImageView ivOutsideCleanImg4;
    @BindView(R.id.iv_inside_clean_img_1)
    public RoundedImageView ivInsideCleanImg1;
    @BindView(R.id.iv_inside_clean_img_2)
    public RoundedImageView ivInsideCleanImg2;
    @BindView(R.id.iv_inside_clean_img_3)
    public RoundedImageView ivInsideCleanImg3;
    @BindView(R.id.iv_electricity_img)
    public RoundedImageView ivElectricityImg;
    @BindView(R.id.ll_network_check_enable)
    public LinearLayout llNetworkCheckEnable;
    @BindView(R.id.ll_network_check_unable)
    public LinearLayout llNetworkCheckUnable;
    @BindView(R.id.ll_safe_check_enable)
    public LinearLayout llSafeCheckEnable;
    @BindView(R.id.ll_safe_check_unable)
    public LinearLayout llSafeCheckUnable;
    @BindView(R.id.ll_button_check_enable)
    public LinearLayout llButtonCheckEnable;
    @BindView(R.id.ll_button_check_unable)
    public LinearLayout llButtonCheckUnable;
    @BindView(R.id.tv_bg_network_check_enable)
    public TextView tvBgNetworkCheckEnable;
    @BindView(R.id.tv_bg_network_check_unable)
    public TextView tvBgNetworkCheckUnable;
    @BindView(R.id.tv_bg_safe_check_enable)
    public TextView tvBgSafeCheckEnable;
    @BindView(R.id.tv_bg_safe_check_unable)
    public TextView tvBgSafeCheckUnable;
    @BindView(R.id.tv_bg_button_check_enable)
    public TextView tvBgButtonCheckEnable;
    @BindView(R.id.tv_bg_button_check_unable)
    public TextView tvBgButtonCheckUnable;
    @BindView(R.id.btn_patrol_add)
    public TextView btnPatrolAdd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_patrol_detail;
    }

    @Override
    public void initView() {
        String id = getIntent().getStringExtra("id");
        mPresenter = new MyPartolDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.requestPatrolDetail(id);
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    @Override
    public void requestPatrolDetailSuccess(PatrolDetailBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().getName().length() > 14) {
                tvTitle.setTextSize(12);
            }
            tvTitle.setText(bean.getData().getName());
            tvPatrolDetailAddress.setText(bean.getData().getAddress());
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg1()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivPunchImg);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg2()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivScreenCleanImg);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg3()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivBackLockImg);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg4_1()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivOutsideCleanImg1);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg4_2()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivOutsideCleanImg2);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg4_3()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivOutsideCleanImg3);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg4_4()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivOutsideCleanImg4);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg5_1()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivInsideCleanImg1);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg5_2()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivInsideCleanImg2);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg5_3()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivInsideCleanImg3);
            Glide.with(MyPatrolDetailActivity.this).load(bean.getData().getImg6_1()).error(R.drawable.icon_image_broken).placeholder(R.drawable.picture_icon_placeholder).into(ivElectricityImg);
            if ("1".equals(bean.getData().getIsnetdbm())) {
                tvBgNetworkCheckEnable.setBackgroundResource(R.drawable.icon_radio_check);
                tvBgNetworkCheckUnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
            } else {
                tvBgNetworkCheckEnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
                tvBgNetworkCheckUnable.setBackgroundResource(R.drawable.icon_radio_check);
            }
            if ("1".equals(bean.getData().getIsdixian())) {
                tvBgNetworkCheckEnable.setBackgroundResource(R.drawable.icon_radio_check);
                tvBgNetworkCheckUnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
            } else {
                tvBgNetworkCheckEnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
                tvBgNetworkCheckUnable.setBackgroundResource(R.drawable.icon_radio_check);
            }
            if ("1".equals(bean.getData().getIsopenbtn())) {
                tvBgSafeCheckEnable.setBackgroundResource(R.drawable.icon_radio_check);
                tvBgSafeCheckUnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
            } else {
                tvBgSafeCheckEnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
                tvBgSafeCheckUnable.setBackgroundResource(R.drawable.icon_radio_check);
            }
        }
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void showProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }
}