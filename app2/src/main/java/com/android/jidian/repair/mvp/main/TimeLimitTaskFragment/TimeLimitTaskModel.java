package com.android.jidian.repair.mvp.main.TimeLimitTaskFragment;

import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/12 10:54
 * description:
 */
public class TimeLimitTaskModel implements TimeLimitTaskContract.Model {
    @Override
    public Flowable<WorktaskListsBean> requestWorktaskLists(String page) {
        return RetrofitClient.getInstance().getApeService().requestWorktaskLists(page);
    }
}
