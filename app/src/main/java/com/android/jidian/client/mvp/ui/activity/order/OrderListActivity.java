package com.android.jidian.client.mvp.ui.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.client.MainShopOrder_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.UserUOrderBean;
import com.android.jidian.client.mvp.contract.OrderListContract;
import com.android.jidian.client.mvp.presenter.OrderListPresenter;
import com.android.jidian.client.mvp.ui.activity.pay.PayByConfirmOrder;
import com.android.jidian.client.mvp.ui.adapter.OrderListAdapter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


import butterknife.BindView;
import butterknife.OnClick;

public class OrderListActivity extends U6BaseActivityByMvp<OrderListPresenter> implements OrderListContract.View {

    @BindView(R.id.srl_order_list)
    SmartRefreshLayout srl_order_list;
    @BindView(R.id.rv_order_list)
    RecyclerView rv_order_list;
    @BindView(R.id.nullDataPanel)
    LinearLayout nullDataPanel;
    @BindView(R.id.pageReturn)
    LinearLayout pageReturn;

    private String mLastid;
    private OrderListAdapter mAdapter;
    private boolean mIsRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_order_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new OrderListPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(OrderListActivity.this);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srl_order_list.setRefreshHeader(materialHeader);
        srl_order_list.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(OrderListActivity.this);
        srl_order_list.setRefreshFooter(classicsFooter);
        rv_order_list.setLayoutManager(new LinearLayoutManager(OrderListActivity.this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderListAdapter();
        mAdapter.setListener(new OrderListAdapter.OnClickItemViewListener() {
            @Override
            public void OnClickItem(UserUOrderBean.DataBean.ListsBean bean) {
                if ("待支付".equals(bean.getStatus_desc())) {
                    //点击待支付订单
                    Intent intent = new Intent(activity, PayByConfirmOrder.class);
                    intent.putExtra("orderno", bean.getOrder_num());
                    intent.putExtra("fee", bean.getOrder_fee());
                    startActivity(intent);
                }
            }
        });
        rv_order_list.setAdapter(mAdapter);
        srl_order_list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData(true);
            }
        });
        srl_order_list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(false);
            }
        });
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        activity.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData(true);
    }

    private void requestData(boolean isRefresh) {
        mIsRefresh = isRefresh;
        if (isRefresh) {
            mLastid = "";
        }
        if (mPresenter != null) {
            mPresenter.requestUserUOrder(uid, TextUtils.isEmpty(mLastid) ? null : mLastid);
        }
    }

    @Override
    public void requestUserUOrderSuccess(UserUOrderBean bean) {
        if (bean.getData() != null) {
            srl_order_list.setVisibility(View.VISIBLE);
            nullDataPanel.setVisibility(View.GONE);
            mLastid = bean.getData().getLastid();
            if (bean.getData().getLists() != null) {
                if (mIsRefresh) {
                    mAdapter.setNewData(bean.getData().getLists());
                } else {
                    mAdapter.addData(bean.getData().getLists());
                }
            }
        } else {
            dataNull();
        }
        srl_order_list.finishRefresh();
        srl_order_list.finishLoadMore();
    }

    @Override
    public void requestUserUOrderFail(String msg) {
        srl_order_list.finishRefresh();
        srl_order_list.finishLoadMore();
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
        srl_order_list.finishRefresh();
        srl_order_list.finishLoadMore();
        if (mIsRefresh) {
            srl_order_list.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
        }
    }
}