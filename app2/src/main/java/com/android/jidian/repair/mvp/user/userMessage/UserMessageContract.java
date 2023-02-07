package com.android.jidian.repair.mvp.user.userMessage;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:13
 * description:
 */
public interface UserMessageContract {
    interface Model {
        Flowable<BaseBean> requestAdvicesAllRead(String type);
    }

    interface View extends BaseView {
        void requestAdvicesAllReadSuccess(BaseBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestAdvicesAllRead(String type);
    }
}
