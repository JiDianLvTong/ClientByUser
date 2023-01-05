package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.mvp.contract.AdvicesLists1Contract;
import com.android.jidian.client.mvp.model.AdvicesLists1Model;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;
import com.google.gson.Gson;

import io.reactivex.disposables.Disposable;

public class AdvicesLists1Presenter extends BasePresenter<AdvicesLists1Contract.View> implements AdvicesLists1Contract.Presenter {
    private AdvicesLists1Contract.Model model;

    public AdvicesLists1Presenter() {
        this.model = new AdvicesLists1Model();
    }

    @Override
    public void advicesListsV2(int type, int page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.advicesListsV2(type, page)
                .compose(RxScheduler.Flo_io_main())
                .map(responseBody -> new Gson().fromJson(responseBody.string(), AdvicesLists2Bean.class))
                .subscribe(advicesLists1Bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onAdvicesListsV2Success(advicesLists1Bean);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onAdvicesListsV2Error(throwable);
                    }
                });
    }

    @Override
    public void advicesRead(int msg_id) {
        if (!isViewAttached()) {
            return;
        }
        Disposable disposable = model.advicesRead(msg_id)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(advicesReadBean -> {
                    if (mView != null) {
                        mView.onAdvicesReadSuccess(advicesReadBean);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onAdvicesReadError(throwable);
                    }
                });
    }

    @Override
    public void advicesAllRead(int type) {
        if (!isViewAttached()) {
            return;
        }
        Disposable disposable = model.advicesAllRead(type)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(advicesAllReadBean -> {
                    if (mView != null) {
                        mView.onAdvicesAllReadSuccess(advicesAllReadBean);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onAdvicesAllReadError(throwable);
                    }
                });
    }
}