package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.MainActivityContract;
import com.android.jidian.client.mvp.model.MainActivityModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.pub.weixin.Constants;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2022/12/26 16:45
 * company: 兴达智联
 * description:
 */
public class MainActivityPresenter extends BasePresenter<MainActivityContract.View>implements MainActivityContract.Presenter {

    private MainActivityContract.Model model;

    public MainActivityPresenter() {
        this.model = new MainActivityModel();
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

    @Override
    public void appVerUpgrade(String uid) {
        if (!isViewAttached()) {
            return;
        }
//        mView.showProgress();
        Disposable disposable = model.appVerUpgrade(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
//                        mView.hideProgress();
                        if ("1".equals(bean.getStatus())) {
                            mView.appVerUpgradeSuccess(bean);
                        }else {
                            mView.requestMainInfoFail(bean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
//                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestCheckAccv2(String apptoken, String appsn) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
//            mView.showProgress();
        }
        Disposable disposable = model.requestCheckAccv2(apptoken, appsn)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
//                        mView.hideProgress();
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
//                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }

}
