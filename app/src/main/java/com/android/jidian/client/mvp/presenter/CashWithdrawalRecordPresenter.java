package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.CashWithdrawalRecordContract;
import com.android.jidian.client.mvp.model.CashWithdrawalRecordModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2021/11/26 下午3:41
 * company: 兴达智联
 * description:
 */
public class CashWithdrawalRecordPresenter extends BasePresenter<CashWithdrawalRecordContract.View> implements CashWithdrawalRecordContract.Presenter {

    private CashWithdrawalRecordContract.Model model;

    public CashWithdrawalRecordPresenter() {
        this.model = new CashWithdrawalRecordModel();
    }

    @Override
    public void requestPullCashGetCashRecord() {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestPullCashGetCashRecord()
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPullCashGetCashRecordSuccess(bean);
                            } else {
                                mView.requestShowTis(bean.getMsg());
                            }
                        } else {
                            mView.requestShowTis("网络异常");
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
