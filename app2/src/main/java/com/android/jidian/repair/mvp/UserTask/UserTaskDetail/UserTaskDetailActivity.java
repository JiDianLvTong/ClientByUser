package com.android.jidian.repair.mvp.UserTask.UserTaskDetail;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;

public class UserTaskDetailActivity extends BaseActivityByMvp<UserTaskDetailPresenter> implements UserTaskDetailContract.View {

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.ll_task_top)
    public LinearLayout llTaskTop;
    @BindView(R.id.tv_task_item_type)
    public TextView tvTaskItemType;
    @BindView(R.id.tv_task_item_num)
    public TextView tvTaskItemNum;
    @BindView(R.id.tv_task_item_guide)
    public TextView tvTaskItemGuide;
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

    private String mLng, mLat;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_task_detail;
    }

    @Override
    public void initView() {
        String wtid = getIntent().getStringExtra("id");
        mPresenter = new UserTaskDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.requestWorktaskDetail(wtid);
    }

    @Override
    public void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean) {
        mLat = bean.getLat();
        mLng = bean.getLng();
        if ("10".equals(bean.getWtype())) {//电柜故障
            tvTaskItemNum.setText("电柜编号:" + bean.getCabid());
            tvTaskItemAddress.setVisibility(View.VISIBLE);
            tvTaskItemAddress.setText("地址：" + bean.getAddress());
            tvTestItemContent.setText(bean.getContent());
        } else if ("20".equals(bean.getWtype())) {//救援任务
            tvTaskItemNum.setText("手机号码:" + bean.getPhone());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTaskItemGuide.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
        } else {//其他任务
            tvTaskItemNum.setText(bean.getTitle());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTaskItemGuide.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
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
        if ("1".equals(bean.getUstatus())) {// 1 == 已完成   2 == 待完成
            llNoSalve.setVisibility(View.GONE);
            llHasSalve.setVisibility(View.VISIBLE);
            tvTaskContent.setText(bean.getContent());
            if (bean.getImages().size() > 0) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg1);
            }
            if (bean.getImages().size() > 1) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg2);
            }
            if (bean.getImages().size() > 2) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg3);
            }
            if (bean.getImages().size() > 3) {
                Glide.with(UserTaskDetailActivity.this).load(bean.getImages().get(0)).placeholder(R.drawable.icon_image_broken).into(ivTaskSolveImg4);
            }
        }
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