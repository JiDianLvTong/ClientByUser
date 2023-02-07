package com.android.jidian.repair.mvp.main.PatrolFragment.noPartol;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/1 10:48
 * description:
 */
public class PatrolPresenter extends BasePresenter<PatrolContract.View> implements PatrolContract.Presenter {

    private PatrolContract.Model mModel;

    public PatrolPresenter() {
        mModel = new PatrolModel();
    }

    @Override
    public void requestPatrolIndex(String lng, String lat, String page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestPatrolIndex(lng, lat, page)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPatrolIndexSuccess(bean);
                            } else {
                                mView.requestShowTips(bean.getMsg());
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
