package com.android.jidian.repair.mvp.main.FailureFragment;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:14
 * description:
 */
public interface FailureContract {
    interface Model {
        Flowable<BaseBean> requestWorktaskAddFault(String cabid, String content, String img1, String img2, String img3, String img4);
    }

    interface View extends BaseView {
        void requestWorktaskAddFaultSuccess(BaseBean bean);

        void requestShowTips(String msg);
    }

    interface Presenter {
        void requestWorktaskAddFault(String cabid, String content, String img1, String img2, String img3, String img4);
    }
}
