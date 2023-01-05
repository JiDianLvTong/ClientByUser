package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.MyEbikeBtyContract;
import com.android.jidian.client.mvp.model.MyEbikeBtyModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;

import io.reactivex.disposables.Disposable;

public class MyEbikeBtyPresenter extends BasePresenter<MyEbikeBtyContract.View> implements MyEbikeBtyContract.Presenter {
    private MyEbikeBtyContract.Model model;

    public MyEbikeBtyPresenter() {
        this.model = new MyEbikeBtyModel();
    }

    @Override
    public void myEbikeBty(String uid) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        //TODO RxLife
        Disposable disposable = model.myEbikeBty(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(myEbikeBtyBean -> {
                    if (mView != null) {
                        mView.onmyEbikeBtySuccess(myEbikeBtyBean);
                        mView.hideProgress();
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onmyEbikeBtyError(throwable);
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void userConfirm(String uid, String id, int type, String confirm) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        //TODO RxLife
        Disposable disposable = model.userConfirm(uid, id, type, confirm)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(UserConfirmBean -> {
                    if (mView != null) {
                        mView.onuserConfirmSuccess(UserConfirmBean, id, type);
                        mView.hideProgress();
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onuserConfirmError(throwable);
                        mView.hideProgress();
                    }
                });
    }
}