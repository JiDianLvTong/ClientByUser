package com.android.jidian.repair.mvp.main;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:37
 * description:
 */
public class MainModel implements MainContract.Model {
    @Override
    public Flowable<BaseBean> requestLoginCheckAcc(String uid, String apptoken, String nowlng, String nowlat) {
        return RetrofitClient.getInstance().getApeService().requestCheckAcc(uid, apptoken, nowlng, nowlat);
    }

    @Override
    public Flowable<UpdateVersionBean> requestAppUpdateVersion(String uid) {
        return RetrofitClient.getInstance().getApiService().requestAppUpdateVersion(uid);
    }
}
