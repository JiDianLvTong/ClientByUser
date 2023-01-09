package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.mvp.bean.MainAppVersionBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/26 16:39
 * company: 兴达智联
 * description:
 */
public interface MainActivityContract {
    interface Model {
        Flowable<MainAppVersionBean> appVerUpgrade(String uid);
        Flowable<LoginCheckAccv2Bean> requestCheckAccv2(String apptoken, String appsn);
    }

    interface View extends BaseView {
        void appVerUpgradeSuccess(MainAppVersionBean bean);
        void requestCheckAccv2Success(LoginCheckAccv2Bean bean);
    }

    interface Presenter{
        void appVerUpgrade(String uid);
        void requestCheckAccv2(String apptoken,String appsn);
    }
}
