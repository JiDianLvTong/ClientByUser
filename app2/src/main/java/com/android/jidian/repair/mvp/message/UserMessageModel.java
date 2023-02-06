package com.android.jidian.repair.mvp.message;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:15
 * description:
 */
public class UserMessageModel implements UserMessageContract.Model {
    @Override
    public Flowable<BaseBean> requestAdvicesAllRead(String type) {
        return RetrofitClient.getInstance().getApeService().requestAdvicesAllRead(type);
    }
}
