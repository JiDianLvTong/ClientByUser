package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.PullCashGetUserProfitBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/25 下午5:53
 * company: 兴达智联
 * description:
 */
public interface PullCashGetUserProfitContract {
    interface Model{
        Flowable<PullCashGetUserProfitBean> requestPullCashGetUserProfit();
    }

    interface View extends BaseView{
        void requestPullCashGetUserProfitSuccess(PullCashGetUserProfitBean.DataBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter{
        void requestPullCashGetUserProfit();
    }
}
