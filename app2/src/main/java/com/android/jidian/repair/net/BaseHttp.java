package com.android.jidian.repair.net;


import static com.android.jidian.repair.MyApplication.getLocalVersion;
import static com.android.jidian.repair.MyApplication.getLocalVersionName;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;

import com.android.jidian.repair.MyApplication;
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.utils.Md5;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BaseHttp {

    public interface BaseHttpListener{
        void dataReturn(int code , String message , String data);
    }

    public interface BaseHttpErrorMessageListener{
        void dataReturn(int code , String message , String errorMessage ,  String data);
    }


    private String url;
    private Context context;
    private List<BaseHttpParameterFormat> baseHttpParameterFormats;
    private BaseHttpListener baseHttpListener;
    private BaseHttpErrorMessageListener baseHttpErrorMessageListener;
    private boolean isSend = false;
    private int outTime = 10;
    private String returnRawData = "";

    public BaseHttp(Context context, String url , List<BaseHttpParameterFormat> baseHttpParameterFormats , BaseHttpListener baseHttpListener) {
        this.url = url;
        this.context = context;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("data",System.currentTimeMillis()+""));
        this.baseHttpListener = baseHttpListener;
    }

    public BaseHttp(Context context,String url , List<BaseHttpParameterFormat> baseHttpParameterFormats , int outTime , BaseHttpListener baseHttpListener) {
        this.url = url;
        this.context = context;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
        this.outTime = outTime;
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("data",System.currentTimeMillis()+""));
        this.baseHttpListener = baseHttpListener;
    }

    public BaseHttp(Context context,String url , List<BaseHttpParameterFormat> baseHttpParameterFormats ,int outTime,  BaseHttpErrorMessageListener baseHttpErrorMessageListener) {
        this.url = url;
        this.context = context;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
        this.outTime = outTime;
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("data",System.currentTimeMillis()+""));
        this.baseHttpErrorMessageListener = baseHttpErrorMessageListener;
    }

    public BaseHttp(Context context,String url , List<BaseHttpParameterFormat> baseHttpParameterFormats) {
        this.url = url;
        this.context = context;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
    }

    public BaseHttp(Context context ,String url , List<BaseHttpParameterFormat> baseHttpParameterFormats , int outTime) {
        this.url = url;
        this.context = context;
        this.outTime = outTime;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
    }

    public void onStart(){

        timeOut();

        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        //本地无网络，进行提示，并返回
        if(!isNetworkConnected(context)){
            returnMessage(-1,"okHttpError - "+ url + " - " + "网络异常，请稍候再试！" , "本地网络异常，请稍候再试！" , "");
            return;
        }
        //设置网络请求超时，okhttp附带的超时不一定有用
        timeOut();
        //初始化本地请求
        OkHttpClient client = new OkHttpClient();
        //初始化请求本体
        FormBody.Builder formBodybuilder = new FormBody.Builder();

        for(int i = 0 ; i < baseHttpParameterFormats.size() ; i++){
            formBodybuilder.add(baseHttpParameterFormats.get(i).getName() , baseHttpParameterFormats.get(i).getData());
        }
        FormBody formBody = formBodybuilder.build();

        //初始化请求头文件
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("aptk", Md5.getAptk());
        headerBuilder.add("apud", UserInfoSp.getInstance().getId());
        headerBuilder.add("osname", "Android");
        headerBuilder.add("proname", "ape");
        headerBuilder.add("verName",  getLocalVersionName(MyApplication.getAppContext()));
        headerBuilder.add("version", String.valueOf(getLocalVersion(MyApplication.getAppContext())));
        headerBuilder.add("company", "jidianlvtong");
        headerBuilder.add("device", Build.MODEL);
        headerBuilder.add("systemver", Build.VERSION.RELEASE);
        Headers headers = headerBuilder.build();

        //初始化请求
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .headers(headers)
                .build();
        //进行请求
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("okHttpError：   "+ url + "   " + e.toString());
                returnMessage( -1, "okHttpError：   "+ url + "   " + e.toString() , "网络请求错误，请稍候再试！","");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                System.out.println("ok - " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String body = "";
                    for(int i = 0 ; i < baseHttpParameterFormats.size() ; i++){
                        body = body + baseHttpParameterFormats.get(i).getName() + " - " + baseHttpParameterFormats.get(i).getData() + "   ";
                    }
                    System.out.println("okHttpSuccess - "+url+" ：   " + jsonObject);
                    returnRawData = result;
                    String messageString = jsonObject.getString("msg");
                    String status = jsonObject.getString("status");
                    String data = "";
                    String error = "";
                    if(jsonObject.has("errno")){
                        error = jsonObject.getString("errno");
                    }else{
                        error = "";
                    }
                    if(status.equals("0")){
                        data = jsonObject.toString();
                    }else{
                        if(jsonObject.has("data")){
                            data = jsonObject.getString("data");
                        }else{
                            data = jsonObject.toString();
                        }
                    }
                    returnMessage( Integer.parseInt(status),error,messageString,data);
                } catch (JSONException e) {
                    System.out.println("okHttpError：   "+ url + "   " + e.toString());
                    returnMessage( -1, "okHttpError：   "+ url + "   " + e.toString() , "网络解析错误，请稍候再试！","");
                }
            }
        });
    }

    private void timeOut(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                returnMessage( -1, "okHttpError：   "+ url + "   " + "http请求超时","","");
            }
        };
    }

    private void returnMessage(int code , String message , String errorMessage , String data){
        if(isSend == false){
            isSend = true;
            if(baseHttpListener!=null){
                baseHttpListener.dataReturn(code,message,data);
            }
            if(baseHttpErrorMessageListener!=null){
                baseHttpErrorMessageListener.dataReturn(code,errorMessage,errorMessage,data);
            }
        }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
