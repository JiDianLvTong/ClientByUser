package com.android.jidian.client.mvp.ui.activity.map;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/29 15:30
 * company: 兴达智联
 * description:
 */
public interface ChargeSiteMapContract {
    interface Model {
        Flowable<ChargeSiteMapBean> requestChargeSite(String uid,String lng, String lat , String type);
    }

    interface View extends BaseView {
        void requestChargeSiteSuccess(ChargeSiteMapBean bean);
        void requestChargeSiteError(String msg);
    }

    interface Presenter {
        void requestChargeSite(String uid,String lng, String lat , String type);
    }
}
