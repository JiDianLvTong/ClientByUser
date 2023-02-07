package com.android.jidian.repair.mvp.main.UserFragment;

import com.android.jidian.repair.base.BaseView;
import com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolIndexBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/13 11:53
 * description:
 */
public interface UserContract {
    interface Model {
        Flowable<PatrolIndexBean> requestPatrolIndex(String lng, String lat, String page);
    }

    interface View extends BaseView {
        void requestPatrolIndexSuccess(PatrolIndexBean.DataBean bean);

        void requestPatrolIndexFail(String msg);
    }

    interface Presenter {
        void requestPatrolIndex(String lnh, String lat, String page);
    }
}
