package com.android.jidian.client.mvp.ui.activity.main.mainUserFragment;

import static com.android.jidian.client.bean.event.MainUserEvent.COUPON_USE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainDiscount_;
import com.android.jidian.client.Newpay_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseFragmentByMvp;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.bean.event.MainUserEvent;
import com.android.jidian.client.mvp.contract.MainUserContract;
import com.android.jidian.client.mvp.presenter.MainShopPresenter;
import com.android.jidian.client.mvp.presenter.MainUserPresenter;
import com.android.jidian.client.mvp.ui.activity.Deposit.DepositActivity;
import com.android.jidian.client.mvp.ui.activity.coupon.CouponActivity;
import com.android.jidian.client.mvp.ui.activity.h5.MainMoreQuestion;
import com.android.jidian.client.mvp.ui.activity.order.OrderListActivity;
import com.android.jidian.client.mvp.ui.activity.pay.PayByCoinActivity;
import com.android.jidian.client.mvp.ui.activity.pay.PayByReNew;
import com.android.jidian.client.mvp.ui.activity.pub.ScanCodeActivity;
import com.android.jidian.client.mvp.ui.activity.userInfo.PersonalInfoActivity;
import com.android.jidian.client.mvp.ui.activity.set.MainSetActivity;
import com.android.jidian.client.util.UserInfoHelper;
import com.itheima.roundedimageview.RoundedImageView;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainUserFragment extends U6BaseFragmentByMvp<MainUserPresenter> implements MainUserContract.View {

    @BindView(R.id.myInfo)
    public LinearLayout myInfo;
    @BindView(R.id.myMiDou)
    public LinearLayout myMiDou;
    @BindView(R.id.otherPanel)
    public LinearLayout otherPanel;
    @BindView(R.id.myInfoName)
    public TextView myInfoName;
    @BindView(R.id.myInfoImage)
    public RoundedImageView myInfoImage;
    @BindView(R.id.myMiDouCount)
    public TextView myMiDouCount;
    @BindView(R.id.tv_coin_charge)
    public TextView tv_coin_charge;
    @BindView(R.id.tv_deposit_back)
    public TextView tv_deposit_back;

    @BindView(R.id.myMonth)
    public LinearLayout myMonth;
    @BindView(R.id.myMonthInTimePanel)
    public LinearLayout myMonthInTimePanel;
    @BindView(R.id.myMonthInTimePanelTime)
    public TextView myMonthInTimePanelTime;
    @BindView(R.id.myMonthInTimePanelData)
    public TextView myMonthInTimePanelData;
    @BindView(R.id.myMonthOutTimePanel)
    public LinearLayout myMonthOutTimePanel;
    @BindView(R.id.myMonthOutTimePanelData)
    public TextView myMonthOutTimePanelData;
    @BindView(R.id.myMonthOutTimePanelRePay)
    public LinearLayout myMonthOutTimePanelRePay;
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.myScan)
    public LinearLayout myScan;

    private JSONArray mJSONArray;

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_user;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MainUserEvent event) {
        if (event.getEvent() == COUPON_USE) {
            requestData();
        }
    }

    //设置坐标
    public void setFragmentRefresh() {
        if (mPresenter == null) {
            mPresenter = new MainUserPresenter();
            mPresenter.attachView(this);
        }
        requestData();
    }

    @Override
    public void initView(View view) {
        //mvp初始化
        mPresenter = new MainUserPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String name = sharedPreferences.getString("real_name", "");
        myInfoName.setText(name);
        //请求数据
        showProgress();
        requestData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//不可见
            if (mPresenter != null) {
                requestData();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                String name = sharedPreferences.getString("real_name", "");
                myInfoName.setText(name);
            }
        }
    }

    //请求数据
    private void requestData() {
        if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
            mPresenter.requestMainInfo(UserInfoHelper.getInstance().getUid());
        }
    }

    @Override
    public void requestMainInfoSuccess(MainActiyivyExpenseBean bean) {
        hideProgress();
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
//            String gid = bean.getData().getUmonth().getPackets().getGid();
//            String otype = bean.getData().getUmonth().getPackets().getOtype();
//            mJSONArray = new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("gid", gid);
//                jsonObject.put("otype", otype);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            mJSONArray.put(jsonObject);

            myMiDouCount.setText(bean.getData().getTop().getCList().get(0).getNums() + "");
//            if (!TextUtils.isEmpty(bean.getData().getUmonth().getPackets().getGid())) {//可能有包月
//                //0=未过期，1=已过期
//                if ("0".equals(bean.getData().getUmonth().getPackets().getIsexpire())) {//包月没过期
//                    myMonthInTimePanel.setVisibility(View.VISIBLE);
//                    myMonthOutTimePanel.setVisibility(View.GONE);
//                    myMonthInTimePanelTime.setText(bean.getData().getUmonth().getPackets().getDays() + "");
//                    myMonthInTimePanelData.setText(bean.getData().getUmonth().getPackets().getExpire());
//                    myMonthOutTimePanelRePay.setVisibility(View.VISIBLE);
//                } else if ("2".equals(bean.getData().getUmonth().getPackets().getIsexpire())) {//没有包月
//                    myMonthInTimePanel.setVisibility(View.GONE);
//                    myMonthOutTimePanel.setVisibility(View.GONE);
//                    myMonthOutTimePanelRePay.setVisibility(View.GONE);
//                } else {//包月过期
//                    myMonthInTimePanel.setVisibility(View.GONE);
//                    myMonthOutTimePanel.setVisibility(View.VISIBLE);
//                    myMonthOutTimePanelRePay.setVisibility(View.VISIBLE);
//                    myMonthOutTimePanelData.setText(bean.getData().getUmonth().getPackets().getExpire());
//                }
//            } else {//没有包月
//                myMonthInTimePanel.setVisibility(View.GONE);
//                myMonthOutTimePanel.setVisibility(View.GONE);
//                myMonthOutTimePanelRePay.setVisibility(View.GONE);
//            }

        }
    }

    @Override
    public void requestMainInfoFail(String msg) {
        hideProgress();
        showMessage(msg);
        smartRefreshLayout.finishRefresh();
    }

    @OnClick(R.id.myScan)
    public void onClickmyScan(){
        Intent intent = new Intent(requireActivity(), ScanCodeActivity.class);
        intent.putExtra("from", "keshang");
        startActivity(intent);
    }

    @OnClick({R.id.myMonth})
    public void onClickMyMonth() {
        //from=40
        if (mJSONArray != null) {
            Intent intent = new Intent(getActivity(), PayByReNew.class);
            intent.putExtra("mjson", mJSONArray.toString());
            intent.putExtra("type", "40");
            requireActivity().startActivity(intent);
        }
    }

    @OnClick(R.id.myOrder)
    public void onClickMyOrder() {
        getActivity().startActivity(new Intent(getActivity(), OrderListActivity.class));
    }

    @OnClick(R.id.myCoupon)
    public void onClickMyCoupon() {
        getActivity().startActivity(new Intent(getActivity(), CouponActivity.class));
    }

    @OnClick(R.id.myAddress)
    public void onClickMyAddress() {
//        getActivity().startActivity(new Intent(getActivity(), MyAddress.class));
    }

    @OnClick(R.id.myCommonProblem)
    public void onClickMyCommonProblem() {
        getActivity().startActivity(new Intent(getActivity(), MainMoreQuestion.class));
    }


    @OnClick(R.id.mySet)
    public void onClickMySet() {
        getActivity().startActivity(new Intent(getActivity(), MainSetActivity.class));
    }

    @OnClick(R.id.myInfo)
    public void onClickMyInfo() {
        getActivity().startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
    }

    @OnClick(R.id.tv_coin_charge)
    public void onClickCoinCharge() {
        getActivity().startActivity(new Intent(getActivity(), PayByCoinActivity.class));
    }

    @OnClick(R.id.tv_deposit_back)
    public void onClickDepositBack() {
        getActivity().startActivity(new Intent(getActivity(), DepositActivity.class));
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
        smartRefreshLayout.finishRefresh();
        showMessage("无网络链接，请检查您的网络设置！");
    }

}
