package com.android.jidian.client.mvp.model;

import com.android.jidian.client.mvp.contract.ChargeSiteMapContract;
import com.android.jidian.client.mvp.bean.ChargeSiteMapBean;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/30 17:50
 * company: 兴达智联
 * description:
 */
public class ChargeSiteMapModel implements ChargeSiteMapContract.Model {

    @Override
    public Flowable<ChargeSiteMapBean> requestChargeSite(String uid, String lng, String lat, String type) {
        return RetrofitClient.getInstance().getApiService().getAllMarker(uid , lng, lat , type);
    }
}
