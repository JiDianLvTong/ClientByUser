package com.android.jidian.client.base;

public interface BaseView {

    void showProgress();

    void hideProgress();

    void onError(Throwable throwable);
}