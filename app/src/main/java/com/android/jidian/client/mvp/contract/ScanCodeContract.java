package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.QrCodeScanBean;

import io.reactivex.Flowable;

public interface ScanCodeContract {
    interface Model {
        Flowable<QrCodeScanBean> requestQrCodeScan(String uid, String code, String entercode, String type, String sync);

        Flowable<QrCodeScanBean> requestEndBindBike(String uid);

        Flowable<BaseBean> requestCouponScan(String uid, String code);
    }

    interface View extends BaseView {
        void requestQrCodeScanSuccess(QrCodeScanBean data);

        void requestQrCodeScanError(String msg);

        void requestEndBindBikeSuccess(QrCodeScanBean qrCodeScanBean);

        void requestCouponScanSuccess(BaseBean bean);
    }

    interface Presenter {
        void requestQrCodeScan(String uid, String code, String entercode, String type, String sync);

        void requestEndBindBike(String uid);

        void requestCouponScan(String uid, String code);
    }
}
