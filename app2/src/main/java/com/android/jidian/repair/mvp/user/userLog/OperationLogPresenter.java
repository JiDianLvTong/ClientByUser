package com.android.jidian.repair.mvp.user.userLog;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : PTT
 * date: 2020/8/4 15:42
 * company: 兴达智联
 * description:
 */
public class OperationLogPresenter extends BasePresenter<OperationLogContract.View> implements OperationLogContract.Presenter {
    private OperationLogContract.Model model;

    public OperationLogPresenter() {
        this.model = new OperationLogModel();
    }

    @Override
    public void requestDwLogsList(String month, String uid, String keywd, String lastid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.requestDwLogsList(month, uid, keywd, lastid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestDwLogsListSuccess(bean);
                            } else {
                                if ("我是有底线的~".equals(bean.getMsg())) {
                                    mView.requestDwLogsListSuccess(null);
                                } else {
                                    mView.requestDwLogsListFail(bean.getMsg());
                                }
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestDwLogsListFail(throwable.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void requestBtyHistoryList(String month, String uid, String battery, String lastid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.requestBtyHistoryList(month, uid, battery, lastid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestBtyHistoryListSuccess(bean);
                            } else {
                                if ("我是有底线的~".equals(bean.getMsg())) {
                                    mView.requestBtyHistoryListSuccess(null);
                                } else {
                                    mView.requestDwLogsListFail(bean.getMsg());
                                }
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestDwLogsListFail(throwable.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void requestCabBatteryList(String month, String uid, String cabid, String lastid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.requestCabBatteryList(month, uid, cabid, lastid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestBtyHistoryListSuccess(bean);
                            } else {
                                if ("我是有底线的~".equals(bean.getMsg())) {
                                    mView.requestBtyHistoryListSuccess(null);
                                } else {
                                    mView.requestDwLogsListFail(bean.getMsg());
                                }
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestDwLogsListFail(throwable.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void requestCabEleList(String month, String uid, String cabid, String lastid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        model.requestCabEleList(month, uid, cabid, lastid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestCabEleListSuccess(bean);
                            } else {
                                if ("我是有底线的~".equals(bean.getMsg())) {
                                    mView.requestCabEleListSuccess(null);
                                } else {
                                    mView.requestDwLogsListFail(bean.getMsg());
                                }
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestDwLogsListFail(throwable.getLocalizedMessage());
                    }
                });
    }
}