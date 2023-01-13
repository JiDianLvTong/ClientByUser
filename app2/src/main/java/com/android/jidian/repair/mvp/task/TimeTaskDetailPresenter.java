package com.android.jidian.repair.mvp.task;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public class TimeTaskDetailPresenter extends BasePresenter<TimeTaskDetailContract.View> implements TimeTaskDetailContract.Presenter {

    private TimeTaskDetailModel mModel;

    public TimeTaskDetailPresenter() {
        mModel = new TimeTaskDetailModel();
    }

    @Override
    public void requestWorktaskDetail(String wtid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestWorktaskDetail(wtid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                if (bean.getData() != null) {
                                    mView.requestWorktaskDetailSuccess(bean.getData());
                                }
                            } else {
                                mView.requestWorktaskDetailFail(bean.getMsg());
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
