package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.MainContract;
import com.android.jidian.client.mvp.model.MainModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.pub.weixin.Constants;

import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private MainContract.Model model;

    public MainPresenter() {
        this.model = new MainModel();
    }

    @Override
    public void cabinetDeatil(int cabid, String repair, String jingdu, String weidu) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        //TODO RxLife
        Disposable disposable = model.cabinetDeatil(cabid, repair, jingdu, weidu)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(cabinetDetailBean -> {
                    mView.hideProgress();
                    mView.onSuccess(cabinetDetailBean);
                }, throwable -> {
                    mView.hideProgress();
                    mView.onError(throwable);
                });
    }

    @Override
    public void requestCheckAccv2(String apptoken, String appsn) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestCheckAccv2(apptoken, appsn)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if (!"1".equals(bean.getStatus())) {
                                mView.requestCheckAccv2Success(bean);
                            }else {
                                Constants.APP_ID = bean.getData().getWxappid();
                            }
                        } else {
                            mView.requestCheckAccv2Error("网络异常");
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestCheckAccv2Error("网络异常");
                    }
                });
    }
}