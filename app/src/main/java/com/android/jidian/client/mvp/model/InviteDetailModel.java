package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.PullActivityInviteListsBean;
import com.android.jidian.client.mvp.contract.InviteDetailContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/27 上午9:52
 * company: 兴达智联
 * description:
 */
public class InviteDetailModel implements InviteDetailContract.Model {
    @Override
    public Flowable<PullActivityInviteListsBean> requestPullActivityInviteLists(String aid, String type, String page) {
        return RetrofitClient.getInstance().getApiService().requestPullActivityInviteLists(aid, type, page);
    }
}
