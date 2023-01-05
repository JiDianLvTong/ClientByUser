package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.CouponCashv2Bean;
import com.android.jidian.client.bean.CouponScanBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;

public interface CouponCashv2Contract {
    interface Model {
        Flowable<CouponCashv2Bean> couponCashv2(int uid, Integer cid, String code, String type);

        Flowable<CouponScanBean> couponScan(int uid, String code);
    }

    interface View extends BaseView {
        void onCouponCashv2Success(CouponCashv2Bean couponCashv2Bean);

        void onCouponScanSuccess(CouponScanBean couponScanBean);

        void onCouponScanError(Throwable throwable);
    }

    interface Presenter {
        void couponCashv2(int uid, Integer cid, String code, String type);

        void couponScan(int uid, String code);
    }
}
