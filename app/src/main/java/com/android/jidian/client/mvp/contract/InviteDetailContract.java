package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.PullActivityInviteListsBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/27 上午9:50
 * company: 兴达智联
 * description:
 */
public interface InviteDetailContract {
    interface Model {
        Flowable<PullActivityInviteListsBean> requestPullActivityInviteLists(String aid, String type, String page);
    }

    interface View extends BaseView {
        void requestPullActivityInviteListsSuccess(PullActivityInviteListsBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestPullActivityInviteLists(String aid, String type, String page);
    }
}
