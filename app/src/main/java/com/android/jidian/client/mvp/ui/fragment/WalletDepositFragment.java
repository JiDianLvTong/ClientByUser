package com.android.jidian.client.mvp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jidian.client.MainShop_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.DepositRefundOrderBean;
import com.android.jidian.client.mvp.contract.WalletDepositContract;
import com.android.jidian.client.mvp.presenter.WalletDepositPresenter;
import com.android.jidian.client.mvp.ui.adapter.WalletDepositAdapter;
import com.android.jidian.client.mvp.ui.dialog.CommonTipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author : PTT
 * date: 9/18/21 2:26 PM
 * company: 兴达智联
 * description: 我的钱包 - 押金
 */
public class WalletDepositFragment extends BaseFragment<WalletDepositPresenter> implements WalletDepositContract.View {
    public static WalletDepositFragment newInstance() {
        return new WalletDepositFragment();
    }

    @BindView(R.id.srl_wallet_deposit)
    SmartRefreshLayout srlWalletDeposit;
    @BindView(R.id.rv_wallet_deposit)
    RecyclerView rvWalletDeposit;
    @BindView(R.id.empty_wallet_deposit)
    LinearLayout rvEmpty;
    private WalletDepositAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_deposit;
    }

    @Override
    public void initView(View view) {
        mPresenter = new WalletDepositPresenter();
        mPresenter.attachView(this);

        rvWalletDeposit.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new WalletDepositAdapter(getContext());
        rvWalletDeposit.setAdapter(mAdapter);
        requestData();

        view.findViewById(R.id.btn_wallet_deposit_item).setOnClickListener(v -> {
            //点击去商家
            startActivity(new Intent(getContext(), MainShop_.class));
        });

        srlWalletDeposit.setEnableLoadMore(false);
        srlWalletDeposit.setOnRefreshListener(refreshLayout -> {
            requestData();
        });

        mAdapter.setOnItemClickListener(new WalletDepositAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DepositRefundOrderBean.DataBean bean) {
                if (mPresenter != null) {
                    String msg = "";
                    for (int i = 0; i < bean.getNote().size(); i++) {
                        if (i == 0) {
                            msg = bean.getNote().get(i);
                        } else {
                            msg = msg + ("100".equals(bean.getApply()) ? "\n" : "") + bean.getNote().get(i);
                        }
                    }

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                    String uid = sharedPreferences.getString("id", "");//"126573"
                    new CommonTipDialog().init(msg, "提交申请", ("10".equals(bean.getType()) || "100".equals(bean.getType())) ? "" : (bean.getNote().size() > 1 ? bean.getNote().get(1) : ""), getResources().getColor(R.color.color_FC481E), () -> {
                        mPresenter.submitDepositRefund(uid, bean.getId(), bean.getRefund_id());
                    }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).show(getFragmentManager());
                }
            }

            @Override
            public void onRejectClick(String msg) {
                new CommonTipDialog().init(msg, "我已了解", () -> {
                }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).show(getFragmentManager());
            }
        });
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
        srlWalletDeposit.finishRefresh(false);
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {//onNetConnected
                showMessage(throwable.toString());
            } else {//onNetDisconnected
                showMessage("无网络连接，请检查您的网络设置！");
            }
        }
    }

    @Override
    public void requestListDataSuccess(List<DepositRefundOrderBean.DataBean> bean) {
        if (bean.size() == 0) {
            setUiEmpty();
        } else {
            srlWalletDeposit.finishRefresh();
            rvEmpty.setVisibility(View.GONE);
            rvWalletDeposit.setVisibility(View.VISIBLE);
            mAdapter.setList().clear();
            mAdapter.setList().addAll(bean);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestListDataError(String msg) {
        setUiEmpty();
        showMessage(msg);
    }

    private void setUiEmpty() {
        srlWalletDeposit.finishRefresh();
        rvEmpty.setVisibility(View.VISIBLE);
        rvWalletDeposit.setVisibility(View.GONE);
    }

    @Override
    public void submitDepositRefundSuccess(BaseBean bean) {
        showMessage(bean.msg);
        if (bean.isOk()) {
            srlWalletDeposit.autoRefresh();
        }
    }

    public void requestData() {
        if (mPresenter != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String uid = sharedPreferences.getString("id", "");//"126573"
            mPresenter.requestListData(uid);
        }
    }
}