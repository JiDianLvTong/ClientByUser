package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.mvp.contract.LoginContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class LoginModel implements LoginContract.Model {
    @Override
    public Flowable<BaseBean> sendVerificationCode(String phone) {
        return RetrofitClient.getInstance().getAppServiceApi().sendVerificationCode(phone);
    }

    @Override
    public Flowable<LoginBean> requestLogin(String phone, String vcode, String confirm, String appsn) {
        return RetrofitClient.getInstance().getApiService().requestLogin(phone, vcode, confirm, appsn);
    }

    @Override
    public Flowable<LoginBean> sendJgVerificationLogin(String token, String confirm, String appsn) {
        return RetrofitClient.getInstance().getApiService().sendJgVerificationLogin(token, confirm, appsn);
    }

    @Override
    public Flowable<BaseBean> sendHttpVisitLogs(String uid) {
        return RetrofitClient.getInstance().getAppServiceApi().sendHttpVisitLogs("登录", "5", uid, "109", "Android");
    }
}