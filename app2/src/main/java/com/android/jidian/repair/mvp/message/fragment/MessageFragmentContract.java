package com.android.jidian.repair.mvp.message.fragment;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:23
 * description:
 */
public class MessageFragmentContract {

    interface Model {
        Flowable<AdvicesListsBean> requestAdvicesLists(String type, String page);

        Flowable<BaseBean> requestAdvicesRead(String msg_id);
    }

    interface View extends BaseView {
        void requestAdvicesListsSuccess(AdvicesListsBean bean);

        void requestShowTips(String msg);

        void requestAdvicesReadSuccess(BaseBean bean);
    }

    interface Presenter {
        void requestAdvicesLists(String type, String page);

        void requestAdvicesRead(String msg_id);
    }
}
