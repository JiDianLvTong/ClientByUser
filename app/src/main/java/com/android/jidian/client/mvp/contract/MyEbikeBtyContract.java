package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.MyEbikeBtyBean;
import com.android.jidian.client.bean.UserConfirmBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;

public interface MyEbikeBtyContract {
    interface Model {
        Flowable<MyEbikeBtyBean> myEbikeBty(String uid);

        Flowable<UserConfirmBean> userConfirm(String uid, String id, int type, String confirm);
    }

    interface View extends BaseView {
        void onmyEbikeBtySuccess(MyEbikeBtyBean myEbikeBtyBean);

        void onmyEbikeBtyError(Throwable throwable);

        void onuserConfirmSuccess(UserConfirmBean userConfirmBean, String id, int type);

        void onuserConfirmError(Throwable throwable);
    }

    interface Presenter {
        void myEbikeBty(String uid);

        void userConfirm(String uid, String id, int type, String confirm);
    }
}