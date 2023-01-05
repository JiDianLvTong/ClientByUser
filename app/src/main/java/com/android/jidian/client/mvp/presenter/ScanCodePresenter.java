package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.ScanCodeContract;
import com.android.jidian.client.mvp.model.ScanCodeModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

public class ScanCodePresenter extends BasePresenter<ScanCodeContract.View> implements ScanCodeContract.Presenter {
    private ScanCodeContract.Model model;

    public ScanCodePresenter() {
        this.model = new ScanCodeModel();
    }

    @Override
    public void requestQrCodeScan(String uid, String code, String entercode, String type, String sync) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        //TODO RxLife
        Disposable disposable = model.requestQrCodeScan(uid, code, entercode, type, sync)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(data -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (data != null) {
                            if (data.getStatus() == 1) {
                                mView.requestQrCodeScanSuccess(data);
                            } else {
                                mView.requestQrCodeScanError(data.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestQrCodeScanError(throwable.toString());
                    }
                });
    }

    @Override
    public void requestEndBindBike(String uid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        //TODO RxLife
        Disposable disposable = model.requestEndBindBike(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(cabinetDetailBean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (cabinetDetailBean.getStatus() == 1) {
                            mView.requestEndBindBikeSuccess(cabinetDetailBean);
                        } else {
                            mView.requestQrCodeScanError(cabinetDetailBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestQrCodeScanError(throwable.toString());
                    }
                });
    }

    @Override
    public void requestCouponScan(String uid, String code) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        //TODO RxLife
        Disposable disposable = model.requestCouponScan(uid, code)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(data -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (data != null) {
                            if (data.isOk()) {
                                mView.requestCouponScanSuccess(data);
                            } else {
                                mView.requestQrCodeScanError(data.msg);
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestQrCodeScanError(throwable.toString());
                    }
                });
    }
}