package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.PullCashGetUserAccountInfoBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/26 上午10:13
 * company: 兴达智联
 * description:
 */
public interface PullCashAddCashContract {

    interface Model {
        Flowable<BaseBean> requestPullCashAddCash(String cash_amount);

        Flowable<PullCashGetUserAccountInfoBean> requestPullCashGetUserAccountInfo();

        Flowable<BaseBean> requestPullCashSetUserAccountInfo(String access_code);
    }

    interface View extends BaseView {
        void requestPullCashAddCashSuccess(String msg);

        void requestPullCashAddCashFail(String msg);

        void requestPullCashGetUserAccountInfoSuccess(PullCashGetUserAccountInfoBean.DataBean bean);

        void requestPullCashSetUserAccountInfoSuccess(String msg);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestPullCashAddCash(String cash_amount);

        void requestPullCashGetUserAccountInfo();

        void requestPullCashSetUserAccountInfo(String access_code);
    }

}
