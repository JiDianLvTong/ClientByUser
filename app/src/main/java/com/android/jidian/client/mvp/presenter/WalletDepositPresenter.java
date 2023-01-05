package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.WalletDepositContract;
import com.android.jidian.client.mvp.model.WalletDepositModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

public class WalletDepositPresenter extends BasePresenter<WalletDepositContract.View> implements WalletDepositContract.Presenter {
    private WalletDepositContract.Model model;

    public WalletDepositPresenter() {
        this.model = new WalletDepositModel();
    }

    @Override
    public void requestListData(String uid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestListData(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean.getStatus() == 1) {
                            mView.requestListDataSuccess(bean.getData());
                        } else {
                            mView.requestListDataError(bean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestListDataError(throwable.getMessage().toString());
                    }
                });
    }

    @Override
    public void submitDepositRefund(String uid, String oid, String id) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.submitDepositRefund(uid, oid, id)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.submitDepositRefundSuccess(bean);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestListDataError(throwable.getMessage().toString());
                    }
                });
    }
}