package com.android.jidian.client.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.android.jidian.client.R;
import com.android.jidian.client.base.inputManager.InputManager;
import com.android.jidian.client.widgets.ProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

import me.jessyan.autosize.AutoSizeConfig;

public class U6BaseActivity extends AppCompatActivity {

    protected static final String TAG = "BaseActivity";
    protected Activity activity;

    //软键盘
    protected InputManager inputManager;

    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        progressDialog = new ProgressDialog(this);

        inputInit();
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
        inputManager.onDestroy();
        super.onDestroy();
    }
}