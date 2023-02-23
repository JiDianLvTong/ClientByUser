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
    public void appVerUpgrade(String uid) {
        if (!isViewAttached()) {
            return;
        }
        model.appVerUpgrade(uid)
            .compose(RxScheduler.Flo_io_main())
            .subscribe(bean -> {
                if (mView != null) {
                    if ("1".equals(bean.getStatus())) {
                        mView.appVerUpgradeSuccess(bean);
                    }
                }
            }, throwable -> {
//                if (mView != null) {
//                    mView.onError(throwable);
//                }
            });
    }

    @Override
    public void requestCheckAccv2(String apptoken, String appsn) {
        if (!isViewAttached()) {
            return;
        }
        model.requestCheckAccv2(apptoken, appsn)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        if (bean != null) {
                            if (!"1".equals(bean.getStatus())) {
                                mView.requestCheckAccv2Success(bean);
                            }else {
                                Constants.APP_ID = bean.getData().getWxappid();
                            }
                        }
                    }
                }, throwable -> {
//                    if (mView != null) {
//                        mView.onError(throwable);
//                    }
                });
    }

}
