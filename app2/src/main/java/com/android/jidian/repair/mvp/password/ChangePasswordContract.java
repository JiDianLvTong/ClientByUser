package com.android.jidian.repair.mvp.password;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/6 17:04
 * description:
 */
public interface ChangePasswordContract {
    interface Model{
        Flowable<BaseBean> requestVerificationCode(String phone);

        Flowable<BaseBean> requestResetLoginPwd(String phone, String newpwd, String vcode);
    }

    interface View extends BaseView {
        void requestResetLoginPwdSuccess(BaseBean bean);

        void requestShowTips(String msg);
    }
    interface Presenter{
        void requestVerificationCode(String phone);

        void requestResetLoginPwd(String phone, String newpwd, String vcode);
    }
}
