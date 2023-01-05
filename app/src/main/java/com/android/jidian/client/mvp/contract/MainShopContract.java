package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserPersonalBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;

/**
 * @author : xiaoming
 * date: 2022/12/29 15:30
 * company: 兴达智联
 * description:
 */
public interface MainShopContract {
    interface Model {
        Flowable<ShopBuyBean> requestShopBuy(String lng, String lat);

        Flowable<ShopRentBean> requestShopRent(String lng, String lat);

        Flowable<UserPersonalBean> requestUserPersonal(String uid);
    }

    interface View extends BaseView {
        void requestShopBuySuccess(ShopBuyBean bean);

        void requestShopRentSuccess(ShopRentBean bean);

        void requestShopFail(String msg);

        void requestUserPersonalSuccess(UserPersonalBean bean);
    }

    interface Presenter {
        void requestShopBuy(String lng, String lat);

        void requestShopRent(String lng, String lat);

        void requestUserPersonal(String uid);
    }
}
