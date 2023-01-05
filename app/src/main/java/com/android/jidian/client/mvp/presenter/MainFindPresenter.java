package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.MainFindContract;
import com.android.jidian.client.mvp.model.MainFindModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2022/12/30 17:50
 * company: 兴达智联
 * description:
 */
public class MainFindPresenter extends BasePresenter<MainFindContract.View> implements MainFindContract.Presenter{

    private MainFindContract.Model model;

    public MainFindPresenter() {
        this.model = new MainFindModel();
    }

    @Override
    public void requestFindIndex(String lng, String lat) {
        if (!isViewAttached()) {
            return;
        }
        Disposable disposable = model.requestFindIndex(lng, lat)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        if ("1".equals(bean.getStatus())) {
                            mView.requestFindIndexSuccess(bean);
                        }else {
                            mView.requestFindIndexFail(bean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.onError(throwable);
                    }
                });
    }
}
