package com.android.jidian.repair.mvp.user.userMyPatrol.patrolDetail;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/3 14:59
 * description:
 */
public class MyPartolDetailPresenter extends BasePresenter<MyPartolDetailContract.View> implements MyPartolDetailContract.Presenter {

    private MyPartolDetailContract.Model mModel;

    public MyPartolDetailPresenter() {
        mModel = new MyPartolDetailModel();
    }

    @Override
    public void requestPatrolDetail(String patid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestPatrolDetail(patid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPatrolDetailSuccess(bean);
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
