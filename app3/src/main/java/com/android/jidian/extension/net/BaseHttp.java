package com.android.jidian.extension.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.android.jidian.extension.BuildConfig;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.util.Md5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BaseHttp {

    public interface BaseHttpListener{
        void dataReturn(int code , String errorMsg , String msg , String data);
    }

    private Context context;
    private String url;
    private String returnRawData = "";
    private List<BaseHttpParameterFormat> baseHttpParameterFormats;
    private BaseHttpListener baseHttpListener;
    private boolean isSend = false;
    private int outTime = 10;

    public BaseHttp( Context context,String url , List<BaseHttpParameterFormat> baseHttpParameterFormats , BaseHttpListener baseHttpListener) {
        this.context = context;
        this.url = url;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("data",System.currentTimeMillis()+""));
        this.baseHttpListener = baseHttpListener;
    }

    public BaseHttp( Context context, String url , List<BaseHttpParameterFormat> baseHttpParameterFormats , int outTime , BaseHttpListener baseHttpListener) {
        this.context = context;
        this.url = url;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
        this.outTime = outTime;
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("data",System.currentTimeMillis()+""));
        this.baseHttpListener = baseHttpListener;
    }

    public BaseHttp( Context context , String url , List<BaseHttpParameterFormat> baseHttpParameterFormats) {
        this.context = context;
        this.url = url;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
    }

    public BaseHttp( Context context ,String url , List<BaseHttpParameterFormat> baseHttpParameterFormats , int outTime) {
        this.context = context;
        this.url = url;
        this.outTime = outTime;
        this.baseHttpParameterFormats = baseHttpParameterFormats;
    }

    public void onStart(){
        //??????????????????????????????????????????
        if(!isNetworkConnected(context)){
            returnMessage(-1,"okHttpError - "+ url + " - " + "?????????????????????????????????" , "???????????????????????????????????????" , "");
            return;
        }
        //???????????????????????????okhttp??????????????????????????????
        timeOut();
        //?????????????????????
        OkHttpClient client = new OkHttpClient();
        //?????????????????????
        FormBody.Builder formBodybuilder = new FormBody.Builder();
        boolean isHasUid = false;
        String uid = "";
        for(int i = 0 ; i < baseHttpParameterFormats.size() ; i++){
            if(baseHttpParameterFormats.get(i).getName() == "uid"){
                isHasUid = true;
                uid = baseHttpParameterFormats.get(i).getData();
            }
            formBodybuilder.add(baseHttpParameterFormats.get(i).getName() , baseHttpParameterFormats.get(i).getData());
        }
        FormBody formBody = formBodybuilder.build();
        //????????????????????????
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("aptk" , Md5.getAptk());
        if(isHasUid){
            headerBuilder.add("apud" ,uid);
        }
        headerBuilder.add("proname" , "apu");
        headerBuilder.add("osname" , "Android");
        headerBuilder.add("company" , "jidianlvtong");
        headerBuilder.add("version" , BuildConfig.VERSION_CODE + "");
        headerBuilder.add("ACCESS" , UserInfoSp.getInstance().getAppToken());
        Headers headers = headerBuilder.build();
        //???????????????
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .headers(headers)
                .build();
        //????????????
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("okHttpError???   "+ url + "   " + e.toString());
                returnMessage( -1, "okHttpError???   "+ url + "   " + e.toString() , "???????????????????????????????????????","");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String body = "";
                    for(int i = 0 ; i < baseHttpParameterFormats.size() ; i++){
                        body = body + baseHttpParameterFormats.get(i).getName() + " - " + baseHttpParameterFormats.get(i).getData() + "   ";
                    }
                    System.out.println("okHttpSuccess - "+url+" ???   " + jsonObject);
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
                    System.out.println("okHttpError???   "+ url + "   " + e.toString());
                    returnMessage( -1, "okHttpError???   "+ url + "   " + e.toString() , "???????????????????????????????????????","");
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
                    sleep(1000 * outTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                returnMessage( -1, "okHttpError???   "+ url + "   " + "http????????????","???????????????????????????????????????","");
            }
        };
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

    public String getReturnRawData() {
        return returnRawData;
    }

    private void returnMessage(int code , String errorMessage , String message , String data){
        if(isSend == false){
            isSend = true;
            if(baseHttpListener!=null){
                baseHttpListener.dataReturn(code,errorMessage , message ,data);
            }
        }
    }

}
