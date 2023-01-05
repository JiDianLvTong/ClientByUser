package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.FindIndexBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2022/12/30 17:48
 * company: 兴达智联
 * description:
 */
public interface MainFindContract {
    interface Model {
        Flowable<FindIndexBean> requestFindIndex(String lng, String lat);
    }

    interface View extends BaseView {
        void requestFindIndexSuccess(FindIndexBean bean);

        void requestFindIndexFail(String msg);
    }

    interface Presenter {
        void requestFindIndex(String lng, String lat);
    }
}
