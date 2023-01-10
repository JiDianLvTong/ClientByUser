package com.android.jidian.repair.mvp.login;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/10 17:13
 * description:
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public Flowable<LoginBean> requestLoginJdLogin(String phone, String passwd) {
        return RetrofitClient.getInstance().getApeService().requestLoginJdLogin(phone, passwd);
    }
}
