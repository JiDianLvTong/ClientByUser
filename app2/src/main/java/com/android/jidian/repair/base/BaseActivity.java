package com.android.jidian.repair.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.android.jidian.repair.base.broadcastManage.BroadcastManager;
import com.android.jidian.repair.base.dialog.DialogByLoading;
import com.android.jidian.repair.base.inputManager.InputManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = "JiDianByRepair";
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
        //判断是否需要注册EventBus
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
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
    private void inputInit() {
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
        if (inputManager != null) {
            inputManager.onDestroy();
        }
        if (inputManager != null) {
            broadcastManager.onDestroy();
        }
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }

    }

    /**
     * 默认绑定一个事件，防止源码里面去找方法的时候找不到报错。
     */
    @Subscribe
    public void onEvent(BaseActivity activity) {
    }

}