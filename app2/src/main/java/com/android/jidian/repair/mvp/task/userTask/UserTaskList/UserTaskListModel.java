package com.android.jidian.repair.mvp.task.userTask.UserTaskList;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/3 15:37
 * description:
 */
public class UserTaskListModel implements UserTaskListContract.Model {
    @Override
    public Flowable<WorktaskMylistsBean> requestWorktaskMylists(String page) {
        return RetrofitClient.getInstance().getApeService().requestWorktaskMylists(page);
    }
}
