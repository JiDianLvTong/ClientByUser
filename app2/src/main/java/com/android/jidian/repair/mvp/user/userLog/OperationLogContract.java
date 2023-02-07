package com.android.jidian.repair.mvp.user.userLog;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : PTT
 * date: 2020/8/4 15:43
 * company: 兴达智联
 * description:
 */
public interface OperationLogContract {
    interface Model {
        Flowable<OperationLogBean> requestDwLogsList(String month, String uid, String keywd, String lastid);

        Flowable<BatteryInquiryBean> requestBtyHistoryList(String month, String uid, String battery, String lastid);

        Flowable<BatteryInquiryBean> requestCabBatteryList(String month, String uid, String cabid, String lastid);

        Flowable<MeterReadingBean> requestCabEleList(String month, String uid, String cabid, String lastid);
    }

    interface View extends BaseView {
        void requestDwLogsListSuccess(OperationLogBean bean);

        void requestBtyHistoryListSuccess(BatteryInquiryBean bean);

        void requestCabEleListSuccess(MeterReadingBean bean);

        void requestDwLogsListFail(String msg);
    }

    interface Presenter {
        void requestDwLogsList(String month, String uid, String keywd, String lastid);

        void requestBtyHistoryList(String month, String uid, String battery, String lastid);

        void requestCabBatteryList(String month, String uid, String cabid, String lastid);

        void requestCabEleList(String month, String uid, String cabid, String lastid);
    }
}