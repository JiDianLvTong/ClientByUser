package com.android.jidian.repair.mvp.UserTask.UserTaskList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.UserMyPatrol.patrolList.UserMyPatrolAdapter;
import com.android.jidian.repair.mvp.main.TimeLimitTaskFragment.TimeLimitTabkAdapter;
import com.android.jidian.repair.mvp.main.TimeLimitTaskFragment.WorktaskListsBean;
import com.android.jidian.repair.mvp.task.TimeTaskDetailActivity;
import com.android.jidian.repair.utils.MapUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.OnClick;

public class UserTaskListActivity extends BaseActivityByMvp<UserTaskListPresenter> implements UserTaskListContract.View {

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.srl_my_task)
    public SmartRefreshLayout srlMyTask;
    @BindView(R.id.rv_my_task)
    public RecyclerView rvMyTask;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;

    private UserTaskListAdapter mAdapter;
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_task_list;
    }

    @Override
    public void initView() {
        mPresenter = new UserTaskListPresenter();
        mPresenter.attachView(this);
        tvTitle.setText("即时任务列表");
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(this);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlMyTask.setRefreshHeader(materialHeader);
        srlMyTask.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(this);
        srlMyTask.setRefreshFooter(classicsFooter);
        rvMyTask.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserTaskListAdapter();
        rvMyTask.setAdapter(mAdapter);
        mAdapter.setListener(new UserTaskListAdapter.OnItemViewClickListener() {
            @Override
            public void OnClickGuide(WorktaskMylistsBean.DataBean.ListsBean bean) {
                MapUtil.showNavigationDialog(UserTaskListActivity.this, "目的地", bean.getLat(), bean.getLng());
            }

            @Override
            public void OnClickPhone(WorktaskMylistsBean.DataBean.ListsBean bean) {
                if (!TextUtils.isEmpty(bean.getPhone())) {
                    Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getPhone()));
                    startActivity(Intent);
                }
            }

            @Override
            public void OnClickItem(WorktaskMylistsBean.DataBean.ListsBean bean) {
                Intent intent = new Intent(UserTaskListActivity.this, TimeTaskDetailActivity.class);
                intent.putExtra("wtid", bean.getId());
                startActivity(intent);
            }
        });
        srlMyTask.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                requestData();
            }
        });
        srlMyTask.autoRefresh();
    }

    private void requestData() {
        if (mPresenter != null) {
            mPresenter.requestWorktaskMylists(mPage + "");
        }
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    @Override
    public void requestWorktaskMylistsSuccess(WorktaskMylistsBean bean) {
        if (bean.getData().getLists() != null) {
            if (bean.getData().getLists().size() > 0) {
                rvMyTask.setVisibility(View.VISIBLE);
                nullDataPanel.setVisibility(View.GONE);
                if (mPage == 1) {
                    mAdapter.setNewData(bean.getData().getLists());
                } else {
                    mAdapter.addData(bean.getData().getLists());
                }
                mPage++;
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
        srlMyTask.finishRefresh();
        srlMyTask.finishLoadMore();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
        srlMyTask.finishRefresh();
        srlMyTask.finishLoadMore();
    }

    private void dataNull() {
        srlMyTask.finishRefresh();
        srlMyTask.finishLoadMore();
        if (mPage == 1) {
            rvMyTask.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
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
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
        dataNull();
    }
}