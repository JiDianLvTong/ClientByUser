package com.android.jidian.repair.mvp.login;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/1/10 17:14
 * description:
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private LoginContract.Model model;

    public LoginPresenter() {
        this.model = new LoginModel();
    }

    @Override
    public void requestLoginJdLogin(String phone, String passwd) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.requestLoginJdLogin(phone, passwd)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                if (bean.getData() != null) {
                                    mView.requestLoginJdLoginSuccess(bean.getData());
                                }
                            } else {
                                mView.requestShowTips(bean.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestShowTips(throwable.getLocalizedMessage());
                    }
                });
    }
}
