package com.android.jidian.client.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.R;
import com.android.jidian.client.mvp.ui.adapter.EvaluationListsActivity_RecyclerView_Adapter;
import com.android.jidian.client.bean.EvaluateListsBean;
import com.android.jidian.client.mvp.contract.EvaluationListsContract;
import com.android.jidian.client.mvp.presenter.EvaluationListsPresenter;
import com.android.jidian.client.util.BuryingPointManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EvaluationListsActivity extends U6BaseActivityByMvp<EvaluationListsPresenter> implements EvaluationListsContract.View {
    @BindView(R.id.activity_evaluation_textView1)
    TextView activityEvaluationTextView1;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int cabid;
    private String name = "";//电柜名称
    private String repair = "";
    private int page = 1;
    private boolean isRefresh = false;
    private boolean isFirstIn = true;
    List<EvaluateListsBean.DataBean> dataList = new ArrayList<>();
    private EvaluationListsActivity_RecyclerView_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_evaluation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        cabid = intent.getIntExtra("cabid", -1);
        name = intent.getStringExtra("name");
        repair = intent.getStringExtra("repair");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //站点评论页访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_SITE_REVIEWS);
            }
        }, 500);
        activityEvaluationTextView1.setText(name);//设置默认title，展示电柜名称
        mPresenter = new EvaluationListsPresenter();
        mPresenter.attachView(this);
        mPresenter.evaluateLists(cabid, page, repair);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            isRefresh = true;
            page = 1;
            mPresenter.evaluateLists(cabid, page, repair);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            mPresenter.evaluateLists(cabid, page, repair);
        });
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.recyclerview_decoration)));
        recyclerView.addItemDecoration(divider);
    }

    @Override
    public void onSuccess(EvaluateListsBean bean) {
        //如果是下拉刷新后请求的网络，更新数据源：切勿clear否则刷新期间点击会空指针
        if (isRefresh) {
            dataList = new ArrayList<>();
        }
        textView1.setText(getResources().getString(R.string.no_evaluation));
        textView2.setVisibility(View.GONE);
        String error = bean.getError();
        String msg = bean.getMsg();
        if (bean.getStatus() == 1) {
            //设置title
            String title = bean.getCabname() + "(" + bean.getTotal() + ")";
            activityEvaluationTextView1.setText(title);
            //positionStart和itemCount用来加载更多数据，adapter的数据range范围通知
            int positionStart = dataList.size();
            List<EvaluateListsBean.DataBean> dataBeans = bean.getData();
            int itemCount = dataBeans == null ? 0 : dataBeans.size();
            if (itemCount > 0) {
                dataList.addAll(dataBeans);
            }
            if (dataList.size() > 0) {
                textView1.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (isFirstIn) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    //只有首次进入页面，才去创建adapter，之后的数据更新只notify一下即可
                    adapter = new EvaluationListsActivity_RecyclerView_Adapter(this, dataList);
                    recyclerView.setAdapter(adapter);
                    isFirstIn = false;
                } else {
                    adapter.setData(dataList);
                    if (isRefresh) {
                        //刷新后重设dataList
                        adapter.notifyDataSetChanged();
                    } else {
                        //非刷新：加载更多，通知新增数据的范围，提升性能
                        adapter.notifyItemRangeInserted(positionStart, itemCount);
                    }
                }
            } else {
                textView1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            refreshLayout.finishRefresh(true);
            refreshLayout.finishLoadMore(0, true, false);
        } else {
            if (error.equals("E200")) {//E200有两种情况：第一页无数据，及最后上拉加载最后一页无数据
                if (page == 1 && dataList.isEmpty()) {//如果是请求第一页无数据，清空界面
                    if (adapter != null) {
                        adapter.setData(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    activityEvaluationTextView1.setText(name);//设置默认title，展示电柜名称
                    textView1.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.finishLoadMore(false);
                } else {//否则是上啦加载页，不能清空数据
                    showMessage(msg);
                    refreshLayout.finishLoadMore(0, true, false);
                }
            } else {
                textView1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                refreshLayout.finishLoadMore(false);
            }
            refreshLayout.finishRefresh(false);
        }
        isRefresh = false;
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
        textView1.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        refreshLayout.finishRefresh(false);
        refreshLayout.finishLoadMore(false);
//        showMessage(throwable.getLocalizedMessage());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {//onNetConnected
                textView1.setText(getResources().getString(R.string.no_evaluation));
                textView2.setVisibility(View.GONE);
            } else {//onNetDisconnected
                textView1.setText("网络已断开，点击刷新重试~");
                textView2.setVisibility(View.VISIBLE);
                textView2.setOnClickListener(v -> {
                    isRefresh = true;
                    page = 1;
                    mPresenter.evaluateLists(cabid, page, repair);
                });
            }
        }
    }

    @OnClick(R.id.page_return)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.linearLayout2)
    public void onViewClicked2() {
        //点击评价按钮
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SITE_REVIEWS_EVALUATE);
        Intent intent = new Intent(EvaluationListsActivity.this, EvaluateAddsActivity.class);
        intent.putExtra("repair", repair);
        intent.putExtra("cabid", cabid);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}