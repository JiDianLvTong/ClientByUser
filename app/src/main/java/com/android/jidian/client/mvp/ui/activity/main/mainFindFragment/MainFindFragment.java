package com.android.jidian.client.mvp.ui.activity.main.mainFindFragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jidian.client.Main_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.FindIndexBean;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.mvp.contract.LoginContract;
import com.android.jidian.client.mvp.contract.MainFindContract;
import com.android.jidian.client.mvp.presenter.LoginPresenter;
import com.android.jidian.client.mvp.presenter.MainFindPresenter;
import com.android.jidian.client.mvp.ui.activity.map.ChargeSiteMap;
import com.android.jidian.client.util.OtherUtils;
import com.android.jidian.client.util.UserInfoHelper;
import com.bumptech.glide.Glide;
import com.minminaya.widget.GeneralRoundFrameLayout;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.header.FalsifyHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.sxu.shadowdrawable.ShadowDrawable;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFindFragment extends BaseFragment<MainFindPresenter> implements MainFindContract.View {

    @BindView(R.id.mapPanel)
    public LinearLayout mapPanel;
    @BindView(R.id.imagePanel_1)
    public GeneralRoundFrameLayout imagePanel_1;
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.i_1)
    public ImageView i_1;
    @BindView(R.id.i_2)
    public ImageView i_2;
    @BindView(R.id.i_3)
    public ImageView i_3;
    @BindView(R.id.i_4)
    public ImageView i_4;
    @BindView(R.id.i_5)
    public ImageView i_5;
    @BindView(R.id.i_6)
    public ImageView i_6;

    private String lng = "", lat = "";

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_find;
    }

    @Override
    public void initView(View view) {
        //mvp初始化
        mPresenter = new MainFindPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"),Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
                    requestData();
                } else {
                    smartRefreshLayout.finishRefresh();
                }

            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
        //请求数据
        requestData();
    }

    @Override
    public void requestFindIndexSuccess(FindIndexBean bean) {
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
//            if (bean.getData().getToplist() != null & bean.getData().getToplist().size() > 0) {
//                Glide.with(getActivity()).load(bean.getData().getToplist().get(0).getImgurl()).into(i_1);
//            }
            if (bean.getData().getImglist() != null) {
                if (bean.getData().getImglist().size() > 0) {
                    Glide.with(getActivity()).load(bean.getData().getImglist().get(0).getImgurl()).into(i_2);
                }
                if (bean.getData().getImglist().size() > 1) {
                    Glide.with(getActivity()).load(bean.getData().getImglist().get(1).getImgurl()).into(i_3);
                }
                if (bean.getData().getImglist().size() > 2) {
                    Glide.with(getActivity()).load(bean.getData().getImglist().get(2).getImgurl()).into(i_4);
                }
            }
        }
    }

    @Override
    public void requestFindIndexFail(String msg) {
        smartRefreshLayout.finishRefresh();
        showMessage(msg);
    }

    /**
     * 刷新界面数据
     */
    public void setFragmentPosition(String lng, String lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void requestData() {
        mPresenter.requestFindIndex(lng, lat);
    }

    @OnClick(R.id.mapPanel)
    public void onClickMapPanel() {
        getActivity().startActivity(new Intent(getActivity(), ChargeSiteMap.class));
    }

    @OnClick(R.id.imagePanel_1)
    public void onClickImagePanel_1() {

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
    }

}
