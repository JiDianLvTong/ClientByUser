package com.android.jidian.client.mvp.presenter;

import com.android.jidian.client.base.BasePresenter;
import com.android.jidian.client.mvp.contract.InviteDetailContract;
import com.android.jidian.client.mvp.model.InviteDetailModel;
import com.android.jidian.client.net.RxScheduler;

import io.reactivex.disposables.Disposable;

/**
 * @author : xiaoming
 * date: 2021/11/27 上午9:53
 * company: 兴达智联
 * description:
 */
public class InviteDetailPresenter extends BasePresenter<InviteDetailContract.View> implements InviteDetailContract.Presenter {

    private InviteDetailContract.Model model;

    public InviteDetailPresenter() {
        this.model = new InviteDetailModel();
    }

    @Override
    public void requestPullActivityInviteLists(String aid, String type, String page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        Disposable disposable = model.requestPullActivityInviteLists(aid, type, page)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPullActivityInviteListsSuccess(bean);
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
