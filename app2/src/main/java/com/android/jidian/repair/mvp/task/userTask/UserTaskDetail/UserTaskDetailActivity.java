package com.android.jidian.repair.mvp.task.userTask.UserTaskDetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.task.TimeTaskDetailActivity;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.bumptech.glide.Glide;

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

    @BindView(R.id.ll_no_salve)
    public LinearLayout llNoSalve;
    @BindView(R.id.et_task_solve)
    public EditText etTaskSolve;
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
    @BindView(R.id.btn_task_solve)
    public TextView btnTaskSolve;

    private String wtId = "";
    private String[] mSolveImageList = new String[]{"", "", "", ""};

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
                tvTaskItemType.setText("电柜故障: 未完成");
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
                tvTaskItemType.setText("救援任务: 未完成");
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
                tvTaskItemType.setText("其他任务: 未完成");
                tvTaskItemType.setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
            }
        }
        if (bean.getImages().size() > 0) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg1);
        }
        if (bean.getImages().size() > 1) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg2);
        }
        if (bean.getImages().size() > 2) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg3);
        }
        if (bean.getImages().size() > 3) {
            Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskFailureImg4);
        }

//        if (!"1".equals(bean.getUstatus())){
//            llNoSalve.setVisibility(View.VISIBLE);
//        }
    }

    @OnClick(R.id.pageReturn)
    public void OnClickPageReturn() {
        finish();
    }


    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @OnClick(R.id.iv_task_solve_img_1)
    public void OnClickivTaskSolveImg1() {
        PictureSelectorUtils.addPhotoByCameraAndAlbum(UserTaskDetailActivity.this, ADD_SOLVE_IMG_1);
    }

    @OnClick(R.id.iv_task_solve_img_2)
    public void OnClickivTaskSolveImg2() {
        PictureSelectorUtils.addPhotoByCameraAndAlbum(UserTaskDetailActivity.this, ADD_SOLVE_IMG_2);
    }

    @OnClick(R.id.iv_task_solve_img_3)
    public void OnClickivTaskSolveImg3() {
        PictureSelectorUtils.addPhotoByCameraAndAlbum(UserTaskDetailActivity.this, ADD_SOLVE_IMG_3);
    }

    @OnClick(R.id.iv_task_solve_img_4)
    public void OnClickivTaskSolveImg4() {
        PictureSelectorUtils.addPhotoByCameraAndAlbum(UserTaskDetailActivity.this, ADD_SOLVE_IMG_4);
    }

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

        //todo:: 待完成任务 需要完成
//        mPresenter.requestWorktaskResolve(wtId, "1", etTaskSolve.getText().toString().trim(),
//                mSolveImageList[0], mSolveImageList[1], mSolveImageList[2], mSolveImageList[3]);
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