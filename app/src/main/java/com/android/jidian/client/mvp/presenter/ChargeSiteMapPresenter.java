package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.ChargeSiteMapContract;
import com.android.jidian.client.mvp.model.ChargeSiteMapModel;
import com.android.jidian.client.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2022/12/30 17:50
 * company: 兴达智联
 * description:
 */
public class ChargeSiteMapPresenter extends BasePresenter<ChargeSiteMapContract.View> implements ChargeSiteMapContract.Presenter{

    private ChargeSiteMapContract.Model model;

    public ChargeSiteMapPresenter() {
        this.model = new ChargeSiteMapModel();
    }

    @Override
    public void requestChargeSite(String uid, String lng, String lat, String type) {
        if (!isViewAttached()) {
            return;
        }
        model.requestChargeSite(uid , lng , lat , type)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        if (bean.getStatus() == 1) {
                            mView.requestChargeSiteSuccess(bean);
                        }else {
                            mView.requestChargeSiteError(bean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.requestChargeSiteError("error");
                    }
                });
    }
}
