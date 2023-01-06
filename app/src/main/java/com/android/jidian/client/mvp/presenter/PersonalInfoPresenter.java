package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.PersonalInfoContract;
import com.android.jidian.client.mvp.model.PersionalInfoModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2023/1/6 16:05
 * company: 兴达智联
 * description:
 */
public class PersonalInfoPresenter extends BasePresenter<PersonalInfoContract.View> implements PersonalInfoContract.Presenter {

    private PersonalInfoContract.Model model;

    public PersonalInfoPresenter() {
        this.model = new PersionalInfoModel();
    }

    @Override
    public void requestUserPersonal(String uid) {
        if (!isViewAttached()) {
            return;
        }
        mView.showProgress();
        Disposable disposable = model.requestUserPersonal(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if ("1".equals(bean.getStatus())) {
                            mView.requestUserPersonalSuccess(bean);
                        }else {
                            mView.requestUserPersonalFail(bean.getMsg());
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
