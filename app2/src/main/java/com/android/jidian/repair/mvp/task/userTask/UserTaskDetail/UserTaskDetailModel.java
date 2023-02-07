package com.android.jidian.repair.mvp.task.userTask.UserTaskDetail;

import com.android.jidian.repair.mvp.task.WorktaskDetailBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public class UserTaskDetailModel implements UserTaskDetailContract.Model {
    @Override
    public Flowable<WorktaskDetailBean> requestWorktaskDetail(String wtid) {
        return RetrofitClient.getInstance().getApeService().requestWorktaskDetail(wtid);
    }
}
