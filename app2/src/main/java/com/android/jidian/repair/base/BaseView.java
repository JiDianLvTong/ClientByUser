package com.android.jidian.repair.base;

public interface BaseView {

    void showProgress();

    void hideProgress();

    void onError(Throwable throwable);
}