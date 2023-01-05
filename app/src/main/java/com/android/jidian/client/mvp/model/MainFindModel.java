package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.FindIndexBean;
import com.android.jidian.client.mvp.contract.MainFindContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/30 17:50
 * company: 兴达智联
 * description:
 */
public class MainFindModel implements MainFindContract.Model {
    @Override
    public Flowable<FindIndexBean> requestFindIndex(String lng, String lat) {
        return RetrofitClient.getInstance().getApiService().requestFindIndex(lng, lat);
    }
}
