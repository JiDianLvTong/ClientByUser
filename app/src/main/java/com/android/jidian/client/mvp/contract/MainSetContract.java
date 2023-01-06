package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.BaseBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/6 10:38
 * company: 兴达智联
 * description:
 */
public interface MainSetContract {

    interface Model {
        Flowable<BaseBean> requestUserCancel(String uid, String code, String confirm);

        Flowable<BaseBean> requestUserSmsCancel();
    }

    interface View extends BaseView {
        void requestUserCancelSuccess(BaseBean bean);

        void requestUserCancelFail(String msg);

        void requestUserSmsCancel(BaseBean bean);
    }

    interface Presenter {
        void requestUserCancel(String uid, String code, String confirm);

        void requestUserSmsCancel();
    }

}
