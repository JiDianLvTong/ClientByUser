package com.android.jidian.repair.mvp.UserMyPatrol.patrolDetail;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 14:58
 * description:
 */
public class MyPartolDetailModel implements MyPartolDetailContract.Model {
    @Override
    public Flowable<PatrolDetailBean> requestPatrolDetail(String patid) {
        return RetrofitClient.getInstance().getApeService().requestPatrolDetail(patid);
    }
}
