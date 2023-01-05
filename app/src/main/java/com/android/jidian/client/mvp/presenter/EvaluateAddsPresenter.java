package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.EvaluateAddsContract;
import com.android.jidian.client.mvp.model.EvaluateAddsModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;

import io.reactivex.disposables.Disposable;

public class EvaluateAddsPresenter extends BasePresenter<EvaluateAddsContract.View> implements EvaluateAddsContract.Presenter {
    EvaluateAddsModel model;

    public EvaluateAddsPresenter() {
        this.model = new EvaluateAddsModel();
    }

    @Override
    public void evaluateAdds(int cabid, int stars, String labids, String content, String repair) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.evaluateAdds(cabid, stars, labids, content, repair)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    mView.onSuccess1(bean);
                    mView.hideProgress();
                }, throwable -> {
                    mView.onError(throwable);
                    mView.hideProgress();
                });
    }

    @Override
    public void evaluateLabs(int cabid, String repair) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.evaluateLabs(cabid, repair)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    mView.hideProgress();
                    mView.onSuccess2(bean);
                }, throwable -> {
                    mView.hideProgress();
                    mView.onError(throwable);
                });
    }
}
