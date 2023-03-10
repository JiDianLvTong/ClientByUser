package com.android.jidian.repair.mvp.main.PatrolFragment.noPartol;

import static com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolFragmentEvent.LOCATION_SUCCESS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragmentByMvp;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.mvp.cabinet.cabFailureAdd.FailureAddActivity;
import com.android.jidian.repair.mvp.cabinet.cabPatrol.PatrolAddActivity;
import com.android.jidian.repair.utils.MapUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatrolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@BindEventBus
public class PatrolFragment extends BaseFragmentByMvp<PatrolPresenter> implements PatrolContract.View {

    @BindView(R.id.srl_patrol)
    public SmartRefreshLayout srlPatrol;
    @BindView(R.id.rv_patrol)
    public RecyclerView rvPatrol;
    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;

    private PartolAdapter mAdapter;
    private int mPage = 1;
    private boolean mHasPiosition = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "lng";
    private static final String ARG_PARAM2 = "lat";

    // TODO: Rename and change types of parameters
    private String mLng;
    private String mLat;

    public PatrolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatrolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatrolFragment newInstance(String param1, String param2) {
        PatrolFragment fragment = new PatrolFragment();
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
            mLng = getArguments().getString(ARG_PARAM1);
            mLat = getArguments().getString(ARG_PARAM2);
            mHasPiosition = !TextUtils.isEmpty(mLng) && !TextUtils.isEmpty(mLat);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_patrol;
    }

    public void setFragmentRefresh() {
        if (srlPatrol != null) {
            srlPatrol.autoRefresh();
        }
    }

    @Override
    public void initView(View view) {
        mPresenter = new PatrolPresenter();
        mPresenter.attachView(this);
        //????????????
        MaterialHeader materialHeader = new MaterialHeader(requireActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srlPatrol.setRefreshHeader(materialHeader);
        srlPatrol.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(requireActivity());
        srlPatrol.setRefreshFooter(classicsFooter);
        rvPatrol.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PartolAdapter();
        rvPatrol.setAdapter(mAdapter);
        mAdapter.setListener(new PartolAdapter.OnItemViewClickListener() {
            @Override
            public void OnClickGuide(PatrolIndexBean.DataBean.ListsBean bean) {
                MapUtil.showNavigationDialog(requireActivity(), "", bean.getWeidu(), bean.getJingdu());
            }
            @Override
            public void OnClickItemT1(PatrolIndexBean.DataBean.ListsBean bean) {
                Intent intent = new Intent(requireActivity(), PatrolAddActivity.class);
                intent.putExtra("name", bean.getName());
                intent.putExtra("address", bean.getAddress());
                intent.putExtra("id", bean.getId());
                startActivity(intent);
            }
            @Override
            public void OnClickItemT2(PatrolIndexBean.DataBean.ListsBean bean) {
                Intent intent = new Intent(requireActivity(), FailureAddActivity.class);
                intent.putExtra("id", bean.getId());
                startActivity(intent);
            }
        });
        srlPatrol.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
//        srlPatrol.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(PatrolFragmentEvent event) {
        if (event != null) {
            if (event.getEvent() == LOCATION_SUCCESS) {
                if (!mHasPiosition) {
                    mLng = event.getLng();
                    mLat = event.getLat();
                    requestData();
                }
            }
        }
    }

    private void requestData() {
        if (mPresenter != null) {
            if (!TextUtils.isEmpty(mLng) && !TextUtils.isEmpty(mLat)) {
                mPresenter.requestPatrolIndex(mLng, mLat, mPage + "");
                mHasPiosition = true;
            }
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
        showMessage("????????????????????????????????????????????????");
        dataNull();
    }

    @Override
    public void requestPatrolIndexSuccess(PatrolIndexBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().getLists().size() > 0) {
                rvPatrol.setVisibility(View.VISIBLE);
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
        srlPatrol.finishRefresh();
        srlPatrol.finishLoadMore();
    }

    private void dataNull() {
        srlPatrol.finishRefresh();
        srlPatrol.finishLoadMore();
        if (1 == mPage) {
            rvPatrol.setVisibility(View.GONE);
            nullDataPanel.setVisibility(View.VISIBLE);
        } else {

        }
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
        srlPatrol.finishRefresh();
        srlPatrol.finishLoadMore();
    }
}