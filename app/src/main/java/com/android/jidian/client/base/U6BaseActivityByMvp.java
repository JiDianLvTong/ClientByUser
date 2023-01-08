package com.android.jidian.client.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.android.jidian.client.widgets.MyToast;

import io.reactivex.disposables.CompositeDisposable;

public abstract class U6BaseActivityByMvp<T extends BasePresenter> extends U6BaseActivity {

    protected T mPresenter;
    public CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        initView();
    }

    protected void showMessage(String msg) {
        MyToast.showTheToast(this, msg);
    }

    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}