package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.PullCashGetUserProfitBean;
import com.android.jidian.client.mvp.contract.PullCashGetUserProfitContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/25 下午5:55
 * company: 兴达智联
 * description:
 */
public class PullCashGetUserProfitModel implements PullCashGetUserProfitContract.Model {
    @Override
    public Flowable<PullCashGetUserProfitBean> requestPullCashGetUserProfit() {
        return RetrofitClient.getInstance().getApiService().requestPullCashGetUserProfit();
    }
}
