package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.PullCashGetUserProfitContract;
import com.android.jidian.client.mvp.model.PullCashGetUserProfitModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2021/11/25 下午5:56
 * company: 兴达智联
 * description:
 */
public class PullCashGetUserProfitPresenter extends BasePresenter<PullCashGetUserProfitContract.View> implements PullCashGetUserProfitContract.Presenter {

    private PullCashGetUserProfitContract.Model model;

    public PullCashGetUserProfitPresenter() {
        this.model = new PullCashGetUserProfitModel();
    }

    @Override
    public void requestPullCashGetUserProfit() {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestPullCashGetUserProfit()
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPullCashGetUserProfitSuccess(bean.getData());
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
}
