package com.android.jidian.repair.mvp.UserMyPatrol.patrolList;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 13:57
 * description:
 */
public class UserMyPatrolModel implements UserMyPatrolContract.Model {
    @Override
    public Flowable<PatrolMyListBean> requestPatrolMylists(String lng, String lat, String page) {
        return RetrofitClient.getInstance().getApeService().requestPatrolMylists(lng, lat, page);
    }
}
