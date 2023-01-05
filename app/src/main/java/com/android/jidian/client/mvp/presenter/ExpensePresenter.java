package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.ExpenseContract;
import com.android.jidian.client.mvp.model.ExpenseModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;

import io.reactivex.disposables.Disposable;

public class ExpensePresenter extends BasePresenter<ExpenseContract.View> implements ExpenseContract.Presenter {
    private ExpenseContract.Model model;

    public ExpensePresenter() {
        this.model = new ExpenseModel();
    }

    @Override
    public void expense(String uid) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.expense(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(expenseBean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onexpenseSuccess(expenseBean);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onexpenseError(throwable);
                    }
                });
    }
}