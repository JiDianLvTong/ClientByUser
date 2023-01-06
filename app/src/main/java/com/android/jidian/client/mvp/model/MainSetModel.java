package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.mvp.contract.MainSetContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/6 10:39
 * company: 兴达智联
 * description:
 */
public class MainSetModel implements MainSetContract.Model {
    @Override
    public Flowable<BaseBean> requestUserCancel(String uid, String code, String confirm) {
        return RetrofitClient.getInstance().getApiService().requestUserCancel(uid, code, confirm);
    }

    @Override
    public Flowable<BaseBean> requestUserSmsCancel() {
        return RetrofitClient.getInstance().getApiService().requestUserSmsCancel();
    }
}
