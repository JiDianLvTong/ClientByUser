package com.android.jidian.repair.mvp.message.fragment;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:27
 * description:
 */
public class MessageFragmentModel implements MessageFragmentContract.Model {
    @Override
    public Flowable<AdvicesListsBean> requestAdvicesLists(String type, String page) {
        return RetrofitClient.getInstance().getApeService().requestAdvicesLists(type, page);
    }

    @Override
    public Flowable<BaseBean> requestAdvicesRead(String msg_id) {
        return RetrofitClient.getInstance().getApeService().requestAdvicesRead(msg_id);
    }
}
