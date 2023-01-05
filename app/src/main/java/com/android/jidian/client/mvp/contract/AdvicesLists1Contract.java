package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.AdvicesAllReadBean;
import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.bean.AdvicesReadBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

public interface AdvicesLists1Contract {
    interface Model {
        Flowable<ResponseBody> advicesListsV2(int type, int page);

        Flowable<AdvicesReadBean> advicesRead(int msg_id);

        Flowable<AdvicesAllReadBean> advicesAllRead(int type);
    }

    interface View extends BaseView {
        void onAdvicesListsV2Success(AdvicesLists2Bean advicesLists1Bean);

        void onAdvicesListsV2Error(Throwable throwable);

        void onAdvicesReadSuccess(AdvicesReadBean advicesReadBean);

        void onAdvicesReadError(Throwable throwable);

        void onAdvicesAllReadSuccess(AdvicesAllReadBean advicesAllReadBean);

        void onAdvicesAllReadError(Throwable throwable);
    }

    interface Presenter {
        void advicesListsV2(int type, int page);

        void advicesRead(int msg_id);

        void advicesAllRead(int type);
    }
}