package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.DepositRefundOrderBean;
import com.android.jidian.client.mvp.contract.DepositContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/9 14:10
 * company: 兴达智联
 * description:
 */
public class DepositModel implements DepositContract.Model {
    @Override
    public Flowable<DepositRefundOrderBean> requestDepositRefundOrder(String uid) {
        return RetrofitClient.getInstance().getApiService().requestDepositRefundOrder(uid);
    }

    @Override
    public Flowable<BaseBean> requestSubmitDepositRefund(String uid, String oid, String id) {
        return RetrofitClient.getInstance().getApiService().requestSubmitDepositRefund(uid, oid, id);
    }
}
