package com.android.jidian.repair.net;

import static com.android.jidian.repair.MyApplication.getLocalVersion;
import static com.android.jidian.repair.MyApplication.getLocalVersionName;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.android.jidian.repair.MyApplication;
import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.utils.LoggingInterceptor;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
                    .addHeader("aptk", Md5.getAptk())
                    .addHeader("apud", UserInfoHelper.getInstance().getUid())
                    .addHeader("osname", "Android")
                    .addHeader("proname", "ape")
                    .addHeader("verName",  getLocalVersionName(MyApplication.getAppContext()))
                    .addHeader("version", String.valueOf(getLocalVersion(MyApplication.getAppContext())))
                    .addHeader("company", "jidianlvtong")
                    .addHeader("device", Build.MODEL)
                    .addHeader("systemver", Build.VERSION.RELEASE);
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

    public APEService getApeService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(PubFunction.ape)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APEService.class);
    }

    public APIService getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(PubFunction.api)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APIService.class);
    }

    public APEService getAppUploadImgService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(PubFunction.upload)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APEService.class);
    }

}