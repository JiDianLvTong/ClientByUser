package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.DepositRefundOrderBean;
import com.android.jidian.client.mvp.contract.WalletDepositContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class WalletDepositModel implements WalletDepositContract.Model {
    @Override
    public Flowable<DepositRefundOrderBean> requestListData(String uid) {
        return RetrofitClient.getInstance().getApiService().requestDepositRefundOrder(uid);
    }

    @Override
    public Flowable<BaseBean> submitDepositRefund(String uid, String oid, String id) {
        return RetrofitClient.getInstance().getApiService().submitDepositRefund(uid, oid, id);
    }
}