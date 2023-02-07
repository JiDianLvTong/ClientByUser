package com.android.jidian.repair.mvp.cabinet.cabAddCab;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

import java.io.File;

/**
 * @author : xiaoming
 * date: 2023/2/3 17:59
 * description:
 */
public class AddCabPresenter extends BasePresenter<AddCabContract.View> implements AddCabContract.Presenter {

    private AddCabContract.Model mModel;

    public AddCabPresenter() {
        mModel = new AddCabModel();
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
