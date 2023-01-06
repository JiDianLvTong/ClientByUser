package com.android.jidian.client.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.UserUOrderBean;
import com.android.jidian.client.mvp.contract.OrderListContract;
import com.android.jidian.client.mvp.presenter.OrderListPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

public class OrderListActivity extends U6BaseActivityByMvp<OrderListPresenter> implements OrderListContract.View {

    @BindView(R.id.srl_order_list)
    SmartRefreshLayout srl_order_list;
    @BindView(R.id.rv_order_list)
    RecyclerView rv_order_list;
    @BindView(R.id.nullDataPanel)
    LinearLayout nullDataPanel;

    private String mLastid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u6_activity_order_list);
    }

    @Override
    public void initView() {
        mPresenter = new OrderListPresenter();
        mPresenter.attachView(this);
        rv_order_list.setLayoutManager(new LinearLayoutManager(OrderListActivity.this, LinearLayoutManager.VERTICAL, false));

        srl_order_list.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mLastid = "";
                requestData();
            }
        });
        requestData();
    }

    private void requestData() {
        if (mPresenter != null) {
            mPresenter.requestUserUOrder(uid, mLastid.isEmpty() ? null : mLastid);
        }
    }

    @Override
    public void requestUserUOrderSuccess(UserUOrderBean bean) {
        if (bean.getData() != null) {
            srl_order_list.setVisibility(View.VISIBLE);
            nullDataPanel.setVisibility(View.GONE);
            mLastid = bean.getData().getLastid();
        } else {
            dataNull();
        }
    }

    @Override
    public void requestUserUOrderFail(String msg) {
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

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
        dataNull();
    }

    private void dataNull() {
        srl_order_list.setVisibility(View.GONE);
        nullDataPanel.setVisibility(View.VISIBLE);
    }
}