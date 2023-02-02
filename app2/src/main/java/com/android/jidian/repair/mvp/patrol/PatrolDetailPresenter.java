package com.android.jidian.repair.mvp.patrol;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

import java.io.File;

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

    /**
     * 巡检提交
     * @param cabid 电柜编号
     * @param img1 打卡合影
     * @param img2 屏幕清洁
     * @param img3 后门锁状态，
     * @param img4_1 柜体前侧清洁
     * @param img4_2 柜体左则清洁
     * @param img4_3 柜体右则清洁
     * @param img4_4 柜体顶部清洁
     * @param img5_1 柜体内上部清洁
     * @param img5_2 柜体内中部清洁
     * @param img5_3 柜体内下部清洁
     * @param img6_1 是否漏电检测笔图
     * @param isnetdbm 网络信号：1=有，0=无
     * @param isdixian 电柜地线：1=有，0=无
     * @param isopenbtn 换点按钮是否可用：1=是，0=否
     */
    @Override
    public void requestPatrolAddpatrol(String cabid, String img1, String img2, String img3, String img4_1, String img4_2, String img4_3, String img4_4, String img5_1, String img5_2, String img5_3, String img6_1, String isnetdbm, String isdixian, String isopenbtn) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestPatrolAddpatrol(cabid, img1, img2, img3, img4_1, img4_2, img4_3, img4_4, img5_1, img5_2, img5_3, img6_1, isnetdbm, isdixian, isopenbtn)
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
