package com.android.jidian.repair.mvp.password;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/6 17:05
 * description:
 */
public class ChangePasswordModel implements ChangePasswordContract.Model{
    @Override
    public Flowable<BaseBean> requestVerificationCode(String phone) {
        return RetrofitClient.getInstance().getApiService().requestVerificationCode(phone);
    }

    @Override
    public Flowable<BaseBean> requestResetLoginPwd(String phone, String newpwd, String vcode) {
        return RetrofitClient.getInstance().getApeService().requestResetLoginPwd(phone, newpwd, vcode);
    }
}
