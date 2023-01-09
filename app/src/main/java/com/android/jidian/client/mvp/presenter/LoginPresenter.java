package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.LoginContract;
import com.android.jidian.client.mvp.model.LoginModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private LoginContract.Model model;

    public LoginPresenter() {
        this.model = new LoginModel();
    }

    @Override
    public void sendVerificationCode(String phone) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.sendVerificationCode(phone)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if (bean.isOk()) {
                                mView.sendVerificationCodeResult(bean.msg);
                            } else {
                                mView.requestLoginError(bean.msg);
                            }
                        } else {
                            mView.requestLoginError("网络异常");
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestLoginError("网络异常");
                    }
                });
    }

    @Override
    public void requestLogin(String phone, String vcode, String confirm, String appsn) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestLogin(phone, vcode, confirm, appsn)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            mView.requestLoginSuccess(bean);
                        } else {
                            mView.requestLoginError("网络异常");
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestLoginError("网络异常");
                    }
                });
    }

    @Override
    public void sendJgVerificationLogin(String token, String confirm, String appsn) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.sendJgVerificationLogin(token, confirm, appsn)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            mView.requestLoginSuccess(bean);
                        } else {
                            mView.requestLoginError("网络异常");
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestLoginError("网络异常");
                    }
                });
    }

    @Override
    public void sendHttpVisitLogs(String uid) {
        if (!isViewAttached()) {
            return;
        }
        Disposable disposable = model.sendHttpVisitLogs(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                }, throwable -> {
                });
    }
}