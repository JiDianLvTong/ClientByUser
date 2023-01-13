package com.android.jidian.repair.mvp.main.TimeLimitTaskFragment;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/1/12 10:54
 * description:
 */
public class TimeLimitTaskPresenter extends BasePresenter<TimeLimitTaskContract.View> implements TimeLimitTaskContract.Presenter {

    private TimeLimitTaskContract.Model mModel;

    public TimeLimitTaskPresenter() {
        mModel = new TimeLimitTaskModel();
    }

    @Override
    public void requestWorktaskLists(String page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestWorktaskLists(page)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestWorktaskListsSuccess(bean.getData());
                            } else {
                                mView.requestWorktaskListsFail(bean.getMsg());
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
