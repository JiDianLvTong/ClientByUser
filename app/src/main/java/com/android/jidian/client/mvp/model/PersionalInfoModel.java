package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.PersonalInfoContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/6 16:04
 * company: 兴达智联
 * description:
 */
public class PersionalInfoModel implements PersonalInfoContract.Model {
    @Override
    public Flowable<UserPersonalBean> requestUserPersonal(String uid) {
        return RetrofitClient.getInstance().getApiService().requestUserPersonal(uid);
    }
}
