package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.UserPersonalBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/6 16:02
 * company: 兴达智联
 * description:
 */
public interface PersonalInfoContract {
    interface Model{
        Flowable<UserPersonalBean> requestUserPersonal(String uid);
    }
    interface View extends BaseView{
        void requestUserPersonalSuccess(UserPersonalBean bean);

        void requestUserPersonalFail(String msg);
    }
    interface Presenter{
        void requestUserPersonal(String uid);
    }
}
