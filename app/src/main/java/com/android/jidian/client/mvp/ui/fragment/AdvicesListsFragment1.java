package com.android.jidian.client.mvp.ui.fragment;

import android.content.Intent;
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
import com.android.jidian.client.mvp.contract.AdvicesLists1Contract;
import com.android.jidian.client.mvp.presenter.AdvicesLists1Presenter;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.CalcUtils;
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
 * 系统消息
 */
public class AdvicesListsFragment1 extends BaseFragment<AdvicesLists1Presenter> implements AdvicesLists1Contract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.fragment_advices_lists1_recyclerView)
    RecyclerView fragmentAdvicesLists1RecyclerView;
    @BindView(R.id.fragment_advices_lists1_linearLayout1)
    LinearLayout fragmentAdvicesLists1LinearLayout1;
    @BindView(R.id.fragment_advices_lists1_smartRefreshLayout)
    SmartRefreshLayout fragmentAdvicesLists1SmartRefreshLayout;

    private int page = 1;
    private boolean isRefresh = false;
    private boolean isFirstIn = true;
    private List<AdvicesLists2Bean.DataBean> dataList = new ArrayList<>();
    private AdvicesListsAdapter adapter;

    public AdvicesListsFragment1() {
    }

    public static AdvicesListsFragment1 newInstance(String param1, String param2) {
        AdvicesListsFragment1 fragment = new AdvicesListsFragment1();
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
        return R.layout.fragment_advices_lists1;
    }

    @Override
    public void initView(View view) {
        mPresenter = new AdvicesLists1Presenter();
        mPresenter.attachView(this);
        mPresenter.advicesListsV2(0, 1);
        //系统消息
        fragmentAdvicesLists1SmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                mPresenter.advicesListsV2(0, page);
            }
        });
        fragmentAdvicesLists1SmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                mPresenter.advicesListsV2(0, page);
            }
        });
        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.recyclerview_decoration)));
        fragmentAdvicesLists1RecyclerView.addItemDecoration(divider);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //消息中心-系统消息页面访问
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_SYSTEM_MSG);
                }
            }, 500);
        }
    }

    @Override
    public void onAdvicesListsV2Success(AdvicesLists2Bean bean) {
        if (isRefresh) {
            dataList = new ArrayList<>();
            fragmentAdvicesLists1RecyclerView.scrollToPosition(0);
        }
        if (bean.getStatus() == 1) {
            List<AdvicesLists2Bean.DataBean> dataBeans = bean.getData();
            int itemCount = dataBeans == null ? 0 : dataBeans.size();
            if (itemCount > 0) {
                dataList.addAll(dataBeans);
            }
            if (dataList.size() > 0) {
                fragmentAdvicesLists1LinearLayout1.setVisibility(View.GONE);
                fragmentAdvicesLists1RecyclerView.setVisibility(View.VISIBLE);
                if (isFirstIn) {
                    fragmentAdvicesLists1RecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    adapter = new AdvicesListsAdapter(requireContext(), dataList);
                    adapter.setOnClickListener(new AdvicesListsAdapter.onClickListener() {
                        @Override
                        public void onItemViewClick(View itemView) {
                            if (CalcUtils.isFastDoubleClick()) {
                                return;
                            }
                            //点击具体系统消息
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_MSG_ITEM);
                            AdvicesLists2Bean.DataBean dataBean = (AdvicesLists2Bean.DataBean) itemView.getTag(R.id.view_holder1);
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
                    });
                    fragmentAdvicesLists1RecyclerView.setAdapter(adapter);
                    isFirstIn = false;
                } else {
                    adapter.setList(dataList);
                    adapter.notifyDataSetChanged();
                }
            } else {
                fragmentAdvicesLists1LinearLayout1.setVisibility(View.VISIBLE);
                fragmentAdvicesLists1RecyclerView.setVisibility(View.GONE);
            }
            fragmentAdvicesLists1SmartRefreshLayout.finishRefresh(true);
            fragmentAdvicesLists1SmartRefreshLayout.finishLoadMore(0, true, false);
        } else {
            if (bean.getError() == 1 || bean.getError() == 2) {
                //两种情况：第一页无数据，及最后上拉加载最后一页无数据
                //如果是请求第一页无数据，清空界面
                if (page == 1 && dataList.isEmpty()) {
                    if (adapter != null) {
                        adapter.setList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    fragmentAdvicesLists1LinearLayout1.setVisibility(View.VISIBLE);
                    fragmentAdvicesLists1RecyclerView.setVisibility(View.GONE);
                } else {
                    //否则是上啦加载页，不能清空数据
                    showMessage(bean.getMsg());
                    fragmentAdvicesLists1SmartRefreshLayout.finishLoadMore(0, true, false);
                }
            } else {
                fragmentAdvicesLists1LinearLayout1.setVisibility(View.VISIBLE);
                fragmentAdvicesLists1RecyclerView.setVisibility(View.GONE);
                fragmentAdvicesLists1SmartRefreshLayout.finishLoadMore(false);
            }
            fragmentAdvicesLists1SmartRefreshLayout.finishRefresh(false);
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
        fragmentAdvicesLists1SmartRefreshLayout.autoRefresh();
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
    }

    public void advicesAllRead() {
        mPresenter.advicesAllRead(0);
    }
}