package com.android.jidian.repair.mvp.UserMyPatrol.patrolList;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 13:55
 * description:
 */
public interface UserMyPatrolContract {
    interface Model {
        Flowable<PatrolMyListBean> requestPatrolMylists(String lng, String lat, String page);

    }

    interface View extends BaseView {
        void requestPatrolMylistsSuccess(PatrolMyListBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestPatrolMylists(String lng, String lat, String page);
    }
}
