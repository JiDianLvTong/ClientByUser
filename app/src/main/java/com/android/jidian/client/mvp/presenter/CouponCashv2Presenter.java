package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.CouponCashv2Contract;
import com.android.jidian.client.mvp.model.CouponCashv2Model;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;

import io.reactivex.disposables.Disposable;

public class CouponCashv2Presenter extends BasePresenter<CouponCashv2Contract.View> implements CouponCashv2Contract.Presenter {
    private CouponCashv2Contract.Model model;

    public CouponCashv2Presenter() {
        this.model = new CouponCashv2Model();
    }

    @Override
    public void couponCashv2(int uid, Integer cid, String code, String type) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        //TODO RxLife
        Disposable disposable = model.couponCashv2(uid, cid, code, type)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(couponCashv2Bean -> {
                    mView.hideProgress();
                    mView.onCouponCashv2Success(couponCashv2Bean);
                }, throwable -> {
                    mView.hideProgress();
                    mView.onError(throwable);
                });
    }

    @Override
    public void couponScan(int uid, String code) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        //TODO RxLife
        Disposable disposable = model.couponScan(uid, code)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(couponScanBean -> {
                    mView.hideProgress();
                    mView.onCouponScanSuccess(couponScanBean);
                }, throwable -> {
                    mView.hideProgress();
                    mView.onCouponScanError(throwable);
                });
    }
}
