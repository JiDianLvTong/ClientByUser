package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.bean.MainAppVersionBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/26 16:39
 * company: 兴达智联
 * description:
 */
public interface MainActivityContract {
    interface Model {
        Flowable<MainActiyivyExpenseBean> requestMainInfo(String uid);
        Flowable<MainAppVersionBean> appVerUpgrade(String uid);
        Flowable<LoginCheckAccv2Bean> requestCheckAccv2(String apptoken, String appsn);
    }

    interface View extends BaseView {
        void requestMainInfoSuccess(MainActiyivyExpenseBean bean);

        void requestMainInfoFail(String msg);

        void appVerUpgradeSuccess(MainAppVersionBean bean);

        void appVerUpgradeFail(String msg);

        void requestCheckAccv2Success(LoginCheckAccv2Bean bean);

        void requestCheckAccv2Error(String msg);
    }

    interface Presenter{
        void requestMainInfo(String uid);

        void appVerUpgrade(String uid);

        void requestCheckAccv2(String apptoken,String appsn);
    }
}
