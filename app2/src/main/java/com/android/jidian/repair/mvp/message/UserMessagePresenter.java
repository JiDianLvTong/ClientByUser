package com.android.jidian.repair.mvp.message;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.mvp.UserAuditCab.UserAuditCabContract;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:15
 * description:
 */
public class UserMessagePresenter extends BasePresenter<UserMessageContract.View> implements UserMessageContract.Presenter {

    private UserMessageContract.Model mModel;

    public UserMessagePresenter() {
        this.mModel = new UserMessageModel();
    }

    @Override
    public void requestAdvicesAllRead(String type) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestAdvicesAllRead(type)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestAdvicesAllReadSuccess(bean);
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
