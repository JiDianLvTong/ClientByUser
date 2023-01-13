package com.android.jidian.repair.mvp.main.TimeLimitTaskFragment;

import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:14
 * description:
 */
public interface TimeLimitTaskContract {
    interface Model{
        Flowable<WorktaskListsBean> requestWorktaskLists(String page);
    }
    interface View extends BaseView{
        void requestWorktaskListsSuccess(WorktaskListsBean.DataBean bean);

        void requestWorktaskListsFail(String msg);
    }
    interface Presenter{
        void requestWorktaskLists(String page);
    }
}
