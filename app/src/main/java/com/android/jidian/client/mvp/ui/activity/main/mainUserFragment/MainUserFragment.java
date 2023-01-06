package com.android.jidian.client.mvp.ui.activity.main.mainUserFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainDiscount_;
import com.android.jidian.client.MainHello_;
import com.android.jidian.client.MainInfo_;
import com.android.jidian.client.MainMessage_1_;
import com.android.jidian.client.Newpay_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseFragmentByMvp;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.mvp.contract.MainUserContract;
import com.android.jidian.client.mvp.presenter.MainUserPresenter;
import com.android.jidian.client.mvp.ui.activity.OrderListActivity;
import com.android.jidian.client.mvp.ui.activity.set.MainSetActivity;
import com.android.jidian.client.mvp.ui.activity.MoreQuestion;
import com.android.jidian.client.util.UserInfoHelper;
import com.itheima.roundedimageview.RoundedImageView;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

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
    public TextView myMonthOutTimePanelRePay;
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;

    private JSONArray mJSONArray;

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_user;
    }

    @Override
    public void initView(View view) {
        //mvp初始化
        mPresenter = new MainUserPresenter();
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String name = sharedPreferences.getString("real_name", "");
        myInfoName.setText(name);

        //请求数据
        requestData();
    }

    //请求数据
    private void requestData() {
        if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
            mPresenter.requestMainInfo(UserInfoHelper.getInstance().getUid());
        }
    }

    @Override
    public void requestMainInfoSuccess(MainActiyivyExpenseBean bean) {
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            String gid = bean.getData().getUmonth().getPackets().getGid();
            String otype = bean.getData().getUmonth().getPackets().getOtype();
            mJSONArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("gid", gid);
                jsonObject.put("otype", otype);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mJSONArray.put(jsonObject);

            myMiDouCount.setText(bean.getData().getTop().getCList().get(0).getNums() + "");
            //0=未过期，1=已过期
            if ("0".equals(bean.getData().getUmonth().getPackets().getIsexpire())) {//包月没过期
                myMonthInTimePanel.setVisibility(View.VISIBLE);
                myMonthOutTimePanel.setVisibility(View.GONE);
                myMonthInTimePanelTime.setText(bean.getData().getUmonth().getPackets().getDays() + "");
                myMonthInTimePanelData.setText(bean.getData().getUmonth().getPackets().getExpire());
            } else {//包月过期
                myMonthInTimePanel.setVisibility(View.GONE);
                myMonthOutTimePanel.setVisibility(View.VISIBLE);
                myMonthOutTimePanelData.setText(bean.getData().getUmonth().getPackets().getExpire());
            }
        }
    }

    @Override
    public void requestMainInfoFail(String msg) {
        showMessage(msg);
        smartRefreshLayout.finishRefresh();
    }

    @OnClick({R.id.myMonth})
    public void onClickMyMonth() {
        //from=40
        if (mJSONArray != null) {
            Intent intent = new Intent(getActivity(), Newpay_.class);
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
        getActivity().startActivity(new Intent(getActivity(), MainDiscount_.class));
    }

    @OnClick(R.id.myAddress)
    public void onClickMyAddress() {
//        getActivity().startActivity(new Intent(getActivity(), MyAddress.class));
    }

    @OnClick(R.id.myCommonProblem)
    public void onClickMyCommonProblem() {
        getActivity().startActivity(new Intent(getActivity(), MoreQuestion.class));
    }


    @OnClick(R.id.mySet)
    public void onClickMySet() {
        getActivity().startActivity(new Intent(getActivity(), MainSetActivity.class));
    }

    @OnClick(R.id.myInfo)
    public void onClickMyInfo() {
        getActivity().startActivity(new Intent(getActivity(), MainInfo_.class));
    }

    @OnClick(R.id.myMiDou)
    public void onClickMyMiDou() {
        getActivity().startActivity(new Intent(getActivity(), MainHello_.class));
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
