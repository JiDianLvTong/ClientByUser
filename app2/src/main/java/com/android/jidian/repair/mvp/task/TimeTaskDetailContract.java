package com.android.jidian.repair.mvp.task;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.base.BaseView;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public interface TimeTaskDetailContract {
    interface Model {
        Flowable<WorktaskDetailBean> requestWorktaskDetail(String wtid);

        Flowable<UploadUploadUrlSetBean> requestUploadUploadUrlSet(String token);

        Observable<UploadImageBean> requestUpLoadImg(String url, File file, String upToken, String companyid);

        Flowable<BaseBean> requestWorktaskResolve(String wtid, String ustat, String content, String img1, String img2, String img3, String img4);
    }

    interface View extends BaseView {
        void requestWorktaskDetailSuccess(WorktaskDetailBean.DataBean bean);

        void requestWorktaskDetailFail(String msg);

        void requestUploadUploadUrlSetSuccess(UploadUploadUrlSetBean.DataBean bean);

        void requestUpLoadImgSuccess(UploadImageBean bean, int index);

        void requestShowTips(String msg);

        void requestWorktaskResolveSuccess(BaseBean bean);
    }

    interface Presenter {
        void requestWorktaskDetail(String wtid);

        void requestUploadUploadUrlSet(String token);

        void requestUpLoadImg(String url, String filePath, String upToken, String companyid, int requestCode);

        void requestWorktaskResolve(String wtid, String ustat, String content, String img1, String img2, String img3, String img4);
    }
}
