package com.android.jidian.client.util;

import android.content.Context;
import android.util.Log;

import com.android.jidian.client.mvp.ui.activity.login.LoginActivity;
import com.android.jidian.client.widgets.MyToast;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;

import cn.jiguang.verifysdk.api.LoginSettings;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.VerifyListener;

/**
 * @author : PTT
 * date: 2020/9/25 11:28
 * company: 兴达智联
 * description: 极光请求授权一键登录
 */
public class JgVerificationLoginAuthManager {

    /**
     * 使用极光认证预取号
     */
    public static void requestJgVerificationpreLogin(Context mContext) {
        //获取sdk是否整体初始化成功的标识 boolean : true - 成功，false - 失败
        if (!JVerificationInterface.isInitSuccess()) {
            MyToast.showTheToast(mContext, "当前网络环境不支持认证");
            return;
        }
        //判断当前的手机网络环境是否可以使用认证。
        if (!JVerificationInterface.checkVerifyEnable(mContext)) {
            MyToast.showTheToast(mContext, "当前网络环境不支持认证");
            return;
        }

        JVerificationInterface.preLogin(mContext, 5000, new PreLoginListener() {
            @Override
            public void onResult(final int code, final String content) {
                LogUtils.e("极光认证--预取号preLogin--" + "code=" + code + ", content=" + content);
            }
        });
    }

    /**
     * 使用极光一键登录
     */
    public static void requestJgVerificationLogin(Context mContext, onLoginAuthSuccessListener mListener) {
        //清除sdk当前预取号结果缓存
//        JVerificationInterface.clearPreLoginCache();
        //获取sdk是否整体初始化成功的标识 boolean : true - 成功，false - 失败
        if (!JVerificationInterface.isInitSuccess()) {
            MyToast.showTheToast(mContext, "当前网络环境不支持认证");
            return;
        }
        //判断当前的手机网络环境是否可以使用认证。
        if (!JVerificationInterface.checkVerifyEnable(mContext)) {
            MyToast.showTheToast(mContext, "当前网络环境不支持认证");
            return;
        }
        ((LoginActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListener.loginAuthShowLoading();
                LoginSettings settings = new LoginSettings();
                //设置登录完成后是否自动关闭授权页
                settings.setAutoFinish(true);
                //设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
                settings.setTimeout(15 * 1000);
                //设置授权页事件监听
                settings.setAuthPageEventListener(new AuthPageEventListener() {
                    @Override
                    public void onEvent(int cmd, String msg) {
                        Log.e("极光认证2--onEvent--", "cmd=" + cmd + ", msg=" + msg);
                    }
                });
                JVerificationInterface.loginAuth(mContext, settings, new VerifyListener() {
                    @Override
                    public void onResult(int code, String content, String operator) {
                        //code: 返回码，6000代表loginToken获取成功，6001代表loginToken获取失败，其他返回码详见描述
                        Log.e("极光认证--一键登录loginAuth--", "code=" + code + ", token=" + content + " ,operator=" + operator);
                        if (code == 6000) {
                            mListener.loginAuthSuccess(content);
                        } else {
                            mListener.loginAuthFail();
                        }
                    }
                });
            }
        });
    }

    public interface onLoginAuthSuccessListener {
        void loginAuthShowLoading();

        void loginAuthSuccess(String loginToken);

        void loginAuthFail();
    }
}