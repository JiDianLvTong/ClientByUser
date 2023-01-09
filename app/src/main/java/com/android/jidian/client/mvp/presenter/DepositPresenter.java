package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.DepositContract;
import com.android.jidian.client.mvp.model.DepositModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2023/1/9 14:10
 * company: 兴达智联
 * description:
 */
public class DepositPresenter extends BasePresenter<DepositContract.View> implements DepositContract.Presenter {

    private DepositContract.Model model;

    public DepositPresenter() {
        this.model = new DepositModel();
    }

    @Override
    public void requestDepositRefundOrder(String uid) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.requestDepositRefundOrder(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if ("1".equals(bean.getStatus())) {
                            mView.requestDepositRefundOrderSuccess(bean);
                        } else {
                            mView.requestDepositRefundOrderFail(bean.getMsg());
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
