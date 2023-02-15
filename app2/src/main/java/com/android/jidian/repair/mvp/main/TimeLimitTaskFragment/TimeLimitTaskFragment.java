package com.android.jidian.repair.mvp.main.TimeLimitTaskFragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragmentByMvp;
import com.android.jidian.repair.mvp.task.TimeTaskDetailActivity;
import com.android.jidian.repair.utils.MapUtil;
import com.android.jidian.repair.utils.WebSocketLongLink;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeLimitTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeLimitTaskFragment extends BaseFragmentByMvp<TimeLimitTaskPresenter> implements TimeLimitTaskContract.View {

    @BindView(R.id.srl_time_task)
    public SmartRefreshLayout srlTimeTask;
    @BindView(R.id.rv_time_task)
    public RecyclerView rvTimeTask;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;

    private TimeLimitTabkAdapter mAdapter;
    private int mPage = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimeLimitTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeLimitTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeLimitTaskFragment newInstance(String param1, String param2) {
        TimeLimitTaskFragment fragment = new TimeLimitTaskFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void setFragmentRefresh() {
        requestData(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_time_limit_task;
    }

    @Override
    public void initView(View view) {
        mPresenter = new TimeLimitTaskPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(requireActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlTimeTask.setRefreshHeader(materialHeader);
        srlTimeTask.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(requireActivity());
        srlTimeTask.setRefreshFooter(classicsFooter);
        rvTimeTask.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TimeLimitTabkAdapter();
        mAdapter.setListener(new TimeLimitTabkAdapter.OnItemViewClickListener() {
            @Override
            public void OnClickGuide(WorktaskListsBean.DataBean.ListsBean bean) {
                MapUtil.showNavigationDialog(requireActivity(), "目的地", bean.getLat(), bean.getLng());
            }

            @Override
            public void OnClickPhone(WorktaskListsBean.DataBean.ListsBean bean) {
                if (!TextUtils.isEmpty(bean.getPhone())) {
                    Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getPhone()));
                    startActivity(Intent);
                }
            }

            @Override
            public void OnClickItem(WorktaskListsBean.DataBean.ListsBean bean) {
                Intent intent = new Intent(requireActivity(), TimeTaskDetailActivity.class);
                intent.putExtra("wtid", bean.getId());
                startActivity(intent);
            }
        });
        rvTimeTask.setAdapter(mAdapter);
        srlTimeTask.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData(true);
            }
        });
        srlTimeTask.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(false);
            }
        });
        requestData(true);
    }

    private void requestData(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        }
        if (mPresenter != null) {
            mPresenter.requestWorktaskLists(mPage + "");
        }
    }

    @Override
    public void requestWorktaskListsSuccess(WorktaskListsBean.DataBean bean) {
        if (bean.getLists() != null) {
            if (bean.getLists().size() > 0) {
                rvTimeTask.setVisibility(View.VISIBLE);
                nullDataPanel.setVisibility(View.GONE);
                if (mPage == 1) {
                    mAdapter.setNewData(bean.getLists());
                } else {
                    mAdapter.addData(bean.getLists());
                }
                mPage++;
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
        srlTimeTask.finishRefresh();
        srlTimeTask.finishLoadMore();
    }

    @Override
    public void requestWorktaskListsFail(String msg) {
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
        srlTimeTask.finishRefresh();
        srlTimeTask.finishLoadMore();
        if (1 == mPage) {
            rvTimeTask.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
        } else {

        }
    }
}