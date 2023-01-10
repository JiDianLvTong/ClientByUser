package com.android.jidian.repair.mvp.login;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/10 17:11
 * description:
 */
public interface LoginContract {
    interface Model {
        Flowable<LoginBean> requestLoginJdLogin(String phone, String passwd);
    }

    interface View extends BaseView {
        void requestLoginJdLoginSuccess(LoginBean.DataBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestLoginJdLogin(String phone, String passwd);
    }
}
