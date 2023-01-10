package com.android.jidian.repair.mvp.main;

import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;

public class MainActivity extends BaseActivityByMvp<MainPresenter> implements MainContract.View {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}