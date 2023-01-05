package com.android.jidian.client.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.client.R;
import com.android.jidian.client.mvp.ui.activity.AdvicesType2Activity;
import com.android.jidian.client.adapter.AdvicesListsAdapter;
import com.android.jidian.client.bean.AdvicesAllReadBean;
import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.bean.AdvicesReadBean;
import com.android.jidian.client.mvp.contract.AdvicesLists2Contract;
import com.android.jidian.client.mvp.presenter.AdvicesLists2Presenter;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.android.jidian.client.mvp.ui.activity.AdvicesType2Activity.DATABEAN;
import static com.android.jidian.client.mvp.ui.activity.AdvicesType2Activity.DATABEANBUNDLE;

/**
 * 活动消息
 */
public class AdvicesListsFragment2 extends BaseFragment<AdvicesLists2Presenter> implements AdvicesLists2Contract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.fragment_advices_lists2_recyclerView)
    RecyclerView fragmentAdvicesLists2RecyclerView;
    @BindView(R.id.fragment_advices_lists2_linearLayout1)
    LinearLayout fragmentAdvicesLists2LinearLayout1;
    @BindView(R.id.fragment_advices_lists2_smartRefreshLayout)
    SmartRefreshLayout fragmentAdvicesLists2SmartRefreshLayout;

    private int page = 1;
    private boolean isRefresh = false;
    private boolean isFirstIn = true;
    private List<AdvicesLists2Bean.DataBean> dataList = new ArrayList<>();
    private AdvicesListsAdapter adapter;

    public static AdvicesListsFragment2 newInstance(String param1, String param2) {
        AdvicesListsFragment2 fragment = new AdvicesListsFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_advices_lists2;
    }

    @Override
    public void initView(View view) {
        mPresenter = new AdvicesLists2Presenter();
        mPresenter.attachView(this);
        //活动消息
        mPresenter.advicesListsV2(2, 1);
        fragmentAdvicesLists2SmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                mPresenter.advicesListsV2(2, page);
            }
        });
        fragmentAdvicesLists2SmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                mPresenter.advicesListsV2(2, page);
            }
        });
        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.recyclerview_decoration)));
        fragmentAdvicesLists2RecyclerView.addItemDecoration(divider);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //消息中心-活动消息页面访问
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_ACTIVITY_MSG);
                    }
                }, 500);
        }
    }

    @Override
    public void onAdvicesListsV2Success(AdvicesLists2Bean bean) {
        if (isRefresh) {
            dataList = new ArrayList<>();
            fragmentAdvicesLists2RecyclerView.scrollToPosition(0);
        }
        if (bean.getStatus() == 1) {
            List<AdvicesLists2Bean.DataBean> dataBeans = bean.getData();
            int itemCount = dataBeans == null ? 0 : dataBeans.size();
            if (itemCount > 0) {
                dataList.addAll(dataBeans);
            }
            if (dataList.size() > 0) {
                fragmentAdvicesLists2LinearLayout1.setVisibility(View.GONE);
                fragmentAdvicesLists2RecyclerView.setVisibility(View.VISIBLE);
                if (isFirstIn) {
                    fragmentAdvicesLists2RecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    adapter = new AdvicesListsAdapter(requireContext(), dataList);
                    adapter.setOnClickListener(new AdvicesListsAdapter.onClickListener() {
                        @Override
                        public void onItemViewClick(View itemView) {
                            //点击具体活动消息
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_ACTIVITY_MSG_ITEM);
                            AdvicesLists2Bean.DataBean dataBean = (AdvicesLists2Bean.DataBean) itemView.getTag(R.id.view_holder1);
                            if (dataBean != null) {
                                if ("0".equals(dataBean.getIs_read())) {
                                    if (mPresenter != null) {
                                        mPresenter.advicesRead(Integer.parseInt(dataBean.getMsg_id()));
                                    }
                                }
                                Intent intent = new Intent(requireContext(), AdvicesType2Activity.class);
                                Bundle args = new Bundle();
                                args.putSerializable(DATABEAN, dataBean);
                                intent.putExtra(DATABEANBUNDLE, args);
                                startActivity(intent);
                            }
                        }
                    });
                    fragmentAdvicesLists2RecyclerView.setAdapter(adapter);
                    isFirstIn = false;
                } else {
                    adapter.setList(dataList);
                    adapter.notifyDataSetChanged();
                }
            } else {
                fragmentAdvicesLists2LinearLayout1.setVisibility(View.VISIBLE);
                fragmentAdvicesLists2RecyclerView.setVisibility(View.GONE);
            }
            fragmentAdvicesLists2SmartRefreshLayout.finishRefresh(true);
            fragmentAdvicesLists2SmartRefreshLayout.finishLoadMore(0, true, false);
        } else {
            if (bean.getError() == 1 || bean.getError() == 2) {
                //两种情况：第一页无数据，及最后上拉加载最后一页无数据
                //如果是请求第一页无数据，清空界面
                if (page == 1 && dataList.isEmpty()) {
                    if (adapter != null) {
                        adapter.setList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    fragmentAdvicesLists2LinearLayout1.setVisibility(View.VISIBLE);
                    fragmentAdvicesLists2RecyclerView.setVisibility(View.GONE);
                } else {//否则是上啦加载页，不能清空数据
                    showMessage(bean.getMsg());
                    fragmentAdvicesLists2SmartRefreshLayout.finishLoadMore(0, true, false);
                }
            } else {
                fragmentAdvicesLists2LinearLayout1.setVisibility(View.VISIBLE);
                fragmentAdvicesLists2RecyclerView.setVisibility(View.GONE);
                fragmentAdvicesLists2SmartRefreshLayout.finishLoadMore(false);
            }
            fragmentAdvicesLists2SmartRefreshLayout.finishRefresh(false);
        }
        isRefresh = false;
    }

    @Override
    public void onAdvicesListsV2Error(Throwable throwable) {
        onError(throwable);
    }

    @Override
    public void onAdvicesReadSuccess(AdvicesReadBean advicesReadBean) {
    }

    @Override
    public void onAdvicesReadError(Throwable throwable) {
        onError(throwable);
    }

    @Override
    public void onAdvicesAllReadSuccess(AdvicesAllReadBean advicesAllReadBean) {
        fragmentAdvicesLists2SmartRefreshLayout.autoRefresh();
    }

    @Override
    public void onAdvicesAllReadError(Throwable throwable) {
        onError(throwable);
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
        fragmentAdvicesLists2LinearLayout1.setVisibility(View.VISIBLE);
        fragmentAdvicesLists2RecyclerView.setVisibility(View.GONE);
        fragmentAdvicesLists2SmartRefreshLayout.finishRefresh(false);
        fragmentAdvicesLists2SmartRefreshLayout.finishLoadMore(false);
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //onNetConnected
                showMessage(throwable.toString());
            } else {
                //onNetDisconnected
                showMessage("无网络连接，请检查您的网络设置！");
            }
        }
    }

    public void advicesAllRead() {
        mPresenter.advicesAllRead(2);
    }
}