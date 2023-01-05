package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.mvp.contract.ExpenseContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class ExpenseModel implements ExpenseContract.Model {
    @Override
    public Flowable<ExpenseBean> expense(String uid) {
        return RetrofitClient.getInstance().getApiService().requestWalletInfo(uid);
    }

}
