package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.mvp.contract.MainEquipmentContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/29 11:42
 * company: 兴达智联
 * description:
 */
public class MainEquipmentModel implements MainEquipmentContract.Model {
    @Override
    public Flowable<MainActiyivyExpenseBean> requestMainInfo(String uid) {
        return RetrofitClient.getInstance().getApiService().requestMainInfo(uid);
    }
}
