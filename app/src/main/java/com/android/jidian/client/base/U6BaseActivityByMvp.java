package com.android.jidian.client.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.jidian.client.base.broadcastManage.BroadcastManager;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.widgets.ProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.jessyan.autosize.AutoSizeConfig;

public abstract class U6BaseActivityByMvp<T extends BasePresenter> extends U6BaseActivity {

    protected T mPresenter;
    public CompositeDisposable compositeDisposable;
    private Unbinder unbinder;
    protected String apptoken;
    protected BroadcastManager broadcastManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        apptoken = sharedPreferences.getString("apptoken", "");
        progressDialog = new ProgressDialog(this);
        unbinder = ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();

        registerFinishReceiver();
        initView();
        Log.d(TAG, "onCreate: " + getClass().getName());
    }

    //广播注册
    protected void registerFinishReceiver() {
        broadcastManager = new BroadcastManager(activity);
    }

    /**
     * 显示消息
     */
    protected void showMessage(String msg) {
        MyToast.showTheToast(this, msg);
    }

    @Override
    protected void onDestroy() {
        broadcastManager.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }

    /**
     * 初始化视图
     */
    public abstract void initView();
}