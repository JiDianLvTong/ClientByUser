package com.android.jidian.repair.mvp.task;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;

import butterknife.BindView;

public class TimeTaskDetailActivity extends BaseActivityByMvp<TimeTaskDetailPresenter> implements TimeTaskDetailContract.View {

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

    private String mWtid = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_time_task_detail;
    }

    @Override
    public void initView() {
        mWtid = getIntent().getStringExtra("wtid");
        mPresenter = new TimeTaskDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.requestWorktaskDetail(mWtid);

    }

    @Override
    public void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean) {
        if ("10".equals(bean.getWtype())) {//电柜故障
            tvTaskItemType.setBackgroundResource(R.drawable.shape_de0b15_corner_20);
            tvTaskItemNum.setText("电柜编号:" + bean.getCabid());
            tvTaskItemAddress.setVisibility(View.VISIBLE);
            tvTaskItemAddress.setText("地址：" + bean.getAddress());
            tvTestItemContent.setText(bean.getContent());
        } else if ("20".equals(bean.getWtype())) {//救援任务
            tvTaskItemType.setBackgroundResource(R.drawable.shape_64b22a_corner_20);
            tvTaskItemNum.setText("电柜编号:" + bean.getPhone());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
        } else {//其他任务
            tvTaskItemType.setBackgroundResource(R.drawable.shape_64b22a_corner_20);
            tvTaskItemNum.setText("电柜编号:" + bean.getTitle());
            tvTaskItemAddress.setVisibility(View.GONE);
            tvTestItemContent.setText(bean.getContent());
        }
        if ("1".equals(bean.getUstatus())) {// 1 == 已完成  2 == 待完成  -1 == 未完成 -2 == 无法完成
            llNoSalve.setVisibility(View.GONE);
            llHasSalve.setVisibility(View.VISIBLE);
        } else if ("2".equals(bean.getUstatus())) {
            llNoSalve.setVisibility(View.VISIBLE);
            llHasSalve.setVisibility(View.VISIBLE);
        } else if ("-1".equals(bean.getUstatus())) {
            llHasSalve.setVisibility(View.GONE);

        } else if ("-2".equals(bean.getUstatus())) {
            llHasSalve.setVisibility(View.GONE);

        }
    }

    @Override
    public void requestWorktaskDetailFail(String msg) {
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