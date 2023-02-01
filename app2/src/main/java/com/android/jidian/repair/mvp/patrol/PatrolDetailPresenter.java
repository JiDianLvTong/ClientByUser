package com.android.jidian.repair.mvp.patrol;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/1 17:05
 * description:
 */
public class PatrolDetailPresenter extends BasePresenter<PatrolDetailContract.View> implements PatrolDetailContract.Presenter {

    private PatrolDetailContract.Model mModel;

    public PatrolDetailPresenter() {
        this.mModel = new PatrolDetailModel();
    }

    @Override
    public void requestPatrolAddpatrol(String cabid, String img1, String img2, String img3, String img4_1, String img4_2, String img4_3, String img4_4) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestPatrolAddpatrol(cabid, img1, img2, img3, img4_1, img4_2, img4_3, img4_4)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestPatrolAddpatrolSuccess(bean);
                            } else {
                                mView.requestShowTips(bean.getMsg());
                            }
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
