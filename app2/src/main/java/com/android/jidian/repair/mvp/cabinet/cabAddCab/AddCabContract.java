package com.android.jidian.repair.mvp.cabinet.cabAddCab;

import com.android.jidian.repair.base.BaseView;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @author : xiaoming
 * date: 2023/2/3 17:57
 * description:
 */
public interface AddCabContract {
    interface Model {
        Flowable<UploadUploadUrlSetBean> requestUploadUploadUrlSet(String token);

        Observable<UploadImageBean> requestUpLoadImg(String url, File file, String upToken, String companyid);
    }

    interface View extends BaseView {
        void requestShowTips(String msg);

        void requestUploadUploadUrlSetSuccess(UploadUploadUrlSetBean.DataBean bean);

        void requestUpLoadImgSuccess(UploadImageBean bean, int index);
    }

    interface Presenter {
        void requestUploadUploadUrlSet(String token);

        void requestUpLoadImg(String url, String filePath, String upToken, String companyid, int requestCode);
    }
}
