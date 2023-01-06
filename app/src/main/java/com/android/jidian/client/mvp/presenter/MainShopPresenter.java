package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.MainShopContract;
import com.android.jidian.client.mvp.model.MainShopModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2022/12/29 15:35
 * company: 兴达智联
 * description:
 */
public class MainShopPresenter extends BasePresenter<MainShopContract.View> implements MainShopContract.Presenter{

    private MainShopContract.Model model;

    public MainShopPresenter() {
        this.model = new MainShopModel();
    }

    @Override
    public void requestShopBuy(String lng, String lat) {
        if (!isViewAttached()) {
            return;
        }
        model.requestShopBuy(lng, lat)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        if (bean != null) {
                            if (1 == bean.getStatus()) {
                                mView.requestShopBuySuccess(bean);
                            }else {
                                mView.requestShopFail(bean.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestShopRent(String lng, String lat) {
        if (!isViewAttached()) {
            return;
        }
        model.requestShopRent(lng, lat)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        if (bean != null) {
                            if (1 == bean.getStatus()) {
                                mView.requestShopRentSuccess(bean);
                            }else {
                                mView.requestShopFail(bean.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestUserPersonal(String uid) {
        if (!isViewAttached()) {
            return;
        }
//        if (mView != null) {
//            mView.showProgress();
//        }
        Disposable disposable = model.requestUserPersonal(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
//                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestUserPersonalSuccess(bean);
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
}
