package com.android.jidian.repair.mvp.task.userTask.UserTaskList;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 15:35
 * description:
 */
public interface UserTaskListContract {
    interface Model {
        Flowable<WorktaskMylistsBean> requestWorktaskMylists(String page);
    }

    interface View extends BaseView {
        void requestWorktaskMylistsSuccess(WorktaskMylistsBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestWorktaskMylists(String page);
    }
}
