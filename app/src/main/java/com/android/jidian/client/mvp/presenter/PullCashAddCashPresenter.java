package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.PullCashAddCashContract;
import com.android.jidian.client.mvp.model.PullCashAddCashModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2021/11/26 上午10:17
 * company: 兴达智联
 * description:
 */
public class PullCashAddCashPresenter extends BasePresenter<PullCashAddCashContract.View> implements PullCashAddCashContract.Presenter {

    private PullCashAddCashContract.Model model;

    public PullCashAddCashPresenter() {
        this.model = new PullCashAddCashModel();
    }

    @Override
    public void requestPullCashAddCash(String cash_amount) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestPullCashAddCash(cash_amount)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if (1 == bean.status) {
                                mView.requestPullCashAddCashSuccess(bean.msg);
                            } else {
                                mView.requestPullCashAddCashFail(bean.msg);
                            }
                        } else {
                            mView.requestShowTips("网络异常");
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestPullCashGetUserAccountInfo() {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestPullCashGetUserAccountInfo()
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPullCashGetUserAccountInfoSuccess(bean.getData());
                            } else {
                                mView.requestShowTips(bean.getMsg());
                            }
                        } else {
                            mView.requestShowTips("网络异常");
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestPullCashSetUserAccountInfo(String access_code) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestPullCashSetUserAccountInfo(access_code)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if (1 == bean.status) {
                                mView.requestPullCashSetUserAccountInfoSuccess(bean.msg);
                            } else {
                                mView.requestShowTips(bean.msg);
                            }
                        } else {
                            mView.requestShowTips("网络异常");
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
