package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;

public interface ExpenseContract {
    interface Model {
        Flowable<ExpenseBean> expense(String uid);
    }

    interface View extends BaseView {
        void onexpenseSuccess(ExpenseBean expenseBean);

        void onexpenseError(Throwable throwable);
    }

    interface Presenter {
        void expense(String uid);
    }
}
