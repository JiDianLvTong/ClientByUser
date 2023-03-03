package com.android.jidian.client.net;

import android.os.Build;

import com.android.jidian.client.application.MyApplication;
import com.android.jidian.client.pub.Md5;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.LoggingInterceptor;
import com.android.jidian.client.util.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.jidian.client.http.HeaderTypeData.getLocalVersion;
import static com.android.jidian.client.http.HeaderTypeData.getLocalVersionName;

public class RetrofitClient {

    private static volatile RetrofitClient instance;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    private Interceptor getHeaderInterceptor() {
        return chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("apud", UserInfoHelper.getInstance().getUid())
                    .addHeader("access", UserInfoHelper.getInstance().getAccess())
                    .addHeader("aptk", Md5.getAptk())
                    .addHeader("verName", getLocalVersionName(MyApplication.getContext()))
                    .addHeader("version", String.valueOf(getLocalVersion(MyApplication.getContext())))
                    .addHeader("osname", "Android")
                    .addHeader("SERV_ENV", PubFunction.getLevel())
                    .addHeader("proname", "app")
                    .addHeader("device", Build.MODEL)
                    .addHeader("systemver", Build.VERSION.RELEASE)
                    .addHeader("company", "jidianlvtong");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    private OkHttpClient genericClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getHeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public APIService getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(PubFunction.app)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APIService.class);
    }

    public APIService getAppServiceApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(PubFunction.api)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APIService.class);
    }

    public APUService getApuService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(PubFunction.apu)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APUService.class);
    }

}