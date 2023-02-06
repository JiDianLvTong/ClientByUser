package com.android.jidian.repair.mvp.patrol;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @author : xiaoming
 * date: 2023/2/1 17:03
 * description:
 */
public interface PatrolAddContract {
    interface Model {
        Flowable<BaseBean> requestPatrolAddpatrol(String cabid, String img1, String img2, String img3,
                                                  String img4_1, String img4_2, String img4_3, String img4_4,
                                                  String img5_1, String img5_2, String img5_3, String img6_1,
                                                  String isnetdbm, String isdixian, String isopenbtn);

        Flowable<UploadUploadUrlSetBean> requestUploadUploadUrlSet(String token);

        Observable<UploadImageBean> requestUpLoadImg(String url, File file, String upToken, String companyid);
    }

    interface View extends BaseView {
        void requestPatrolAddpatrolSuccess(BaseBean bean);

        void requestShowTips(String msg);

        void requestUploadUploadUrlSetSuccess(UploadUploadUrlSetBean.DataBean bean);

        void requestUpLoadImgSuccess(UploadImageBean bean, int index);
    }

    interface Presenter {
        void requestPatrolAddpatrol(String cabid, String img1, String img2, String img3,
                                    String img4_1, String img4_2, String img4_3, String img4_4,
                                    String img5_1, String img5_2, String img5_3, String img6_1,
                                    String isnetdbm, String isdixian, String isopenbtn);

        void requestUploadUploadUrlSet(String token);

        void requestUpLoadImg(String url, String filePath, String upToken, String companyid, int requestCode);
    }
}
