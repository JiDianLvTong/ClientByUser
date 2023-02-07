package com.android.jidian.repair.mvp.cabinet.cabAuditCab;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 16:27
 * description:
 */
public interface UserAuditCabContract {
    interface Model {
        Flowable<AuditCabListBean> requestAuditCabList(String page);
    }

    interface View extends BaseView {
        void requestAuditCabListSuccess(AuditCabListBean bean);

        void requestFail(String msg);
    }

    interface Presenter {
        void requestAuditCabList(String page);
    }
}
