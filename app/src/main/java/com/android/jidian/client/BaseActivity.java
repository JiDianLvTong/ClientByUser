package com.android.jidian.client;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.jidian.client.widgets.ProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

import me.jessyan.autosize.AutoSizeConfig;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected Activity activity;
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences spNotClear;
    protected String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        spNotClear = getSharedPreferences("userNotClearInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        progressDialog = new ProgressDialog(this);
        // 设置状态栏白色底黑字
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .init();
        Log.d(TAG, "onCreate: " + getClass().getName());
    }

    @Override
    public Resources getResources() {
        //用于解决不让APP字体大小随系统设置字体大小改变，重写getResources与AndroidAutoSize冲突
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        return super.getResources();
    }

    @Override
    protected void onDestroy() {
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}