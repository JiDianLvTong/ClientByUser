package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.DepositRefundOrderBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/9 14:08
 * company: 兴达智联
 * description:
 */
public interface DepositContract {
    interface Model{
        Flowable<DepositRefundOrderBean> requestDepositRefundOrder(String uid);
    }

    interface View extends BaseView{
        void requestDepositRefundOrderSuccess(DepositRefundOrderBean bean);

        void requestDepositRefundOrderFail(String msg);
    }

    interface Presenter{
        void requestDepositRefundOrder(String uid);
    }
}
