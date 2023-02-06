package com.android.jidian.repair.mvp.UserTask.UserTaskDetail;

import com.android.jidian.repair.base.BaseView;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public interface UserTaskDetailContract {
    interface Model {
        Flowable<WorktaskDetailBean> requestWorktaskDetail(String wtid);
    }

    interface View extends BaseView {
        void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestWorktaskDetail(String wtid);
    }
}
