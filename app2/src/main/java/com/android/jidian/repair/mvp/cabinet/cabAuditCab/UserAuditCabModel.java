package com.android.jidian.repair.mvp.cabinet.cabAuditCab;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 16:28
 * description:
 */
public class UserAuditCabModel implements UserAuditCabContract.Model{
    @Override
    public Flowable<AuditCabListBean> requestAuditCabList(String page) {
        return RetrofitClient.getInstance().getApeService().requestAuditCabList(page);
    }
}
