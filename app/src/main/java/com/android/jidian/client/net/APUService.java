package com.android.jidian.client.net;

import com.android.jidian.client.bean.BaseBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author : xiaoming
 * date: 2023/3/3 09:55
 * description:
 */
public interface APUService {
    //客商扫码
    @FormUrlEncoded
    @POST("User/scanBind.html")
    Flowable<BaseBean> requestUserScanBind(@Field("qrstr") String qrstr);
}
