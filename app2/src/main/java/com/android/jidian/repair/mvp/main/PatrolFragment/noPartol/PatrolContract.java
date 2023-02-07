package com.android.jidian.repair.mvp.main.PatrolFragment.noPartol;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/1 10:28
 * description:
 */
public interface PatrolContract {
    interface Model {
        Flowable<PatrolIndexBean> requestPatrolIndex(String lng, String lat, String page);
    }

    interface View extends BaseView {
        void requestPatrolIndexSuccess(PatrolIndexBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestPatrolIndex(String lng, String lat, String page);
    }
}
