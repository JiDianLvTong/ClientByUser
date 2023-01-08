package com.android.jidian.client.base;

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