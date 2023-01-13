package com.android.jidian.repair.mvp.task;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public interface TimeTaskDetailContract {
    interface Model {
        Flowable<WorktaskDetailBean> requestWorktaskDetail(String wtid);
    }

    interface View extends BaseView {
        void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean);

        void requestWorktaskDetailFail(String msg);
    }

    interface Presenter {
        void requestWorktaskDetail(String wtid);
    }
}
