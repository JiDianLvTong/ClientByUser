package com.android.jidian.client.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.widgets.ProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class U6BaseFragmentByMvp<T extends BasePresenter> extends U6BaseFragment{

    public T mPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}