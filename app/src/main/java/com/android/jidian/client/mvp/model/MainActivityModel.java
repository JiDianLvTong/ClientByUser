package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.bean.MainAppVersionBean;
import com.android.jidian.client.mvp.contract.MainActivityContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/26 16:44
 * company: 兴达智联
 * description:
 */
public class MainActivityModel implements MainActivityContract.Model {
    @Override
    public Flowable<MainActiyivyExpenseBean> requestMainInfo(String uid) {
        return RetrofitClient.getInstance().getApiService().requestMainInfo(uid);
    }

    @Override
    public Flowable<MainAppVersionBean> appVerUpgrade(String uid) {
        return RetrofitClient.getInstance().getAppServiceApi().appVerUpgrade(uid);
    }

    @Override
    public Flowable<LoginCheckAccv2Bean> requestCheckAccv2(String apptoken, String appsn) {
        return RetrofitClient.getInstance().getApiService().requestCheckAccv2(apptoken, appsn);
    }
}
