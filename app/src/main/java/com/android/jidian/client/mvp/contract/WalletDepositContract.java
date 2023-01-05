package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.DepositRefundOrderBean;

import java.util.List;

import io.reactivex.Flowable;

public interface WalletDepositContract {
    interface Model {
        Flowable<DepositRefundOrderBean> requestListData(String uid);

        Flowable<BaseBean> submitDepositRefund(String uid, String oid, String i);
    }

    interface View extends BaseView {
        void requestListDataSuccess(List<DepositRefundOrderBean.DataBean> bean);

        void requestListDataError(String msg);

        void submitDepositRefundSuccess(BaseBean bean);
    }

    interface Presenter {
        void requestListData(String uid);

        void submitDepositRefund(String uid, String oid, String i);
    }
}