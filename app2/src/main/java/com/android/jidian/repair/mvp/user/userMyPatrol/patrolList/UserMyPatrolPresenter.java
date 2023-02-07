package com.android.jidian.repair.mvp.user.userMyPatrol.patrolList;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/3 13:57
 * description:
 */
public class UserMyPatrolPresenter extends BasePresenter<UserMyPatrolContract.View> implements UserMyPatrolContract.Presenter {

    private UserMyPatrolContract.Model mModel;

    public UserMyPatrolPresenter() {
        mModel = new UserMyPatrolModel();
    }

    @Override
    public void requestPatrolMylists(String lng, String lat, String page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestPatrolMylists(lng, lat, page)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPatrolMylistsSuccess(bean);
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
