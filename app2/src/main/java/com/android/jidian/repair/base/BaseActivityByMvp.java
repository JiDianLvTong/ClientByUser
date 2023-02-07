package com.android.jidian.repair.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.android.jidian.repair.widgets.dialog.DialogByToast;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivityByMvp<T extends BasePresenter> extends BaseActivity {

    protected T mPresenter;
    public CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(getLayoutId());
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        initView();
        ActivityCollector.addActivity(this);
    }

    protected void showMessage(String msg) {
        DialogByToast.showTheToast(this, msg);
    }

    public abstract int getLayoutId();

    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}