package com.android.jidian.repair.mvp.main;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:37
 * description:
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private MainContract.Model mModel;

    public MainPresenter() {
        mModel = new MainModel();
    }

    @Override
    public void requestLoginCheckAcc(String uid, String apptoken, String nowlng, String nowlat) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestLoginCheckAcc(uid, apptoken, nowlng, nowlat)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("0".equals(bean.getStatus())) {
                                mView.requestLoginCheckAccSuccess();
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }
}
