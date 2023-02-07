package com.android.jidian.repair.mvp.main.PatrolFragment.hasPartol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragmentByMvp;
import com.android.jidian.repair.mvp.UserMyPatrol.patrolDetail.MyPatrolDetailActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPartolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPartolFragment extends BaseFragmentByMvp<UserMyPatrolPresenter> implements UserMyPatrolContract.View {

    @BindView(R.id.srl_my_time_task)
    public SmartRefreshLayout srlMyTimeTask;
    @BindView(R.id.rv_my_time_task)
    public RecyclerView rvMyTimeTask;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;

    private UserMyPatrolAdapter mAdapter;
    private int mPage = 1;
    private String mLng, mLat;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPartolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPartolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPartolFragment newInstance(String param1, String param2) {
        MyPartolFragment fragment = new MyPartolFragment();
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_partol;
    }

    @Override
    public void initView(View view) {
        mPresenter = new UserMyPatrolPresenter();
        mPresenter.attachView(this);
//        tvTitle.setText("已巡检列表");
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(requireActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlMyTimeTask.setRefreshHeader(materialHeader);
        srlMyTimeTask.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(requireActivity());
        srlMyTimeTask.setRefreshFooter(classicsFooter);
        rvMyTimeTask.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserMyPatrolAdapter();
        rvMyTimeTask.setAdapter(mAdapter);
        mAdapter.setListener(new UserMyPatrolAdapter.OnItemViewClickListener() {
//            @Override
//            public void OnClickGuide(PatrolMyListBean.DataBean.ListsBean bean) {
//                MapUtil.showNavigationDialog(UserMyPatrolActivity.this,"",bean.g);
//            }

            @Override
            public void OnClickItem(PatrolMyListBean.DataBean.ListsBean bean) {
                Intent intent = new Intent(requireActivity(), MyPatrolDetailActivity.class);
                intent.putExtra("id", bean.getId());
                startActivity(intent);
            }
        });
        srlMyTimeTask.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        srlMyTimeTask.autoRefresh();
    }

    private void requestData() {
        if (mPresenter != null) {
            mPresenter.requestPatrolMylists(mLng, mLat, mPage + "");
        }
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

    @Override
    public void requestPatrolMylistsSuccess(PatrolMyListBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().getLists().size() > 0) {
                rvMyTimeTask.setVisibility(View.VISIBLE);
                nullDataPanel.setVisibility(View.GONE);
                if (mPage == 1) {
                    mAdapter.setNewData(bean.getData().getLists());
                } else {
                    mAdapter.addData(bean.getData().getLists());
                }
                mPage++;
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
        srlMyTimeTask.finishRefresh();
        srlMyTimeTask.finishLoadMore();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
        srlMyTimeTask.finishRefresh();
        srlMyTimeTask.finishLoadMore();
    }

    private void dataNull() {
        srlMyTimeTask.finishRefresh();
        srlMyTimeTask.finishLoadMore();
        if (mPage == 1) {
            rvMyTimeTask.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
        }
    }
}