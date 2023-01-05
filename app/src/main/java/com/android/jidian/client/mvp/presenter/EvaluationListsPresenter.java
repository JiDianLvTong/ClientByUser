package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.mvp.contract.EvaluationListsContract;
import com.android.jidian.client.mvp.model.EvaluationListsModel;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.base.BasePresenter;

import io.reactivex.disposables.Disposable;

public class EvaluationListsPresenter extends BasePresenter<EvaluationListsContract.View> implements EvaluationListsContract.Presenter {
    private EvaluationListsModel model;

    public EvaluationListsPresenter() {
        this.model = new EvaluationListsModel();
    }

    @Override
    public void evaluateLists(int cabid, int page, String repair) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.evaluateLists(cabid, page, repair)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    mView.hideProgress();
                    mView.onSuccess(bean);
                }, throwable -> {
                    mView.hideProgress();
                    mView.onError(throwable);
                });
    }
}
