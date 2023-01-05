package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.mvp.contract.MainUserContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/30 16:00
 * company: 兴达智联
 * description:
 */
public class MainUserModel implements MainUserContract.Model {
    @Override
    public Flowable<MainActiyivyExpenseBean> requestMainInfo(String uid) {
        return RetrofitClient.getInstance().getApiService().requestMainInfo(uid);
    }
}
