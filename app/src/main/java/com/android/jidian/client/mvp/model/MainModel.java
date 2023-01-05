package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.CabinetDetailBean;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.mvp.contract.MainContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class MainModel implements MainContract.Model {

    @Override
    public Flowable<CabinetDetailBean> cabinetDeatil(int cabid, String repair, String jingdu, String weidu) {
        return RetrofitClient.getInstance().getApiService().cabinetDeatil(cabid, repair, jingdu, weidu);
    }

    @Override
    public Flowable<LoginCheckAccv2Bean> requestCheckAccv2(String apptoken, String appsn) {
        return RetrofitClient.getInstance().getApiService().requestCheckAccv2(apptoken, appsn);
    }
}