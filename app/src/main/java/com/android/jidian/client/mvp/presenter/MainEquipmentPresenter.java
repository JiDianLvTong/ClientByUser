package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.MainActivityContract;
import com.android.jidian.client.mvp.contract.MainEquipmentContract;
import com.android.jidian.client.mvp.model.MainActivityModel;
import com.android.jidian.client.mvp.model.MainEquipmentModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.pub.weixin.Constants;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2022/12/29 11:43
 * company: 兴达智联
 * description:
 */
public class MainEquipmentPresenter extends BasePresenter<MainEquipmentContract.View> implements MainEquipmentContract.Presenter {

    private MainEquipmentContract.Model model;

    public MainEquipmentPresenter() {
        this.model = new MainEquipmentModel();
    }

    @Override
    public void requestWalletInfo(String uid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestMainInfo(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestWalletInfoSuccess(bean);
                            }else {
                                mView.requestWalletInfoFail(bean.getMsg());
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
