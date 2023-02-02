package com.android.jidian.repair.mvp.FailureAdd;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:14
 * description:
 */
public interface FailureContract {
    interface Model {
        Flowable<BaseBean> requestWorktaskAddFault(String cabid, String content, String img1, String img2, String img3, String img4);

        Flowable<UploadUploadUrlSetBean> requestUploadUploadUrlSet(String token);

        Observable<UploadImageBean> requestUpLoadImg(String url, File file, String upToken, String companyid);
    }

    interface View extends BaseView {
        void requestWorktaskAddFaultSuccess(BaseBean bean);

        void requestShowTips(String msg);

        void requestUploadUploadUrlSetSuccess(UploadUploadUrlSetBean.DataBean bean);

        void requestUpLoadImgSuccess(UploadImageBean bean, int index);
    }

    interface Presenter {
        void requestWorktaskAddFault(String cabid, String content, String img1, String img2, String img3, String img4);

        void requestUploadUploadUrlSet(String token);

        void requestUpLoadImg(String url, String filePath, String upToken, String companyid, int requestCode);
    }
}
