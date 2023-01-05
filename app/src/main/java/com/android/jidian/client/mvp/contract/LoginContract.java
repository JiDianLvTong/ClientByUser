package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;

public interface LoginContract {
    interface Model {
        Flowable<BaseBean> sendVerificationCode(String phone);

        Flowable<LoginBean> requestLogin(String phone, String vcode, String confirm, String appsn);

        Flowable<LoginBean> sendJgVerificationLogin(String token,String confirm, String appsn);

        Flowable<BaseBean> sendHttpVisitLogs(String uid);
    }

    interface View extends BaseView {
        void sendVerificationCodeResult(String msg);

        void requestLoginSuccess(LoginBean bean);

        void requestLoginError(String msg);
    }

    interface Presenter {
        void sendVerificationCode(String phone);

        void requestLogin(String phone, String vcode, String confirm, String appsn);

        void sendJgVerificationLogin(String token,String confirm, String appsn);

        void sendHttpVisitLogs(String uid);
    }
}