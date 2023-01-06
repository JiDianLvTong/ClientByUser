package com.android.jidian.client.mvp.ui.activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.android.jidian.client.mvp.ui.dialog.CommonTipDialog;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.JgVerificationLoginAuthManager;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyToast;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2017/6/5.
 * 登录界面 activity
 */

//todo:点击inputview没有光标

public class LoginActivity extends U6BaseActivityByMvp<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.get_number_text)
    TextView tvPhoneCode;
    @BindView(R.id.login_phone)
    EditText edtLoginPhone;
    @BindView(R.id.login_number)
    EditText edtVerificationCode;
    @BindView(R.id.login_read)
    ImageView btnExplain;

    Handler setTimeHandler;
    private boolean isExplainUnRead = false;
    private String confirmStr = "";
    private boolean isOwnLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        handler();

        new Handler().postDelayed(() -> {
            //登录页面访问
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_LOGIN);
        }, 500);
        JgVerificationLoginAuthManager.requestJgVerificationpreLogin(this);
    }

    private void handler() {
        setTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int time_str = msg.getData().getInt("time");
                if (tvPhoneCode != null) {
                    if (time_str == 0) {
                        tvPhoneCode.setText("获取验证码");
                        tvPhoneCode.setClickable(true);
                    } else {
                        tvPhoneCode.setText(time_str + " S");
                        tvPhoneCode.setClickable(false);
                    }
                }
            }
        };
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @OnClick({R.id.submit, R.id.page_return, R.id.get_number_text, R.id.login_read, R.id.rule1, R.id.rule2, R.id.btn_JVerificationLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.page_return:
                this.finish();
                break;
            case R.id.btn_JVerificationLogin:
                //点击一键登录
                requestJg();
                break;
            case R.id.submit:
                //点击登录
                if (isExplainUnRead) {
                    isOwnLogin = false;
                    clickSureReqyestLogin("1");
                } else {
                    MyToast.showTheToast(activity, "请先阅读用户协议和隐私政策");
                }
                break;
            case R.id.get_number_text:
                //点击获取验证码
                String mobileStr2 = edtLoginPhone.getText().toString().trim();
                if (TextUtils.isEmpty(mobileStr2)) {
                    MyToast.showTheToast(this, "手机号不能为空！");
                } else if (11 == mobileStr2.length()) {
                    if (mPresenter != null) {
                        mPresenter.sendVerificationCode(mobileStr2);
                    }
                } else {
                    MyToast.showTheToast(this, "请输入正确的手机格式！");
                }
                break;
            case R.id.login_read:
                if (isExplainUnRead) {
                    btnExplain.setImageResource(R.drawable.u6_pub_agreement_uncheck);
                    isExplainUnRead = false;
                } else {
                    btnExplain.setImageResource(R.drawable.u6_pub_agreement_check);
                    isExplainUnRead = true;
                }
                break;
            case R.id.rule1:
                startActivity(new Intent(activity, MainAgreement.class));
                break;
            case R.id.rule2:
                startActivity(new Intent(activity, MainPrivacyClause.class));
                break;
            default:
                break;
        }
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
                clickJgSureReqyestLogin(loginToken);
            }

            @Override
            public void loginAuthFail() {
                progressDialog.dismiss();
                showMessage("登陆失败");
            }
        });
    }

    @Override
    public void sendVerificationCodeResult(String msg) {
        MyToast.showTheToast(this, msg);
        TimerCount timerCount = new TimerCount(60, setTimeHandler);
        if (!timerCount.isAlive()) {
            timerCount.start();
        }
    }

    /**
     * 请求登录接口
     */
    private void clickSureReqyestLogin(String confirm) {
        String mobileStr = edtLoginPhone.getText().toString().trim();
        String verificationCodeStr = edtVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobileStr)) {
            MyToast.showTheToast(this, "手机号不能为空！");
        } else if (TextUtils.isEmpty(verificationCodeStr)) {
            MyToast.showTheToast(this, "验证码不能为空！");
        } else if (11 != mobileStr.length()) {
            MyToast.showTheToast(this, "请输入正确的手机格式！");
        } else {
            if (mPresenter != null) {
                String appsn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
                mPresenter.requestLogin(mobileStr, verificationCodeStr, confirm, appsn);
            }
        }
    }

    /**
     * 请求极光登录接口
     */
    private void clickJgSureReqyestLogin(String loginTokenStr) {
        if (mPresenter != null) {
            String appsn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
            mPresenter.sendJgVerificationLogin(loginTokenStr, confirmStr, appsn);
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
                    new CommonTipDialog().init(bean.getMsg(), "确认登录", () -> {
                        //点击确认登录
                        if (isOwnLogin) {
                            requestJg();
                        } else {
                            clickSureReqyestLogin(confirmStr);
                        }
                    }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).show(getSupportFragmentManager());
                }
            }
        } else {
            MyToast.showTheToast(this, bean.getMsg());
        }
    }

    @Override
    public void requestLoginError(String msg) {
        MyToast.showTheToast(this, msg);
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
            //通知主页刷新数据
//            Main.handleRefreshMarker.sendEmptyMessage(1);
//            MyToast.showTheToast(this, "正在刷新站点数据，请稍候");
            MyToast.showTheToast(this, msg);
            activity.finish();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static final String appSnFilePath = "/sdcard/Gyt/";
    public static final String appSnFileName = "userAppSn.txt";

    private void httpReLoginSuccess(String str) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this, R.style.Translucent_NoTitle);
        View view = inflater.inflate(R.layout.alertdialog_bind, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(str);
        TextView success_t = view.findViewById(R.id.success);
        success_t.setText("立即恢复");
        success_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSureReqyestLogin("2");
                dialog.dismiss();
            }
        });
        TextView error_t = view.findViewById(R.id.error);
        error_t.setText("注册新账号");
        error_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSureReqyestLogin("");
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onError(Throwable throwable) {
        MyToast.showTheToast(this, throwable.getLocalizedMessage());
    }

    class TimerCount extends Thread {
        int count = 0;
        Handler handler;

        public TimerCount(int count, Handler handler) {
            this.count = count;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            while (count > 0) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                count = count - 1;
                if (handler != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("time", count);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}