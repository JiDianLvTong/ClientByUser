package com.android.jidian.repair.mvp.patrol;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.FailureAdd.FailureAddActivity;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.bumptech.glide.Glide;
import com.itheima.roundedimageview.RoundedImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PatrolAddActivity extends BaseActivityByMvp<PatrolAddPresenter> implements PatrolAddContract.View {

    //上传图片类型
    private static final int PATROL_IMG_PUNCH = 1001;
    private static final int PATROL_IMG_SCREEN_CLEAN = 1002;
    private static final int PATROL_IMG_BACK_LOCK = 1003;
    private static final int PATROL_IMG_OUTSIDE_CLEAN_1 = 1004;
    private static final int PATROL_IMG_OUTSIDE_CLEAN_2 = 1005;
    private static final int PATROL_IMG_OUTSIDE_CLEAN_3 = 1006;
    private static final int PATROL_IMG_OUTSIDE_CLEAN_4 = 1007;
    private static final int PATROL_IMG_INSIDE_CLIEN_1 = 1008;
    private static final int PATROL_IMG_INSIDE_CLIEN_2 = 1009;
    private static final int PATROL_IMG_INSIDE_CLIEN_3 = 1010;
    private static final int PATROL_IMG_ELECTRICITY = 1011;

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

    /**
     * cabid,电柜编号
     * img1,打卡合影
     * img2,屏幕清洁
     * img3,后门锁状态，
     * img6_1,是否漏电检测笔图
     * <p>
     * img4_1,柜体前侧清洁
     * img4_2,柜体左则清洁
     * img4_3,柜体右则清洁
     * img4_4,柜体顶部清洁
     * img5_1,柜体内上部清洁
     * img5_2,柜体内中部清洁
     * img5_3,柜体内下部清洁
     * <p>
     * isnetdbm, 网络信号：1=有，0=无
     * isdixian, 电柜地线：1=有，0=无
     * isopenbtn,开仓正常：1=是，0=否
     */
    private String[] imgListSingle = new String[]{"", "", "", "", ""};
    private String[] imgListGroup = new String[]{"", "", "", "", "", "", ""};
    private String[] radioList = new String[]{"1", "1", "1"};
    private String mCabid;
    private String mPath, mCompanyid, mProj, mUpToken;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_add;
    }

    @Override
    public void initView() {
        mCabid = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        if (name.length() > 14) {
            tvTitle.setTextSize(12);
        }
        tvTitle.setText(name);
        tvPatrolDetailAddress.setText(address);
        mPresenter = new PatrolAddPresenter();
        mPresenter.attachView(this);
        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
    }

    @OnClick(R.id.btn_patrol_add)
    public void onClickBtnPatrolAdd() {
        Intent intent = new Intent(PatrolAddActivity.this, FailureAddActivity.class);
        intent.putExtra("id", mCabid);
        startActivity(intent);
    }

    //img1,打卡合影
    @OnClick(R.id.iv_punch_img)
    public void onClickivPunchImg() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_PUNCH);
    }

    //img2,屏幕清洁
    @OnClick(R.id.iv_screen_clean_img)
    public void onClickivScreenCleanImg() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_SCREEN_CLEAN);
    }

    //img3,后门锁状态，
    @OnClick(R.id.iv_back_lock_img)
    public void onClickivBackLockImg() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_BACK_LOCK);
    }

    //img4_1,柜体前侧清洁
    @OnClick(R.id.iv_outside_clean_img_1)
    public void onClickivOutsideCleanImg1() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_OUTSIDE_CLEAN_1);
    }

    //img4_2,柜体左则清洁
    @OnClick(R.id.iv_outside_clean_img_2)
    public void onClickivOutsideCleanImg2() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_OUTSIDE_CLEAN_2);
    }

    //img4_3,柜体右则清洁
    @OnClick(R.id.iv_outside_clean_img_3)
    public void onClickivOutsideCleanImg3() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_OUTSIDE_CLEAN_3);
    }

    //img4_4,柜体顶部清洁
    @OnClick(R.id.iv_outside_clean_img_4)
    public void onClickivOutsideCleanImg4() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_OUTSIDE_CLEAN_4);
    }

    //img5_1,柜体内上部清洁
    @OnClick(R.id.iv_inside_clean_img_1)
    public void onClickivInsideCleanImg1() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_INSIDE_CLIEN_1);
    }

    //img5_2,柜体内中部清洁
    @OnClick(R.id.iv_inside_clean_img_2)
    public void onClickivInsideCleanImg2() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_INSIDE_CLIEN_2);
    }

    //img5_3,柜体内下部清洁
    @OnClick(R.id.iv_inside_clean_img_3)
    public void onClickivInsideCleanImg3() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_INSIDE_CLIEN_3);
    }

    //img6_1,是否漏电检测笔图
    @OnClick(R.id.iv_electricity_img)
    public void onClickivElectricityImg() {
        PictureSelectorUtils.addPhotoByCamera(PatrolAddActivity.this, PATROL_IMG_ELECTRICITY);
    }

    //isnetdbm, 网络信号：1=有，0=无
    @OnClick(R.id.ll_network_check_enable)
    public void onClickllNetworkCheckEnable() {
        tvBgNetworkCheckEnable.setBackgroundResource(R.drawable.icon_radio_check);
        tvBgNetworkCheckUnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
        radioList[0] = "1";
    }

    //isnetdbm, 网络信号：1=有，0=无
    @OnClick(R.id.ll_network_check_unable)
    public void onClickllNetworkCheckUnable() {
        tvBgNetworkCheckEnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
        tvBgNetworkCheckUnable.setBackgroundResource(R.drawable.icon_radio_check);
        radioList[0] = "0";
    }

    //isdixian, 电柜地线：1=有，0=无
    @OnClick(R.id.ll_safe_check_enable)
    public void onClickllSafeCheckEnable() {
        tvBgSafeCheckEnable.setBackgroundResource(R.drawable.icon_radio_check);
        tvBgSafeCheckUnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
        radioList[1] = "1";
    }

    //isdixian, 电柜地线：1=有，0=无
    @OnClick(R.id.ll_safe_check_unable)
    public void onClickllSafeCheckUnable() {
        tvBgSafeCheckEnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
        tvBgSafeCheckUnable.setBackgroundResource(R.drawable.icon_radio_check);
        radioList[1] = "0";
    }

    //isopenbtn,开仓正常：1=是，0=否
    @OnClick(R.id.ll_button_check_enable)
    public void onClickllButtonCheckEnable() {
        tvBgButtonCheckEnable.setBackgroundResource(R.drawable.icon_radio_check);
        tvBgButtonCheckUnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
        radioList[2] = "1";
    }

    //isopenbtn,开仓正常：1=是，0=否
    @OnClick(R.id.ll_button_check_unable)
    public void onClickllButtonCheckUnable() {
        tvBgButtonCheckEnable.setBackgroundResource(R.drawable.icon_radio_uncheck);
        tvBgButtonCheckUnable.setBackgroundResource(R.drawable.icon_radio_check);
        radioList[2] = "0";
    }

    @OnClick(R.id.btn_patrol_submit)
    public void onClickbtnPatrolSubmit() {
        if (TextUtils.isEmpty(imgListSingle[0])) {
            showMessage("打卡照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListSingle[1])) {
            showMessage("屏幕清洁照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListSingle[2])) {
            showMessage("后门锁状态照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListSingle[3])) {
            showMessage("漏电检查照片不能为空");
            return;
        }

        if (TextUtils.isEmpty(imgListGroup[0])) {
            showMessage("柜体外部清洁/整理1照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListGroup[1])) {
            showMessage("柜体外部清洁/整理2照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListGroup[2])) {
            showMessage("柜体外部清洁/整理3照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListGroup[3])) {
            showMessage("柜体外部清洁/整理4照片不能为空");
            return;
        }

        if (TextUtils.isEmpty(imgListGroup[4])) {
            showMessage("柜体内部清洁/整理1照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListGroup[5])) {
            showMessage("柜体内部清洁/整2照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgListGroup[6])) {
            showMessage("柜体内部清洁/整理3照片不能为空");
            return;
        }
        mPresenter.requestPatrolAddpatrol(mCabid, imgListSingle[0], imgListSingle[1], imgListSingle[2],
                imgListGroup[0], imgListGroup[1], imgListGroup[2], imgListGroup[3],
                imgListGroup[4], imgListGroup[5], imgListGroup[6],
                imgListSingle[3],
                radioList[0], radioList[1], radioList[2]);
    }

    @Override
    public void requestUpLoadImgSuccess(UploadImageBean bean, int index) {
        if (bean.getData() != null) {
            switch (index) {
                case PATROL_IMG_PUNCH://img1,打卡合影
                    imgListSingle[0] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivPunchImg);
                    break;
                case PATROL_IMG_SCREEN_CLEAN://img2,屏幕清洁
                    imgListSingle[1] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivScreenCleanImg);
                    break;
                case PATROL_IMG_BACK_LOCK://img3,后门锁状态，
                    imgListSingle[2] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivBackLockImg);
                    break;
                case PATROL_IMG_ELECTRICITY://img6_1,是否漏电检测笔图
                    imgListSingle[3] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivElectricityImg);
                    break;
                case PATROL_IMG_OUTSIDE_CLEAN_1://img4_1,柜体前侧清洁
                    imgListGroup[0] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivOutsideCleanImg1);
                    break;
                case PATROL_IMG_OUTSIDE_CLEAN_2://img4_2,柜体左则清洁
                    imgListGroup[1] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivOutsideCleanImg2);
                    break;
                case PATROL_IMG_OUTSIDE_CLEAN_3://img4_3,柜体右则清洁
                    imgListGroup[2] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivOutsideCleanImg3);
                    break;
                case PATROL_IMG_OUTSIDE_CLEAN_4://img4_4,柜体顶部清洁
                    imgListGroup[3] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivOutsideCleanImg4);
                    break;

                case PATROL_IMG_INSIDE_CLIEN_1://img5_1,柜体内上部清洁
                    imgListGroup[4] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivInsideCleanImg1);
                    break;
                case PATROL_IMG_INSIDE_CLIEN_2://img5_2,柜体内中部清洁
                    imgListGroup[5] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivInsideCleanImg2);
                    break;
                case PATROL_IMG_INSIDE_CLIEN_3://img5_3,柜体内下部清洁
                    imgListGroup[6] = bean.getData().getFurl();
                    Glide.with(PatrolAddActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivInsideCleanImg3);
                    break;
            }
        } else {
            showMessage(bean.getMsg());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    if (media != null) {
                        String compressPath = media.getCompressPath();
                        if (mPresenter != null) {
                            if (!TextUtils.isEmpty(mPath)) {
                                mPresenter.requestUpLoadImg(mPath, compressPath, mUpToken, mCompanyid, requestCode);
                            } else {
                                showMessage("出错了，请重新选择~");
                                mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
                            }
                        }
                    }
                }
            }
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
    public void requestPatrolAddpatrolSuccess(BaseBean bean) {
        showMessage(bean.getMsg());
        finish();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void requestUploadUploadUrlSetSuccess(UploadUploadUrlSetBean.DataBean bean) {
        PubFunction.upload = bean.getDomain();
        mPath = bean.getPath();
        mCompanyid = bean.getCompanyid();
        mProj = bean.getProj();
        mUpToken = bean.getUpToken();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        activity.finish();
    }
}