package com.android.jidian.client.mvp.ui.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.mvp.contract.LoginContract;
import com.android.jidian.client.mvp.presenter.LoginPresenter;
import com.android.jidian.client.mvp.ui.activity.h5.MainAgreement;
import com.android.jidian.client.mvp.ui.activity.h5.MainPrivacyClause;
import com.android.jidian.client.mvp.ui.activity.main.MainActivityEvent;
import com.android.jidian.client.mvp.ui.dialog.DialogByChoice;
import com.android.jidian.client.mvp.ui.dialog.DialogByEnter;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.JgVerificationLoginAuthManager;
import com.android.jidian.client.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends U6BaseActivityByMvp<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.getNumber)
    public TextView getNumber;
    @BindView(R.id.ruleByUser)
    public TextView ruleByUser;
    @BindView(R.id.ruleByPrivacy)
    public TextView ruleByPrivacy;
    @BindView(R.id.jPushVerificationLogin)
    public TextView jPushVerificationLogin;

    @BindView(R.id.login_phone)
    public EditText edtLoginPhone;
    @BindView(R.id.login_number)
    public EditText edtVerificationCode;
    @BindView(R.id.isRead)
    public ImageView isRead;

    private boolean isReadSelect = false;

    private String confirmStr = "";
    private boolean isOwnLogin = false;

    private Thread timeThread = null;
    private boolean timeState = true;
    private int timeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
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
        //极光登录
        JgVerificationLoginAuthManager.requestJgVerificationpreLogin(this);
    }

    //获取短信接口返回
    @Override
    public void sendVerificationCodeResult(String msg) {
        timeCount = 60;
    }

    //网络错误返回
    @Override
    public void requestLoginError(String msg) {
        new DialogByEnter(activity , msg).showPopupWindow();
    }

    /**
     * 调起极光一键登录
     */
    private void requestJg() {
        JgVerificationLoginAuthManager.requestJgVerificationLogin(this, new JgVerificationLoginAuthManager.onLoginAuthSuccessListener() {
            @Override
            public void loginAuthShowLoading() {
                progressDialog.show();
            }

            @Override
            public void loginAuthSuccess(String loginToken) {
                progressDialog.dismiss();
                isOwnLogin = true;
                loginByJPush(loginToken);
            }

            @Override
            public void loginAuthFail() {
                progressDialog.dismiss();
                showMessage("登陆失败");
            }
        });
    }

    private void loginByCode(String confirm) {
        String mobileStr = edtLoginPhone.getText().toString().trim();
        String verificationCodeStr = edtVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobileStr)) {
            new DialogByEnter(activity ,  "手机号不能为空！").showPopupWindow();
        } else if (TextUtils.isEmpty(verificationCodeStr)) {
            new DialogByEnter(activity ,  "验证码不能为空！").showPopupWindow();
        } else if (11 != mobileStr.length()) {
            new DialogByEnter(activity ,  "请输入正确的手机格式！").showPopupWindow();
        } else {
            if (mPresenter != null) {
                String appsn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
                mPresenter.requestLogin(mobileStr, verificationCodeStr, confirm, appsn);
            }
        }
    }

    private void loginByJPush(String loginTokenStr) {
        if (mPresenter != null) {
            String appSn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
            mPresenter.sendJgVerificationLogin(loginTokenStr, confirmStr, appSn);
        }
    }


    @Override
    public void requestLoginSuccess(LoginBean bean) {
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_LOGIN_SUCCESS);
        if ("1".equals(bean.getStatus())) {
            if (bean.getData() != null) {
                setSuccessJump(bean.getMsg(), bean.getData());
            }
        } else if ("2".equals(bean.getStatus())) {
            httpReLoginSuccess(bean.getMsg());
        } else if ("-10".equals(bean.getStatus())) {
            //status 0=失败；1=成功 -10=登录二次确认
            //confirm 二次确认是回传的参数和值
            if (bean.getData() != null) {
                if (!TextUtils.isEmpty(bean.getData().getConfirm())) {
                    confirmStr = bean.getData().getConfirm();
                    new DialogByChoice(activity, bean.getMsg(), new DialogByChoice.DialogChoiceListener() {
                        @Override
                        public void enterReturn() {
                            //点击确认登录
                            if (isOwnLogin) {
                                requestJg();
                            } else {
                                loginByCode(confirmStr);
                            }
                        }

                        @Override
                        public void cancelReturn() {

                        }
                    }).showPopupWindow();
                }
            }
        } else {
            new DialogByEnter(activity ,  bean.getMsg()).showPopupWindow();
        }
    }

    /**
     * 登录成功
     */
    private void setSuccessJump(String msg, LoginBean.DataBean bean) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", bean.getId());
            editor.putString("access", bean.getAccess());
            editor.putString("phone", bean.getPhone());
            editor.putString("real_name", bean.getReal_name());
            editor.putString("nick_name", bean.getNick_name());
            editor.putString("user_avatar", bean.getAvater());
            editor.putString("last_time", bean.getLast_time());
            editor.putString("last_ip", bean.getLast_ip());
            editor.putString("reg_time", bean.getReg_time());
            editor.putString("agreement", bean.getAgreement());
            editor.putString("world", bean.getWorld());
            editor.putString("auth", bean.getAuth());
            editor.putString("apptoken", bean.getApptoken());
            editor.apply();
            apptoken = bean.getApptoken();
            String appsn = Util.getFileContent(new File(appSnFilePath + appSnFileName));
            if (TextUtils.isEmpty(appsn)) {
                Util.writeTxtToFile(bean.getAppsn(), appSnFilePath, appSnFileName);
            }
            if (mPresenter != null) {
                mPresenter.sendHttpVisitLogs(bean.getId());
            }
            EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.LOGIN_SUCCESS));
            activity.finish();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static final String appSnFilePath = "/sdcard/Gyt/";
    public static final String appSnFileName = "userAppSn.txt";

    private void httpReLoginSuccess(String str) {

        new DialogByChoice(activity, "str", "立即恢复", "注册新账号", new DialogByChoice.DialogChoiceListener() {
            @Override
            public void enterReturn() {
                loginByCode("2");
            }

            @Override
            public void cancelReturn() {
                loginByCode("");
            }
        }).showPopupWindow();
    }

    @Override
    public void onError(Throwable throwable) {
        new DialogByEnter(activity , throwable.getLocalizedMessage()).showPopupWindow();
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        activity.finish();
    }

    @OnClick(R.id.getNumber)
    public void onClickGetNumber(){
        //点击获取验证码
        String phoneStr = edtLoginPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)) {
            new DialogByEnter(activity , "手机号不能为空！").showPopupWindow();
        } else if (11 == phoneStr.length()) {
            if (mPresenter != null) {
                mPresenter.sendVerificationCode(phoneStr);
            }
        } else {
            new DialogByEnter(activity , "请输入正确的手机格式！").showPopupWindow();
        }
    }

    @OnClick(R.id.isRead)
    public void onClickIsRead(){
        if (isReadSelect) {
            isRead.setImageResource(R.drawable.u6_pub_select_uncheck);
            isReadSelect = false;
        } else {
            isRead.setImageResource(R.drawable.u6_pub_select_check);
            isReadSelect = true;
        }
    }

    @OnClick(R.id.ruleByPrivacy)
    public void onClickRuleByPrivacy(){
        activity.startActivity(new Intent(activity , MainPrivacyClause.class));
    }

    @OnClick(R.id.ruleByUser)
    public void onClickRuleByUser(){
        activity.startActivity(new Intent(activity , MainAgreement.class));
    }

    @OnClick(R.id.jPushVerificationLogin)
    public void onClickJPushVerificationLogin(){
        requestJg();
    }

    @OnClick(R.id.submit)
    public void onClickSubmit(){
        if (isReadSelect) {
            isOwnLogin = false;
            loginByCode("1");
        } else {
            new DialogByEnter(activity , "请先阅读用户协议和隐私政策").showPopupWindow();
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeState = false;
    }
}