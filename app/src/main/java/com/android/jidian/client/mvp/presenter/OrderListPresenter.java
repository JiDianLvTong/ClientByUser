package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.OrderListContract;
import com.android.jidian.client.mvp.model.OrderListModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2023/1/6 14:02
 * company: 兴达智联
 * description:
 */
public class OrderListPresenter extends BasePresenter<OrderListContract.View> implements OrderListContract.Presenter {

    private OrderListContract.Model model;

    public OrderListPresenter() {
        this.model = new OrderListModel();
    }

    @Override
    public void requestUserUOrder(String uid, String lastid) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.requestUserUOrder(uid, lastid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if ("1".equals(bean.getStatus())) {
                            mView.requestUserUOrderSuccess(bean);
                        }else {
                            mView.requestUserUOrderFail(bean.getMsg());
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
