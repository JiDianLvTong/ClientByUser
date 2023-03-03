package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.QrCodeScanBean;
import com.android.jidian.client.mvp.contract.ScanCodeContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class ScanCodeModel implements ScanCodeContract.Model {
    @Override
    public Flowable<QrCodeScanBean> requestQrCodeScan(String uid, String code, String entercode, String type, String sync) {
        return RetrofitClient.getInstance().getApiService().requestQrCodeScan(uid, code, entercode, type, "1");
    }

    @Override
    public Flowable<QrCodeScanBean> requestEndBindBike(String uid) {
        return RetrofitClient.getInstance().getApiService().requestEndBindBike(uid);
    }

    @Override
    public Flowable<BaseBean> requestCouponScan(String uid, String code) {
        return RetrofitClient.getInstance().getApiService().sendScan(uid, code);
    }

    @Override
    public Flowable<BaseBean> requestUserScanBind(String qrstr) {
        return RetrofitClient.getInstance().getApiService().requestUserScanBind(qrstr);
    }
}