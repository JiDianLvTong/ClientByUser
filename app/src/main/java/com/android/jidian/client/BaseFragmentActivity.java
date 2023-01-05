package com.android.jidian.client;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.jidian.client.widgets.ProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

public class BaseFragmentActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";
    protected Activity activity;
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
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
    protected void onDestroy() {
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}