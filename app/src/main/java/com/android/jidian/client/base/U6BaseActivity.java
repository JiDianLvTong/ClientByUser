package com.android.jidian.client.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.view.MotionEvent;

import com.android.jidian.client.base.broadcastManage.BroadcastManager;
import com.android.jidian.client.base.inputManager.InputManager;
import com.android.jidian.client.mvp.ui.dialog.DialogByLoading;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class U6BaseActivity extends AppCompatActivity {

    protected static final String Tag = "JiDianByClient";
    //上下文
    protected FragmentActivity activity;
    //软键盘
    protected InputManager inputManager;
    //butterKnife
    protected Unbinder unbinder;
    //appToken
    protected String apptoken;
    //广播
    protected BroadcastManager broadcastManager;
    //提示框
    protected DialogByLoading progressDialog;
    //用户数据
    protected SharedPreferences sharedPreferences;
    protected String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        apptoken = sharedPreferences.getString("apptoken", "");

        progressDialog = new DialogByLoading(this);

        unbinder = ButterKnife.bind(this);

        //初始化广播
        registerFinishReceiver();
        //初始化键盘
        inputInit();
    }

    //广播注册
    protected void registerFinishReceiver() {
        broadcastManager = new BroadcastManager(activity);
    }

    //键盘初始化
    private void inputInit(){
        inputManager = new InputManager(activity);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            ev = inputManager.touchNullDismissInput(ev);
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if(inputManager!=null){
            inputManager.onDestroy();
        }
        if(inputManager!=null){
            broadcastManager.onDestroy();
        }

    }
}