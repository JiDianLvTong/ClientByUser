package com.android.jidian.client.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.jidian.client.R;
import com.android.jidian.client.base.broadcastManage.BroadcastManager;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.widgets.ProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.jessyan.autosize.AutoSizeConfig;

public abstract class BaseActivity2<T extends BasePresenter> extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected T mPresenter;
    public CompositeDisposable compositeDisposable;
    private Unbinder unbinder;

    protected Activity activity;
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected String uid;
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
        // 设置状态栏白色底黑字
//        ImmersionBar.with(this)
//                .fitsSystemWindows(true)
//                .statusBarColor(R.color.white)
//                .statusBarDarkFont(true)
//                .init();
        registerFinishReceiver();
        initView();
        Log.d(TAG, "onCreate: " + getClass().getName());
    }

    @Override
    public Resources getResources() {
        //用于解决不让APP字体大小随系统设置字体大小改变，重写getResources与AndroidAutoSize冲突
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        return super.getResources();
    }

    //广播注册
    protected void registerFinishReceiver() {
        broadcastManager = new BroadcastManager(activity);
    }

    /**
     * 添加订阅
     */
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 取消所有订阅
     */
    public void clearDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /**
     * 显示消息
     */
    protected void showMessage(String msg) {
        MyToast.showTheToast(this, msg);
    }

    @Override
    protected void onDestroy() {
        clearDisposable();
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