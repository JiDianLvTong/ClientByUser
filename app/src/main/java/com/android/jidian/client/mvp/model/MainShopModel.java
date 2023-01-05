package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.MainShopContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/29 15:33
 * company: 兴达智联
 * description:
 */
public class MainShopModel implements MainShopContract.Model {
    @Override
    public Flowable<ShopBuyBean> requestShopBuy(String lng, String lat) {
        return RetrofitClient.getInstance().getApiService().requestShopBuy(lng, lat);
    }

    @Override
    public Flowable<ShopRentBean> requestShopRent(String lng, String lat) {
        return RetrofitClient.getInstance().getApiService().requestShopRent(lng, lat);
    }

    @Override
    public Flowable<UserPersonalBean> requestUserPersonal(String uid) {
        return RetrofitClient.getInstance().getApiService().requestUserPersonal(uid);
    }
}
