package com.android.jidian.repair.mvp.main.FailureFragment;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:14
 * description:
 */
public class FailureModel implements FailureContract.Model {
    @Override
    public Flowable<BaseBean> requestWorktaskAddFault(String cabid, String content, String img1, String img2, String img3, String img4) {
        return RetrofitClient.getInstance().getApeService().requestWorktaskAddFault(cabid, content, img1, img2, img3, img4);
    }
}
