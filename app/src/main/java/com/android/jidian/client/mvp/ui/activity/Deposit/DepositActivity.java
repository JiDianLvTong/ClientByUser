package com.android.jidian.client.mvp.ui.activity.Deposit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.DepositRefundOrderBean;
import com.android.jidian.client.mvp.contract.DepositContract;
import com.android.jidian.client.mvp.presenter.DepositPresenter;
import com.android.jidian.client.mvp.ui.adapter.DepositListAdapter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

public class DepositActivity extends U6BaseActivityByMvp<DepositPresenter> implements DepositContract.View {
    @BindView(R.id.tv_deposit_tips)
    public TextView tv_deposit_tips;
    @BindView(R.id.srl_deposit)
    public SmartRefreshLayout srl_deposit;
    @BindView(R.id.rv_deposit)
    public RecyclerView rv_deposit;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;
    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;

    private DepositListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_deposit);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new DepositPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(DepositActivity.this);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srl_deposit.setRefreshHeader(materialHeader);
        srl_deposit.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(DepositActivity.this);
        srl_deposit.setRefreshFooter(classicsFooter);
        rv_deposit.setLayoutManager(new LinearLayoutManager(DepositActivity.this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DepositListAdapter();
        rv_deposit.setAdapter(mAdapter);
        srl_deposit.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }
        });
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    private void requestData() {
        mPresenter.requestDepositRefundOrder(uid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        srl_deposit.autoRefresh();
    }

    @Override
    public void requestDepositRefundOrderSuccess(DepositRefundOrderBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().size() > 0) {
                srl_deposit.finishRefresh();
                srl_deposit.setVisibility(View.VISIBLE);
                nullDataPanel.setVisibility(View.GONE);
                mAdapter.setNewData(bean.getData());
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
    }

    @Override
    public void requestDepositRefundOrderFail(String msg) {
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
        srl_deposit.finishRefresh();
        srl_deposit.setVisibility(View.GONE);
        nullDataPanel.setVisibility(View.VISIBLE);
    }
}