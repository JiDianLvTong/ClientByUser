package com.android.jidian.repair.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

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
    }

    protected void showMessage(String msg) {
        MyToast.showTheToast(this, msg);
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