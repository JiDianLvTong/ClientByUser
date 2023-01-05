package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/30 15:55
 * company: 兴达智联
 * description:
 */
public interface MainUserContract {
    interface Model{
        Flowable<MainActiyivyExpenseBean> requestMainInfo(String uid);
    }
    interface View extends BaseView{
        void requestMainInfoSuccess(MainActiyivyExpenseBean bean);

        void requestMainInfoFail(String msg);
    }
    interface Presenter{
        void requestMainInfo(String uid);
    }
}
