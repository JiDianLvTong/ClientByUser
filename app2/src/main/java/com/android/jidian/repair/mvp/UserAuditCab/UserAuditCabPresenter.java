package com.android.jidian.repair.mvp.UserAuditCab;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/3 16:29
 * description:
 */
public class UserAuditCabPresenter extends BasePresenter<UserAuditCabContract.View> implements UserAuditCabContract.Presenter {
    private UserAuditCabContract.Model model;

    public UserAuditCabPresenter() {
        this.model = new UserAuditCabModel();
    }

    @Override
    public void requestAuditCabList(String page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.requestAuditCabList(page)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestAuditCabListSuccess(bean);
                            } else {
                                mView.requestFail(bean.getMsg());
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
