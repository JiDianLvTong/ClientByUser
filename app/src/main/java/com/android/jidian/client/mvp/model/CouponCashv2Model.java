package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.CouponCashv2Bean;
import com.android.jidian.client.bean.CouponScanBean;
import com.android.jidian.client.mvp.contract.CouponCashv2Contract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class CouponCashv2Model implements CouponCashv2Contract.Model {
    @Override
    public Flowable<CouponCashv2Bean> couponCashv2(int uid, Integer cid, String code, String type) {
        return RetrofitClient.getInstance().getApiService().couponCashv2(uid, cid, code, type);
    }

    @Override
    public Flowable<CouponScanBean> couponScan(int uid, String code) {
        return RetrofitClient.getInstance().getApiService().couponScan(uid, code);
    }
}
