package com.android.jidian.repair.mvp.patrol;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/1 17:03
 * description:
 */
public interface PatrolDetailContract {
    interface Model{
        Flowable<BaseBean> requestPatrolAddpatrol(String cabid, String img1, String img2, String img3, String img4_1, String img4_2, String img4_3, String img4_4);
    }
    interface View extends BaseView{
        void requestPatrolAddpatrolSuccess(BaseBean bean);

        void requestShowTips(String msg);
    }
    interface Presenter{
        void requestPatrolAddpatrol(String cabid, String img1, String img2, String img3, String img4_1, String img4_2, String img4_3, String img4_4);
    }
}
