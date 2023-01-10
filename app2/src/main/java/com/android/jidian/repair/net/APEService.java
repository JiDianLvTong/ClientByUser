package com.android.jidian.repair.net;

import com.android.jidian.repair.mvp.login.LoginBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:06
 * description:
 */
public interface APEService {
    //更新接口
    @FormUrlEncoded
    @POST("Login/jdLogin.html")
    Flowable<LoginBean> requestLoginJdLogin(@Field("phone") String phone, @Field("passwd") String passwd);
}
