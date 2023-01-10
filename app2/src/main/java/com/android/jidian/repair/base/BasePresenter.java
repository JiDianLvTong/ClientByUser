package com.android.jidian.repair.base;

public class BasePresenter<V> {
    public V mView;

    /**
     * 绑定view
     *
     * @param mView view
     */
    public void attachView(V mView) {
        this.mView = mView;
    }

    /**
     * 解除绑定view
     */
    public void detachView() {
        this.mView = null;
    }

    /**
     * view是否已绑定
     *
     * @return boolean
     */
    public boolean isViewAttached() {
        return mView != null;
    }
}