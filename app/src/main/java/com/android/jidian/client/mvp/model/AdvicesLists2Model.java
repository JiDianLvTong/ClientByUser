package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.AdvicesAllReadBean;
import com.android.jidian.client.bean.AdvicesReadBean;
import com.android.jidian.client.mvp.contract.AdvicesLists2Contract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

public class AdvicesLists2Model implements AdvicesLists2Contract.Model {
    @Override
    public Flowable<ResponseBody> advicesListsV2(int type, int page) {
        return RetrofitClient.getInstance().getApiService().advicesListsV2(type, page);
    }

    @Override
    public Flowable<AdvicesReadBean> advicesRead(int msg_id) {
        return RetrofitClient.getInstance().getApiService().advicesRead(msg_id + "");
    }

    @Override
    public Flowable<AdvicesAllReadBean> advicesAllRead(int type) {
        return RetrofitClient.getInstance().getApiService().advicesAllRead(type);
    }
}