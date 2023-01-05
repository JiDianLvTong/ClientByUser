package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.EvaluateAddsBean;
import com.android.jidian.client.bean.EvaluateLabsBean;
import com.android.jidian.client.base.BaseView;

import io.reactivex.Flowable;

public interface EvaluateAddsContract {
    interface Model {
        Flowable<EvaluateAddsBean> evaluateAdds(int cabid, int stars, String labids, String content, String repair);

        Flowable<EvaluateLabsBean> evaluateLabs(int cabid, String repair);
    }

    interface View extends BaseView {
        void onSuccess1(EvaluateAddsBean bean);

        void onSuccess2(EvaluateLabsBean bean);
    }

    interface Presenter {
        void evaluateAdds(int cabid, int stars, String labids, String content, String repair);

        void evaluateLabs(int cabid, String repair);
    }
}
