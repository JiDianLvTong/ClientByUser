package com.android.jidian.repair.mvp.cabinet.cabAuditCab;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.cabinet.cabAddCab.AddCabActivity;
import com.android.jidian.repair.mvp.task.userTask.UserTaskDetail.UserTaskDetailActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.OnClick;

public class UserAuditCabActivity extends BaseActivityByMvp<UserAuditCabPresenter> implements UserAuditCabContract.View {

    @BindView(R.id.srl_audit_cab)
    public SmartRefreshLayout srlAuditCab;
    @BindView(R.id.rv_audit_cab)
    public RecyclerView rvAuditCab;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;

    private int mPage = 1;
    private UserAuditedAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_audit_cab;
    }

    @Override
    public void initView() {
        mPresenter = new UserAuditCabPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(UserAuditCabActivity.this);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlAuditCab.setRefreshHeader(materialHeader);
        srlAuditCab.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(UserAuditCabActivity.this);
        srlAuditCab.setRefreshFooter(classicsFooter);
        rvAuditCab.setLayoutManager(new LinearLayoutManager(UserAuditCabActivity.this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserAuditedAdapter();
        rvAuditCab.setAdapter(mAdapter);
        mAdapter.setListener(new UserAuditedAdapter.OnClickItemViewListener() {
            @Override
            public void onClickItem(AuditCabListBean.DataBean bean) {
                if ("2".equals(bean.getAudit_status())) {//0 = 待审核  1 = 审核通过   2= 驳回
                    Intent intent = new Intent(UserAuditCabActivity.this, AddCabActivity.class);
                    intent.putExtra("baseUrl", PubFunction.ape + "Cabinet/edit");
                    intent.putExtra("tid", bean.getId());
                    startActivity(intent);
                }
            }
        });
        srlAuditCab.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        srlAuditCab.autoRefresh();
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    private void requestData() {
        if (mPresenter != null) {
            mPresenter.requestAuditCabList(mPage + "");
        }
    }

    @Override
    public void requestAuditCabListSuccess(AuditCabListBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().size() > 0) {
                rvAuditCab.setVisibility(View.VISIBLE);
                nullDataPanel.setVisibility(View.GONE);
                if (mPage == 1) {
                    mAdapter.setNewData(bean.getData());
                } else {
                    mAdapter.addData(bean.getData());
                }
                mPage++;
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
        srlAuditCab.finishRefresh();
        srlAuditCab.finishLoadMore();
    }

    @Override
    public void requestFail(String msg) {
        showMessage(msg);
        dataNull();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    private void dataNull() {
        srlAuditCab.finishRefresh();
        srlAuditCab.finishLoadMore();
        if (1 == mPage) {
            rvAuditCab.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
        } else {

        }
    }

    @OnClick(R.id.addCab)
    public void onClickAddCab() {
        Intent intent = new Intent(activity, AddCabActivity.class);
        intent.putExtra("baseUrl", PubFunction.ape + "Cabinet/cabinet");
        startActivity(intent);
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }
}