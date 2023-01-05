package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.EvaluateListsBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;

public interface EvaluationListsContract {
    interface Model {
        Flowable<EvaluateListsBean> evaluateLists(int cabid, int page, String repair);
    }

    interface View extends BaseView {
        void onSuccess(EvaluateListsBean bean);
    }

    interface Presenter {
        void evaluateLists(int cabid, int page, String repair);
    }
}
