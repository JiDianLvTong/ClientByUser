package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.mvp.contract.AdvicesLists2Contract;
import com.android.jidian.client.mvp.model.AdvicesLists2Model;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;
import com.google.gson.Gson;

import io.reactivex.disposables.Disposable;

public class AdvicesLists2Presenter extends BasePresenter<AdvicesLists2Contract.View>
        implements AdvicesLists2Contract.Presenter {

    private AdvicesLists2Contract.Model model;

    public AdvicesLists2Presenter() {
        this.model = new AdvicesLists2Model();
    }

    @Override
    public void advicesListsV2(int type, int page) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.advicesListsV2(type, page)
                .compose(RxScheduler.Flo_io_main())
                .map(responseBody -> new Gson().fromJson(responseBody.string(), AdvicesLists2Bean.class))
                .subscribe(advicesLists2Bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onAdvicesListsV2Success(advicesLists2Bean);
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