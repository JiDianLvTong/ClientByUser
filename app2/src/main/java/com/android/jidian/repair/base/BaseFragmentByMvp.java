package com.android.jidian.repair.base;

public abstract class BaseFragmentByMvp<T extends BasePresenter> extends BaseFragment {

    public T mPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}