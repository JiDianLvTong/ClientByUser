package com.android.jidian.extension.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.jidian.extension.base.broadcastManage.BroadcastManager;
import com.android.jidian.extension.base.inputManager.InputManager;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByLoading;

import java.lang.reflect.Field;

public class BaseActivity extends AppCompatActivity {

    protected Activity activity;

    protected BroadcastManager broadcastManager;
    protected InputManager inputManager;
    protected DialogByLoading dialogLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        init();

    }

    private void init(){
        dialogLoading = new DialogByLoading(activity);
        statusBars();
        registerFinishReceiver();
        inputInit();
    }

    //保证导航栏沉浸 并且 保持底部导航栏不被沉浸
    //需要和 style 配合
    private void statusBars(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) try {
            Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
            Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
            field.setAccessible(true);
            field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
        } catch (Exception e) {
        }
        //去除灰色遮罩
        //Android5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){//Android4.4以上,5.0以下
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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

    //广播注册
    protected void registerFinishReceiver() {
        broadcastManager = new BroadcastManager(activity);
    }

    //多次点击启动activity
    boolean clickable = true;
    protected Boolean isClickable() {
        return clickable;
    }
    protected void lockClick() {
        clickable = false;
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (isClickable()) {
            lockClick();
            super.startActivityForResult(intent, requestCode, options);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        clickable = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.onDestroy();

        inputManager.onDestroy();
        dialogLoading.onDestroy();
        dialogLoading = null;
    }
}
