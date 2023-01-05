package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.PullCashGetUserAccountInfoBean;
import com.android.jidian.client.mvp.contract.PullCashAddCashContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/26 上午10:16
 * company: 兴达智联
 * description:
 */
public class PullCashAddCashModel implements PullCashAddCashContract.Model {
    @Override
    public Flowable<BaseBean> requestPullCashAddCash(String cash_amount) {
        return RetrofitClient.getInstance().getApiService().requestPullCashAddCash(cash_amount);
    }

    @Override
    public Flowable<PullCashGetUserAccountInfoBean> requestPullCashGetUserAccountInfo() {
        return RetrofitClient.getInstance().getApiService().requestPullCashGetUserAccountInfo();
    }

    @Override
    public Flowable<BaseBean> requestPullCashSetUserAccountInfo(String access_code) {
        return RetrofitClient.getInstance().getApiService().requestPullCashSetUserAccountInfo(access_code);
    }

}
