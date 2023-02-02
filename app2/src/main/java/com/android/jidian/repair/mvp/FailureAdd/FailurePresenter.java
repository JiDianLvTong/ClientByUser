package com.android.jidian.repair.mvp.FailureAdd;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

import java.io.File;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:15
 * description:
 */
public class FailurePresenter extends BasePresenter<FailureContract.View> implements FailureContract.Presenter {

    private FailureContract.Model mModel;

    public FailurePresenter() {
        this.mModel = new FailureModel();
    }

    @Override
    public void requestWorktaskAddFault(String cabid, String content, String img1, String img2, String img3, String img4) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestWorktaskAddFault(cabid, content, img1, img2, img3, img4)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestWorktaskAddFaultSuccess(bean);
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

    @Override
    public void requestUploadUploadUrlSet(String token) {
        if (!isViewAttached()) {
            return;
        }
//        if (mView != null) {
//            mView.showProgress();
//        }
        mModel.requestUploadUploadUrlSet(token)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
//                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                if (bean.getData() != null) {
                                    mView.requestUploadUploadUrlSetSuccess(bean.getData());
                                }
                            } else {
                                mView.requestShowTips(bean.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
//                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }

    @Override
    public void requestUpLoadImg(String url, String filePath, String upToken, String companyid, int requestCode) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestUpLoadImg(url, new File(filePath), upToken, companyid)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestUpLoadImgSuccess(bean, requestCode);
                            } else {
                                mView.requestShowTips(bean.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.requestShowTips(throwable.getLocalizedMessage());
                    }
                });
    }
}
