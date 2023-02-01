package com.android.jidian.repair.mvp.main.PatrolFragment;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/1 10:47
 * description:
 */
public class PatrolModel implements PatrolContract.Model {
    @Override
    public Flowable<PatrolIndexBean> requestPatrolIndex(String lng, String lat, String page) {
        return RetrofitClient.getInstance().getApeService().requestPatrolIndex(lng, lat, page);
    }
}
