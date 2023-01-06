package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.UserUOrderBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/6 14:00
 * company: 兴达智联
 * description:
 */
public interface OrderListContract {
    interface Model {
        Flowable<UserUOrderBean> requestUserUOrder(String uid, String lastid);
    }

    interface View extends BaseView {
        void requestUserUOrderSuccess(UserUOrderBean bean);

        void requestUserUOrderFail(String msg);
    }

    interface Presenter {
        void requestUserUOrder(String uid, String lastid);
    }

}
