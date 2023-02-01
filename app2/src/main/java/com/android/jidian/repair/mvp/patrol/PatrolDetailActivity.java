package com.android.jidian.repair.mvp.patrol;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BaseBean;
import com.itheima.roundedimageview.RoundedImageView;

import butterknife.BindView;

public class PatrolDetailActivity extends BaseActivityByMvp<PatrolDetailPresenter> implements PatrolDetailContract.View {

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.title_layout)
    public TextView titleLayout;
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
    @BindView(R.id.iv_inside_clean_img_1)
    public RoundedImageView ivInsideCleanImg1;
    @BindView(R.id.iv_inside_clean_img_2)
    public RoundedImageView ivInsideCleanImg2;
    @BindView(R.id.iv_inside_clean_img_3)
    public RoundedImageView ivInsideCleanImg3;
    @BindView(R.id.iv_electricity_img)
    public RoundedImageView ivElectricityImg;
    @BindView(R.id.ll_open_check_enable)
    public LinearLayout llOpenCheckEnable;
    @BindView(R.id.ll_open_check_unable)
    public LinearLayout llOpenCheckUnable;
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
    @BindView(R.id.tv_bg_open_check_enable)
    public LinearLayout tvBgOpenCheckEnable;
    @BindView(R.id.tv_bg_open_check_unable)
    public LinearLayout tvBgOpenCheckUnable;
    @BindView(R.id.tv_bg_network_check_enable)
    public LinearLayout tvBgNetworkCheckEnable;
    @BindView(R.id.tv_bg_network_check_unable)
    public LinearLayout tvBgNetworkCheckUnable;
    @BindView(R.id.tv_bg_safe_check_enable)
    public LinearLayout tvBgSafeCheckEnable;
    @BindView(R.id.tv_bg_safe_check_unable)
    public LinearLayout tvBgSafeCheckUnable;
    @BindView(R.id.tv_bg_button_check_enable)
    public LinearLayout tvBgButtonCheckEnable;
    @BindView(R.id.tv_bg_button_check_unable)
    public LinearLayout tvBgButtonCheckUnable;

    private String[] imgList = new String[]{"",};



    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_detail;
    }

    @Override
    public void initView() {

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
    public void requestPatrolAddpatrolSuccess(BaseBean bean) {
        showMessage(bean.getMsg());
        finish();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }
}