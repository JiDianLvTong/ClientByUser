package com.android.jidian.repair.mvp.task.userTask.UserTaskDetail;

import static com.android.jidian.repair.utils.picture.PictureUtil.calculateInSampleSize;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.task.TimeTaskDetailActivity;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.picture.BitmapManager;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class UserTaskDetailActivity extends BaseActivityByMvp<UserTaskDetailPresenter> implements UserTaskDetailContract.View {

    //上传图片类型
    private static final int ADD_SOLVE_IMG_1 = 101;
    private static final int ADD_SOLVE_IMG_2 = 102;
    private static final int ADD_SOLVE_IMG_3 = 103;
    private static final int ADD_SOLVE_IMG_4 = 104;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.ll_task_top)
    public LinearLayout llTaskTop;
    @BindView(R.id.tv_task_item_date)
    public TextView tvTaskItemDate;
    @BindView(R.id.tv_task_item_type)
    public TextView tvTaskItemType;
    @BindView(R.id.tv_task_item_num)
    public TextView tvTaskItemNum;
    @BindView(R.id.tv_task_item_guide)
    public ImageView tvTaskItemGuide;
    @BindView(R.id.tv_task_item_address)
    public TextView tvTaskItemAddress;
    @BindView(R.id.tv_test_item_content)
    public TextView tvTestItemContent;
    @BindView(R.id.iv_task_failure_img_1)
    public ImageView ivTaskFailureImg1;
    @BindView(R.id.iv_task_failure_img_2)
    public ImageView ivTaskFailureImg2;
    @BindView(R.id.iv_task_failure_img_3)
    public ImageView ivTaskFailureImg3;
    @BindView(R.id.iv_task_failure_img_4)
    public ImageView ivTaskFailureImg4;

    @BindView(R.id.ll_has_salve)
    public LinearLayout llHasSalve;
    @BindView(R.id.et_task_has_solve)
    public TextView etTaskHasSolve;
    @BindView(R.id.tv_task_has_item_date)
    public TextView tvTaskHasItemDate;
    @BindView(R.id.iv_task_has_solve_img_1)
    public ImageView ivTaskHasSolveImg1;
    @BindView(R.id.iv_task_has_solve_img_2)
    public ImageView ivTaskHasSolveImg2;
    @BindView(R.id.iv_task_has_solve_img_3)
    public ImageView ivTaskHasSolveImg3;
    @BindView(R.id.iv_task_has_solve_img_4)
    public ImageView ivTaskHasSolveImg4;

    @BindView(R.id.ll_not_salve)
    public LinearLayout llNotSalve;
    @BindView(R.id.et_task_not_solve)
    public TextView etTaskNotSolve;
    @BindView(R.id.tv_task_not_item_date)
    public TextView tvTaskNotItemDate;
    @BindView(R.id.iv_task_not_solve_img_1)
    public ImageView ivTaskNotSolveImg1;
    @BindView(R.id.iv_task_not_solve_img_2)
    public ImageView ivTaskNotSolveImg2;
    @BindView(R.id.iv_task_not_solve_img_3)
    public ImageView ivTaskNotSolveImg3;
    @BindView(R.id.iv_task_not_solve_img_4)
    public ImageView ivTaskNotSolveImg4;

    @BindView(R.id.ll_re_salve)
    public LinearLayout llReSalve;
    @BindView(R.id.et_re_task_solve)
    public TextView etReTaskSolve;
    @BindView(R.id.iv_re_task_solve_img_1)
    public ImageView ivReTaskSolveImg1;
    @BindView(R.id.iv_re_task_solve_img_2)
    public ImageView ivReTaskSolveImg2;
    @BindView(R.id.iv_re_task_solve_img_3)
    public ImageView ivReTaskSolveImg3;
    @BindView(R.id.iv_re_task_solve_img_4)
    public ImageView ivReTaskSolveImg4;
    @BindView(R.id.btn_re_task_solve)
    public TextView btnReTaskSolve;



    private String wtId = "";
    private String[] mSolveImageList = new String[]{"", "", "", ""};
    private String mPath, mProj, mUpToken, mCompanyid;
    private String mlng, mlat;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_task_detail;
    }

    @Override
    public void initView() {
        wtId = getIntent().getStringExtra("wtid");
        mPresenter = new UserTaskDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.requestWorktaskDetail(wtId);
    }

    @Override
    public void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean) {
        llTaskTop.setVisibility(View.VISIBLE);
        if ("10".equals(bean.getWtype())) {//电柜故障
            tvTaskItemNum.setText("电柜编号:" + bean.getCabid());
            tvTaskItemAddress.setVisibility(View.VISIBLE);
            tvTaskItemAddress.setText("地址：" + bean.getAddress());
            tvTestItemContent.setText(bean.getContent());
            if ("1".equals(bean.getUstatus())) {
                tvTaskItemType.setText("电柜故障: 已完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_d7a64a_corner_top_right_10);
            }else{
                tvTaskItemType.setText("电柜故障: 无法完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
            }
        } else if ("20".equals(bean.getWtype())) {//救援任务
            tvTaskItemNum.setText("手机号码:" + bean.getPhone());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTaskItemGuide.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
            if ("1".equals(bean.getUstatus())) {
                tvTaskItemType.setText("救援任务: 已完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_d7a64a_corner_top_right_10);
            }else{
                tvTaskItemType.setText("救援任务: 无法完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
            }
        } else {//其他任务
            tvTaskItemNum.setText(bean.getTitle());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTaskItemGuide.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
            if ("1".equals(bean.getUstatus())) {
                tvTaskItemType.setText("其他任务: 已完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_d7a64a_corner_top_right_10);
            }else{
                tvTaskItemType.setText("其他任务: 无法完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
            }
        }

        tvTaskItemDate.setText("发布时间: " + bean.getCreate_time());

        if (bean.getImages().size() > 0) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg1);
        }
        if (bean.getImages().size() > 1) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(1)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg2);
        }
        if (bean.getImages().size() > 2) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(2)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg3);
        }
        if (bean.getImages().size() > 3) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(3)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg4);
        }

        if ("1".equals(bean.getUstatus())){
            llHasSalve.setVisibility(View.VISIBLE);
            etTaskHasSolve.setText(bean.getContentdw());
            tvTaskHasItemDate.setText("处理时间: " + bean.getTimedw());
            if (bean.getImages().size() > 0) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskHasSolveImg1);
            }
            if (bean.getImages().size() > 1) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(1)).placeholder(R.drawable.icon_image_broken).into(ivTaskHasSolveImg2);
            }
            if (bean.getImages().size() > 2) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(2)).placeholder(R.drawable.icon_image_broken).into(ivTaskHasSolveImg3);
            }
            if (bean.getImages().size() > 3) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(3)).placeholder(R.drawable.icon_image_broken).into(ivTaskHasSolveImg4);
            }
        }else{
            llNotSalve.setVisibility(View.VISIBLE);
            llReSalve.setVisibility(View.VISIBLE);
            etTaskNotSolve.setText(bean.getContentdw());
            tvTaskNotItemDate.setText("处理时间: " + bean.getTimedw());
            if (bean.getImages().size() > 0) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskNotSolveImg1);
            }
            if (bean.getImages().size() > 1) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(1)).placeholder(R.drawable.icon_image_broken).into(ivTaskNotSolveImg2);
            }
            if (bean.getImages().size() > 2) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(2)).placeholder(R.drawable.icon_image_broken).into(ivTaskNotSolveImg3);
            }
            if (bean.getImages().size() > 3) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImagedw().get(3)).placeholder(R.drawable.icon_image_broken).into(ivTaskNotSolveImg4);
            }
        }
    }

    @OnClick(R.id.iv_re_task_solve_img_1)
    public void OnClickIvReTaskSolveImg1() {
        PictureSelectorUtils.addPhotoByCamera(activity, ADD_SOLVE_IMG_1);
    }

    @OnClick(R.id.iv_re_task_solve_img_2)
    public void OnClickIvReTaskSolveImg2() {
        PictureSelectorUtils.addPhotoByCamera(activity, ADD_SOLVE_IMG_2);
    }

    @OnClick(R.id.iv_re_task_solve_img_3)
    public void OnClickIvReTaskSolveImg3() {
        PictureSelectorUtils.addPhotoByCamera(activity, ADD_SOLVE_IMG_3);
    }

    @OnClick(R.id.iv_re_task_solve_img_4)
    public void OnClickIvReTaskSolveImg4() {
        PictureSelectorUtils.addPhotoByCamera(activity, ADD_SOLVE_IMG_4);
    }

    @Override
    public void requestWorktaskDetailFail(String msg) {
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
    public void requestUpLoadImgSuccess(UploadImageBean bean, int index) {
        if (bean.getData() != null) {
            if (ADD_SOLVE_IMG_1 == index) {
                mSolveImageList[0] = bean.getData().getFurl();
                Glide.with(activity).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivReTaskSolveImg1);
            } else if (ADD_SOLVE_IMG_2 == index) {
                mSolveImageList[1] = bean.getData().getFurl();
                Glide.with(activity).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivReTaskSolveImg2);
            } else if (ADD_SOLVE_IMG_3 == index) {
                mSolveImageList[2] = bean.getData().getFurl();
                Glide.with(activity).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivReTaskSolveImg3);
            } else if (ADD_SOLVE_IMG_4 == index) {
                mSolveImageList[3] = bean.getData().getFurl();
                Glide.with(activity).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivReTaskSolveImg4);
            }
        } else {
            showMessage(bean.getMsg());
        }
    }

    @OnClick(R.id.btn_re_task_solve)
    public void OnClickBtnReTaskSolve() {
        if (TextUtils.isEmpty(etReTaskSolve.getText().toString().trim())) {
            showMessage("请输入解决内容");
            return;
        }
        if (TextUtils.isEmpty(mSolveImageList[0])) {
            showMessage("设备图不能为空");
            return;
        }
        if (TextUtils.isEmpty(mSolveImageList[1])) {
            showMessage("合影图不能为空");
            return;
        }
        if (TextUtils.isEmpty(mSolveImageList[2])) {
            showMessage("关键位置图不能为空");
            return;
        }
        mPresenter.requestWorktaskResolve(wtId, "1", etReTaskSolve.getText().toString().trim(),
                mSolveImageList[0], mSolveImageList[1], mSolveImageList[2], mSolveImageList[3]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bitmap = (Bitmap) extras.get("data");
                String filePath = "";
                if (Build.VERSION.SDK_INT >= 29) {
                    filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg" ;
                }else {
                    filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                }
                BitmapManager.saveBitmapFile(new File(filePath), bitmap);
                if (mPresenter != null) {
                    if (!TextUtils.isEmpty(mPath)) {
                        mPresenter.requestUpLoadImg(mPath, filePath, mUpToken, mCompanyid, requestCode);
                    } else {
                        showMessage("出错了，请重新选择~");
                        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
                    }
                }
            } else {
                Uri uri = data.getData();
                String filePath = getRealPathFromURI(uri);
                //图片上传，需要压缩一下
                int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
                //首先使用inJustDecodeBounds = true进行解码以检查尺寸
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                //计算inSampleSize
                options.inSampleSize = calculateInSampleSize(options, requestWidth, requestWidth);
                //使用inSampleSize集解码位图
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

                String bitmapFilePath = "";
                if (Build.VERSION.SDK_INT >= 29) {
                    bitmapFilePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg" ;
                }else {
                    bitmapFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                }
                BitmapManager.saveBitmapFile(new File(bitmapFilePath), bitmap);
                if (mPresenter != null) {
                    if (!TextUtils.isEmpty(mPath)) {
                        mPresenter.requestUpLoadImg(mPath, bitmapFilePath, mUpToken, mCompanyid, requestCode);
                    } else {
                        showMessage("出错了，请重新选择~");
                        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
                    }
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @OnClick(R.id.pageReturn)
    public void OnClickPageReturn() {
        finish();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void requestWorktaskResolveSuccess(BaseBean bean) {
        showMessage(bean.getMsg());
        finish();
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
}