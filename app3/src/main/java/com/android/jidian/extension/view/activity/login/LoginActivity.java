package com.android.jidian.extension.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseActivity;
import com.android.jidian.extension.bean.LoginGetUserInfoBean;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.net.BaseHttp;
import com.android.jidian.extension.net.BaseHttpParameterFormat;
import com.android.jidian.extension.net.HttpUrlMap;
import com.android.jidian.extension.util.DataVerification;
import com.android.jidian.extension.view.activity.main.MainActivity;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByEnter;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.getNumber)
    public TextView getNumber;
    @BindView(R.id.submitView)
    public TextView submitView;

    @BindView(R.id.phoneView)
    public EditText phoneView;
    @BindView(R.id.verCodeView)
    public EditText verCodeView;

    private boolean isReadStatus = false;

    private String confirmStr = "";
    private boolean isOwnLogin = false;

    private Thread timeThread = null;
    private boolean timeState = true;
    private int timeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(activity);
        initView();
    }

    public void initView() {

        if(UserInfoSp.getInstance().isLogin()){
            activity.startActivity(new Intent(activity , MainActivity.class));
            activity.finish();
            return;
        }

        //时间线程
        if(timeThread == null){
            timeThread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while(timeState){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(timeCount > 0){
                                    getNumber.setText(timeCount + " S");
                                    getNumber.setClickable(false);
                                    timeCount = timeCount - 1;
                                }else{
                                    getNumber.setText("获取验证码");
                                    getNumber.setClickable(true);
                                }
                            }
                        });
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            timeThread.start();
        }
    }

    @OnClick(R.id.getNumber)
    public void onClickGetNumber(){
        String phoneStr = phoneView.getText().toString().trim();
        dialogLoading.showPopupWindow();
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("phone",phoneStr));
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.getMsg, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoading.dismiss();
                        if(code == 1){
                            new DialogByEnter(activity,"短信发送成功，请注意查收验证码").showPopupWindow();
                        }else{
                            new DialogByEnter(activity,message).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }


    @OnClick(R.id.submitView)
    public void onClickSubmit(){
        dialogLoading.showPopupWindow();
        String phoneStr = phoneView.getText().toString().trim();
        String verCode = verCodeView.getText().toString().toString();
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("phone",phoneStr));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("vcode",verCode));
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.login, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoading.dismiss();
                        if(code == 1){
                            LoginGetUserInfoBean loginGetUserInfoBean = new Gson().fromJson(data , LoginGetUserInfoBean.class);
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.uid , loginGetUserInfoBean.getId());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.apptoken , loginGetUserInfoBean.getApptoken());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.realname , loginGetUserInfoBean.getReadlName());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.phone , loginGetUserInfoBean.getPhone());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.isfirst , loginGetUserInfoBean.getIsfirst());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.avater , loginGetUserInfoBean.getAvater());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.qrcode , loginGetUserInfoBean.getQrcode());
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        }else{
                            new DialogByEnter(activity,message).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeState = false;
    }
}