package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.PullCashGetCashRecordBean;
import com.android.jidian.client.mvp.contract.CashWithdrawalRecordContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/26 下午3:41
 * company: 兴达智联
 * description:
 */
public class CashWithdrawalRecordModel implements CashWithdrawalRecordContract.Model {
    @Override
    public Flowable<PullCashGetCashRecordBean> requestPullCashGetCashRecord() {
        return RetrofitClient.getInstance().getApiService().requestPullCashGetCashRecord();
    }
}
