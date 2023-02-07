package com.android.jidian.repair.mvp.UserAuditCab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.main.PatrolFragment.PartolAdapter;
import com.android.jidian.repair.mvp.main.PatrolFragment.PatrolIndexBean;
import com.android.jidian.repair.mvp.patrol.PatrolAddActivity;
import com.android.jidian.repair.utils.MapUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.OnClick;

public class UserAuditCabActivity extends BaseActivityByMvp<UserAuditCabPresenter> implements UserAuditCabContract.View {

    @BindView(R.id.tv_title)
    public TextView tvTitle;
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
        tvTitle.setText("待审核电柜");
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
//        mAdapter.setListener(new PartolAdapter.OnItemViewClickListener() {
//            @Override
//            public void OnClickGuide(PatrolIndexBean.DataBean.ListsBean bean) {
//                MapUtil.showNavigationDialog(UserAuditCabActivity.this, "", bean.getWeidu(), bean.getJingdu());
//            }
//
//            @Override
//            public void OnClickItem(PatrolIndexBean.DataBean.ListsBean bean) {
//                Intent intent = new Intent(UserAuditCabActivity.this, PatrolAddActivity.class);
//                intent.putExtra("name", bean.getName());
//                intent.putExtra("address", bean.getAddress());
//                intent.putExtra("id", bean.getId());
//                startActivity(intent);
//            }
//        });
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

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }
}