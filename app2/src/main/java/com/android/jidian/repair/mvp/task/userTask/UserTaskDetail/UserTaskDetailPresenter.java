package com.android.jidian.repair.mvp.task.userTask.UserTaskDetail;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.mvp.task.TimeTaskDetailContract;
import com.android.jidian.repair.mvp.task.TimeTaskDetailModel;
import com.android.jidian.repair.net.RxScheduler;

import java.io.File;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public class UserTaskDetailPresenter extends BasePresenter<UserTaskDetailContract.View> implements UserTaskDetailContract.Presenter {

    private UserTaskDetailContract.Model mModel;

    public UserTaskDetailPresenter() {
        mModel = new UserTaskDetailModel();
    }

    @Override
    public void requestWorktaskDetail(String wtid) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestWorktaskDetail(wtid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                if (bean.getData() != null) {
                                    mView.requestWorktaskDetailSuccess(bean.getData());
                                }
                            } else {
                                mView.requestWorktaskDetailFail(bean.getMsg());
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

    @Override
    public void requestWorktaskResolve(String wtid, String ustat, String content, String img1, String img2, String img3, String img4) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestWorktaskResolve(wtid, ustat, content, img1, img2, img3, img4)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestWorktaskResolveSuccess(bean);
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
