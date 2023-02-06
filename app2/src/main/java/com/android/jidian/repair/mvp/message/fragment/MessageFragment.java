package com.android.jidian.repair.mvp.message.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseFragmentByMvp;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.mvp.UserTask.UserTaskList.UserTaskListAdapter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@BindEventBus
public class MessageFragment extends BaseFragmentByMvp<MessageFragmentPresenter> implements MessageFragmentContract.View {

    @BindView(R.id.rv_fragment_advices_lists)
    RecyclerView rvAdvicesLists;
    @BindView(R.id.nullDataPanel)
    LinearLayout nullDataPanel;
    @BindView(R.id.srl_fragment_advices_lists)
    SmartRefreshLayout srlAdvicesLists;

    private static final String ARG_PARAM1 = "param1";

    private String mType;
    private int mPage = 1;
    private ActiyityMessageListAdapter mActivityAdapter;
    private SystemMessageListAdapter mSystemAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(View view) {
        Bundle getBundle = getArguments();
        if (getBundle != null) {
            mType = getBundle.getString(ARG_PARAM1, "0");
        }
//        tvEmpty.setText("暂无消息");
        mPresenter = new MessageFragmentPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(requireActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlAdvicesLists.setRefreshHeader(materialHeader);
        srlAdvicesLists.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(requireActivity());
        srlAdvicesLists.setRefreshFooter(classicsFooter);
        rvAdvicesLists.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        mActivityAdapter = new ActiyityMessageListAdapter();
        mSystemAdapter = new SystemMessageListAdapter();
        if ("0".equals(mType)) {//0 == 系统消息   2== 活动消息
            rvAdvicesLists.setAdapter(mSystemAdapter);
        } else {
            rvAdvicesLists.setAdapter(mActivityAdapter);
        }
        srlAdvicesLists.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        mPresenter.requestAdvicesLists(mType, mPage + "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageFragmentEvent event) {
        if (MessageFragmentEvent.MESSAGE_ALL_READ == event.getEvent()) {
            srlAdvicesLists.autoRefresh();
        }
    }

    private void requestData() {
        mPresenter.requestAdvicesLists(mType, mPage + "");
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

    @Override
    public void requestAdvicesListsSuccess(AdvicesListsBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().size() > 0) {
                rvAdvicesLists.setVisibility(View.GONE);
                nullDataPanel.setVisibility(View.VISIBLE);
                if ("0".equals(mType)) {//0 == 系统消息   2== 活动消息
                    if (mPage == 1) {
                        mSystemAdapter.setNewData(bean.getData());
                    } else {
                        mSystemAdapter.addData(bean.getData());
                    }
                } else {
                    if (mPage == 1) {
                        mActivityAdapter.setNewData(bean.getData());
                    } else {
                        mActivityAdapter.addData(bean.getData());
                    }
                }
                mPage++;
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
        srlAdvicesLists.finishLoadMore();
        srlAdvicesLists.finishRefresh();
    }

    private void dataNull() {
        srlAdvicesLists.finishLoadMore();
        srlAdvicesLists.finishRefresh();
        if (mPage == 1) {
            rvAdvicesLists.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
        dataNull();
    }

    @Override
    public void requestAdvicesReadSuccess(BaseBean bean) {

    }
}