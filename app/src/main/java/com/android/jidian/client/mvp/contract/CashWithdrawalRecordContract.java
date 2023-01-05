package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.PullCashGetCashRecordBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2021/11/26 下午3:39
 * company: 兴达智联
 * description:
 */
public interface CashWithdrawalRecordContract {

    interface Model {
        Flowable<PullCashGetCashRecordBean> requestPullCashGetCashRecord();
    }

    interface View extends BaseView{
        void requestPullCashGetCashRecordSuccess(PullCashGetCashRecordBean bean);

        void requestShowTis(String msg);
    }

    interface Presenter{
        void requestPullCashGetCashRecord();
    }

}
