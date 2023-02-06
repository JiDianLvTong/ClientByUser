package com.android.jidian.repair.mvp.UserMyPatrol.patrolList;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.UserMyPatrol.patrolDetail.MyPatrolDetailActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.OnClick;

public class UserMyPatrolActivity extends BaseActivityByMvp<UserMyPatrolPresenter> implements UserMyPatrolContract.View {

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.srl_my_time_task)
    public SmartRefreshLayout srlMyTimeTask;
    @BindView(R.id.rv_my_time_task)
    public RecyclerView rvMyTimeTask;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;

    private UserMyPatrolAdapter mAdapter;
    private int mPage = 1;
    private String mLng, mLat;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_my_task;
    }

    @Override
    public void initView() {
        mPresenter = new UserMyPatrolPresenter();
        mPresenter.attachView(this);
        tvTitle.setText("已巡检列表");
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(this);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlMyTimeTask.setRefreshHeader(materialHeader);
        srlMyTimeTask.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(this);
        srlMyTimeTask.setRefreshFooter(classicsFooter);
        rvMyTimeTask.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserMyPatrolAdapter();
        rvMyTimeTask.setAdapter(mAdapter);
        mAdapter.setListener(new UserMyPatrolAdapter.OnItemViewClickListener() {
//            @Override
//            public void OnClickGuide(PatrolMyListBean.DataBean.ListsBean bean) {
//                MapUtil.showNavigationDialog(UserMyPatrolActivity.this,"",bean.g);
//            }

            @Override
            public void OnClickItem(PatrolMyListBean.DataBean.ListsBean bean) {
                Intent intent = new Intent(UserMyPatrolActivity.this, MyPatrolDetailActivity.class);
                intent.putExtra("id", bean.getId());
                startActivity(intent);
            }
        });
        srlMyTimeTask.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        srlMyTimeTask.autoRefresh();
    }

    private void requestData() {
        if (mPresenter != null) {
            mPresenter.requestPatrolMylists(mLng, mLat, mPage + "");
        }
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    @Override
    public void requestPatrolMylistsSuccess(PatrolMyListBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().getLists().size() > 0) {
                rvMyTimeTask.setVisibility(View.VISIBLE);
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
        srlMyTimeTask.finishRefresh();
        srlMyTimeTask.finishLoadMore();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
        srlMyTimeTask.finishRefresh();
        srlMyTimeTask.finishLoadMore();
    }

    private void dataNull() {
        srlMyTimeTask.finishRefresh();
        srlMyTimeTask.finishLoadMore();
        if (mPage == 1) {
            rvMyTimeTask.setVisibility(View.GONE);
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