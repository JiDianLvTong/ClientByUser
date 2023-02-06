package com.android.jidian.repair.mvp.UserMyPatrol.patrolDetail;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 14:50
 * description:
 */
public interface MyPartolDetailContract {
    interface Model {
        Flowable<PatrolDetailBean> requestPatrolDetail(String patid);
    }

    interface View extends BaseView {
        void requestPatrolDetailSuccess(PatrolDetailBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestPatrolDetail(String patid);
    }
}
