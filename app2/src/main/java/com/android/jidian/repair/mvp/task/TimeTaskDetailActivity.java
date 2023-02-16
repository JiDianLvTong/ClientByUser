package com.android.jidian.repair.mvp.task;

import static com.android.jidian.repair.utils.picture.PictureUtil.calculateInSampleSize;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.utils.MapUtil;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.picture.BitmapManager;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class TimeTaskDetailActivity extends BaseActivityByMvp<TimeTaskDetailPresenter> implements TimeTaskDetailContract.View {

    //上传图片类型
    private static final int ADD_SOLVE_IMG_1 = 101;
    private static final int ADD_SOLVE_IMG_2 = 102;
    private static final int ADD_SOLVE_IMG_3 = 103;
    private static final int ADD_SOLVE_IMG_4 = 104;

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.ll_task_top)
    public LinearLayout llTaskTop;
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
    @BindView(R.id.ll_no_salve)
    public LinearLayout llNoSalve;
    @BindView(R.id.et_task_solve)
    public EditText etTaskSolve;
    @BindView(R.id.btn_task_solve)
    public TextView btnTaskSolve;
    @BindView(R.id.btn_task_cant_solve)
    public TextView btnTaskCantSolve;
    @BindView(R.id.ll_task_failure_image)
    public LinearLayout llTaskFailureImage;
    @BindView(R.id.ll_has_salve)
    public LinearLayout llHasSalve;
    @BindView(R.id.iv_task_failure_img_1)
    public ImageView ivTaskFailureImg1;
    @BindView(R.id.iv_task_failure_img_2)
    public ImageView ivTaskFailureImg2;
    @BindView(R.id.iv_task_failure_img_3)
    public ImageView ivTaskFailureImg3;
    @BindView(R.id.iv_task_failure_img_4)
    public ImageView ivTaskFailureImg4;
    @BindView(R.id.ll_task_solve_image)
    public LinearLayout llTaskSolveImage;
    @BindView(R.id.iv_task_solve_img_1)
    public ImageView ivTaskSolveImg1;
    @BindView(R.id.iv_task_solve_img_2)
    public ImageView ivTaskSolveImg2;
    @BindView(R.id.iv_task_solve_img_3)
    public ImageView ivTaskSolveImg3;
    @BindView(R.id.iv_task_solve_img_4)
    public ImageView ivTaskSolveImg4;
    @BindView(R.id.ll_task_has_solve_image)
    public LinearLayout llTaskHasSolveImage;
    @BindView(R.id.iv_task_has_solve_img_1)
    public ImageView ivTaskHasSolveImg1;
    @BindView(R.id.iv_task_has_solve_img_2)
    public ImageView ivTaskHasSolveImg2;
    @BindView(R.id.iv_task_has_solve_img_3)
    public ImageView ivTaskHasSolveImg3;
    @BindView(R.id.iv_task_has_solve_img_4)
    public ImageView ivTaskHasSolveImg4;
    @BindView(R.id.tv_task_content)
    public TextView tvTaskContent;

    @BindView(R.id.tv_task_item_date)
    public TextView tvTaskItemDate;

    private String mWtid = "";
    private String mPath, mProj, mUpToken, mCompanyid;
    private String mlng, mlat;
    private String[] mSolveImageList = new String[]{"", "", "", ""};

//    private String wtid, ustat, content, img1, img2, img3, img4;

    @Override
    public int getLayoutId() {
        return R.layout.activity_time_task_detail;
    }

    @Override
    public void initView() {
        mWtid = getIntent().getStringExtra("wtid");
        mPresenter = new TimeTaskDetailPresenter();
        mPresenter.attachView(this);
        if (UserInfoSp.getInstance().isLogin()) {
            mPresenter.requestWorktaskDetail(mWtid);
        }
        tvTitle.setText("任务详情");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(mProj)) {
            mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
        }
    }

    /**
     * wtid,任务id
     * ustat,处理状态：-2=无法完成，1=已完成。
     * content,处理结果描述
     * img1,设备图
     * img2,合影图
     * img3,关建位置图
     * img4,其他图（可选）
     *
     * @return
     */
    @OnClick(R.id.btn_task_solve)
    public void OnClickbtnTaskSolve() {
        if (TextUtils.isEmpty(etTaskSolve.getText().toString().trim())) {
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
        mPresenter.requestWorktaskResolve(mWtid, "1", etTaskSolve.getText().toString().trim(),
                mSolveImageList[0], mSolveImageList[1], mSolveImageList[2], mSolveImageList[3]);
    }

    @OnClick(R.id.btn_task_cant_solve)
    public void OnClickbtnTaskCantSolve() {
        if (TextUtils.isEmpty(etTaskSolve.getText().toString().trim())) {
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
        mPresenter.requestWorktaskResolve(mWtid, "-2", etTaskSolve.getText().toString().trim(),
                mSolveImageList[0], mSolveImageList[1], mSolveImageList[2], mSolveImageList[3]);
    }

    @OnClick(R.id.tv_task_item_guide)
    public void OnClicktvTaskItemGuide() {
        MapUtil.showNavigationDialog(TimeTaskDetailActivity.this, "", mlat, mlng);
    }

    @OnClick(R.id.iv_task_solve_img_1)
    public void OnClickivTaskSolveImg1() {
        PictureSelectorUtils.addPhotoByCamera(TimeTaskDetailActivity.this, ADD_SOLVE_IMG_1);
    }

    @OnClick(R.id.iv_task_solve_img_2)
    public void OnClickivTaskSolveImg2() {
        PictureSelectorUtils.addPhotoByCamera(TimeTaskDetailActivity.this, ADD_SOLVE_IMG_2);
    }

    @OnClick(R.id.iv_task_solve_img_3)
    public void OnClickivTaskSolveImg3() {
        PictureSelectorUtils.addPhotoByCamera(TimeTaskDetailActivity.this, ADD_SOLVE_IMG_3);
    }

    @OnClick(R.id.iv_task_solve_img_4)
    public void OnClickivTaskSolveImg4() {
        PictureSelectorUtils.addPhotoByCamera(TimeTaskDetailActivity.this, ADD_SOLVE_IMG_4);
    }

    @OnClick(R.id.pageReturn)
    public void OnClickPageReturn() {
        finish();
    }

    @Override
    public void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean) {
        mlat = bean.getLat();
        mlng = bean.getLng();
        if ("10".equals(bean.getWtype())) {//电柜故障
            tvTaskItemType.setText("电柜故障");
            tvTaskItemNum.setText("电柜编号:" + bean.getCabid());
            tvTaskItemAddress.setVisibility(View.VISIBLE);
            tvTaskItemAddress.setText("地址：" + bean.getAddress());
            tvTestItemContent.setText(bean.getContent());
            tvTaskItemGuide.setVisibility(View.VISIBLE);
        } else if ("20".equals(bean.getWtype())) {//救援任务
            tvTaskItemType.setText("救援任务");
            tvTaskItemNum.setText("手机号码:" + bean.getPhone());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTaskItemGuide.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
        } else {//其他任务
            tvTaskItemType.setText("其他任务");
            tvTaskItemNum.setText(bean.getTitle());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTaskItemGuide.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
        }

        tvTaskItemDate.setText("发布时间: " + bean.getCreate_time());


        int imageCount = 0;
        for(int i = 0 ; i < bean.getImages().size() ; i ++){
            if(bean.getImages().get(i).equals("")){
                imageCount = imageCount + 1;
            }
        }
        if(imageCount == 4){
            llTaskFailureImage.setVisibility(View.GONE);
        }

        if (bean.getImages().size() > 0) {
            Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(0)).into(ivTaskFailureImg1);
        }
        if (bean.getImages().size() > 1) {
            Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(1)).into(ivTaskFailureImg2);
        }
        if (bean.getImages().size() > 2) {
            Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(2)).into(ivTaskFailureImg3);
        }
        if (bean.getImages().size() > 3) {
            Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(3)).into(ivTaskFailureImg4);
        }
        if ("1".equals(bean.getUstatus())) {// 1 == 已完成   2 == 待完成
            llNoSalve.setVisibility(View.GONE);
            llHasSalve.setVisibility(View.VISIBLE);
            tvTaskContent.setText(bean.getContent());
            if (bean.getImages().size() > 0) {
                Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg1);
            }
            if (bean.getImages().size() > 1) {
                Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(1)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg2);
            }
            if (bean.getImages().size() > 2) {
                Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(2)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg3);
            }
            if (bean.getImages().size() > 3) {
                Glide.with(TimeTaskDetailActivity.this).load(bean.getImages().get(3)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg4);
            }
        } else if ("2".equals(bean.getUstatus())) {
            llNoSalve.setVisibility(View.VISIBLE);
            llHasSalve.setVisibility(View.GONE);
        }
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
                Glide.with(TimeTaskDetailActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg1);
            } else if (ADD_SOLVE_IMG_2 == index) {
                mSolveImageList[1] = bean.getData().getFurl();
                Glide.with(TimeTaskDetailActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg2);
            } else if (ADD_SOLVE_IMG_3 == index) {
                mSolveImageList[2] = bean.getData().getFurl();
                Glide.with(TimeTaskDetailActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg3);
            } else if (ADD_SOLVE_IMG_4 == index) {
                mSolveImageList[3] = bean.getData().getFurl();
                Glide.with(TimeTaskDetailActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg4);
            }
        } else {
            showMessage(bean.getMsg());
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bitmap = (Bitmap) extras.get("data");
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
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
                String bitmapFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
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
//            Bitmap bitmap = (Bitmap) extras.get("data");
//            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
//            BitmapManager.saveBitmapFile(new File(filePath), bitmap);
//            if (mPresenter != null) {
//                if (!TextUtils.isEmpty(mPath)) {
//                    mPresenter.requestUpLoadImg(mPath, filePath, mUpToken, mCompanyid, requestCode);
//                } else {
//                    showMessage("出错了，请重新选择~");
//                    mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
//                }
//            }
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