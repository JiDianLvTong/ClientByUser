package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/29 11:37
 * company: 兴达智联
 * description:
 */
public interface MainEquipmentContract {
    interface Model{
        Flowable<MainActiyivyExpenseBean> requestMainInfo(String uid);
    }

    interface View extends BaseView{
        void requestWalletInfoSuccess(MainActiyivyExpenseBean bean);
        void requestWalletInfoFail(String msg);
    }

    interface Presenter {
        void requestWalletInfo(String uid);
    }

}
