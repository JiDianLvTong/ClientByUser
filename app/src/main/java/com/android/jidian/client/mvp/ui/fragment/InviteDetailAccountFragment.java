package com.android.jidian.client.mvp.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.PullActivityInviteListsBean;
import com.android.jidian.client.mvp.contract.InviteDetailContract;
import com.android.jidian.client.mvp.presenter.InviteDetailPresenter;
import com.android.jidian.client.mvp.ui.adapter.InviteDetailAccountAdapter;
import com.android.jidian.client.pub.PubFunction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteDetailAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteDetailAccountFragment extends BaseFragment<InviteDetailPresenter> implements InviteDetailContract.View {

    private int mPageNum = 1;
    private SmartRefreshLayout srlFragmentInviteDetail;
    private RecyclerView rvFragmentInviteDetail;
    private LinearLayout nonePanel;
    private boolean isRefresh;
    private String mAid;
    private InviteDetailAccountAdapter mAdapter;
    private List<PullActivityInviteListsBean.DataBean.ListBean> mList = new ArrayList<>();
    private OnFragmentRefreshListener mListener;

    public static InviteDetailAccountFragment newInstance() {
        return new InviteDetailAccountFragment();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_invite_detail, container, false);
//    }

    public void setRefresh(String aid, OnFragmentRefreshListener listener) {
        mAid = aid;
        mListener = listener;
        if (srlFragmentInviteDetail != null) {
            srlFragmentInviteDetail.autoRefresh();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invite_detail;
    }

    @Override
    public void initView(View view) {
        srlFragmentInviteDetail = view.findViewById(R.id.srl_fragment_invite_detail);
        rvFragmentInviteDetail = view.findViewById(R.id.rv_fragment_invite_detail);
        nonePanel = view.findViewById(R.id.none_panel);
        mAdapter = new InviteDetailAccountAdapter(requireContext());
        rvFragmentInviteDetail.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        rvFragmentInviteDetail.setAdapter(mAdapter);
        mPresenter = new InviteDetailPresenter();
        mPresenter.attachView(this);
        srlFragmentInviteDetail.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData(true);
            }
        });
    }

    private void requestData(boolean refresh) {
        if (!PubFunction.isConnect(requireActivity(), true)) {
            showMessage("无网络链接，请检查您的网络设置！");
            stop();
            return;
        }
        if (mPresenter != null) {
            if (refresh) {
                isRefresh = true;
                mPageNum = 1;
            }
            mPresenter.requestPullActivityInviteLists(mAid, "1", mPageNum + "");
        }
    }

    private void stop() {
        if (srlFragmentInviteDetail != null) {
            srlFragmentInviteDetail.finishRefresh();
            srlFragmentInviteDetail.finishLoadMore();
        }
    }

    @Override
    public void showProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
        stop();
    }

    @Override
    public void requestPullActivityInviteListsSuccess(PullActivityInviteListsBean bean) {
        if (bean.getData().getTotal() != null) {
            if (mListener != null) {
                mListener.onRefresh(bean.getData().getTotal().getNum(), bean.getData().getTotal().getProfit());
            }
        }
        if (bean.getData().getList() != null) {
            if (bean.getData().getList().size() > 0) {
//                mAdapter.setTotalLength(0);
//                mAdapter.addData(list);
                if (isRefresh) {
                    mList.clear();
                    mList = bean.getData().getList();
                } else {
                    mList.addAll(bean.getData().getList());
                }
                mAdapter.setLists(mList);
                mAdapter.notifyDataSetChanged();
                nonePanel.setVisibility(View.GONE);
                rvFragmentInviteDetail.setVisibility(View.VISIBLE);
                srlFragmentInviteDetail.setEnableLoadMore(true);
                mPageNum++;
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
        stop();
        isRefresh = false;
    }

    public interface OnFragmentRefreshListener {
        void onRefresh(String num, String profit);
    }

    private void dataNull() {
        if (mPageNum > 1) {
//            mAdapter.setTotalLength(mAdapter.getData().size());
//            mAdapter.notifyDataSetChanged();
            srlFragmentInviteDetail.setEnableLoadMore(false);
        } else {
            showNodata();
        }
    }

    private void showNodata() {
        if (mPageNum == 1) {
            nonePanel.setVisibility(View.VISIBLE);
            rvFragmentInviteDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
        stop();
    }
}