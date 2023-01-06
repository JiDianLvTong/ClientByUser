package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.UserUOrderBean;
import com.android.jidian.client.mvp.contract.OrderListContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/6 14:01
 * company: 兴达智联
 * description:
 */
public class OrderListModel implements OrderListContract.Model {
    @Override
    public Flowable<UserUOrderBean> requestUserUOrder(String uid, String lastid) {
        return RetrofitClient.getInstance().getApiService().requestUserUOrder(uid, lastid);
    }
}
