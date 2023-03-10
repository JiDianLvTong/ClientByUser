package com.android.jidian.repair.net;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.main.UpdateVersionBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author : xiaoming
 * date: 2023/2/6 17:52
 * description:
 */
public interface APIService {
    /**
     * 获取验证码接口
     */
    @FormUrlEncoded
    @POST("Sms/msgSend.html")
    Flowable<BaseBean> requestVerificationCode(@Field("phone") String phone);

    /**
     * 请求app更新
     */
    @FormUrlEncoded
    @POST("AppVer/version.html")
    Flowable<UpdateVersionBean> requestAppUpdateVersion(@Field("uid") String uid);
}
