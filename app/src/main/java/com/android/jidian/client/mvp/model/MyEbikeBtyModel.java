package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.MyEbikeBtyBean;
import com.android.jidian.client.bean.UserConfirmBean;
import com.android.jidian.client.mvp.contract.MyEbikeBtyContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class MyEbikeBtyModel implements MyEbikeBtyContract.Model {
    @Override
    public Flowable<MyEbikeBtyBean> myEbikeBty(String uid) {
        return RetrofitClient.getInstance().getApiService().myEbikeBty(uid);
    }

    @Override
    public Flowable<UserConfirmBean> userConfirm(String uid, String id, int type, String confirm) {
        return RetrofitClient.getInstance().getApiService().userConfirm(uid, id, type, confirm);
    }
}