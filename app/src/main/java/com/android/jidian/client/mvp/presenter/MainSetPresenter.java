package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.MainSetContract;
import com.android.jidian.client.mvp.model.MainSetModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2023/1/6 10:40
 * company: 兴达智联
 * description:
 */
public class MainSetPresenter extends BasePresenter<MainSetContract.View> implements MainSetContract.Presenter {

    private MainSetContract.Model model;

    public MainSetPresenter() {
        this.model = new MainSetModel();
    }

    @Override
    public void requestUserCancel(String uid, String code, String confirm) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestUserCancel(uid, code, confirm)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if (2 == bean.status) {
                                mView.requestUserCancelSuccess(bean);
                            } else {
                                mView.requestUserCancelFail(bean.msg);
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestUserSmsCancel() {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestUserSmsCancel()
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            mView.requestUserSmsCancel(bean);
                        }
                    }
                }, throwable ->

                {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }
}
