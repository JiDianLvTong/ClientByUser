package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.MainUserContract;
import com.android.jidian.client.mvp.model.MainUserModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2022/12/30 16:00
 * company: 兴达智联
 * description:
 */
public class MainUserPresenter extends BasePresenter<MainUserContract.View> implements MainUserContract.Presenter {

    private MainUserContract.Model model;

    public MainUserPresenter() {
        this.model = new MainUserModel();
    }

    @Override
    public void requestMainInfo(String uid) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.requestMainInfo(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(expenseBean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if ("1".equals(expenseBean.getStatus())) {
                            mView.requestMainInfoSuccess(expenseBean);
                        }else {
                            mView.requestMainInfoFail(expenseBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }
}
