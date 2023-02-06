package com.android.jidian.repair.mvp.UserLog;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : PTT
 * date: 2020/8/4 15:44
 * company: 兴达智联
 * description:
 */
public class OperationLogModel implements OperationLogContract.Model {
    @Override
    public Flowable<OperationLogBean> requestDwLogsList(String month, String uid, String keywd, String lastid) {
        return RetrofitClient.getInstance().getApeService().requestDwLogsList(month, uid, keywd, lastid);
    }

    @Override
    public Flowable<BatteryInquiryBean> requestBtyHistoryList(String month, String uid, String battery, String lastid) {
        return RetrofitClient.getInstance().getApeService().requestBtyHistoryList(month, uid, battery, lastid);
    }

    @Override
    public Flowable<BatteryInquiryBean> requestCabBatteryList(String month, String uid, String cabid, String lastid) {
        return RetrofitClient.getInstance().getApeService().requestCabBatteryList(month, uid, cabid, lastid);
    }

    @Override
    public Flowable<MeterReadingBean> requestCabEleList(String month, String uid, String cabid, String lastid) {
        return RetrofitClient.getInstance().getApeService().requestCabEleList(month, uid, cabid, lastid);
    }
}