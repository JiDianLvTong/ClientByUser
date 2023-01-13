package com.android.jidian.repair.mvp.main;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:36
 * description:
 */
public interface MainContract {
    interface Model {
        Flowable<BaseBean> requestLoginCheckAcc(String uid, String apptoken, String nowlng, String nowlat);
    }

    interface View extends BaseView {
        void requestLoginCheckAccSuccess();

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestLoginCheckAcc(String uid, String apptoken, String nowlng, String nowlat);
    }
}
